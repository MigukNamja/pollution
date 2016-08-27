package miguknamja.pollution;

import java.util.Map;

import miguknamja.pollution.data.DataPerPolluter;
import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.data.PollutersPerChunk;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import miguknamja.utils.ChunkKey;
import miguknamja.utils.ChunkMath;
import miguknamja.utils.Logging;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/*
 * This class implements the following actions:
 * 1. Calculating pollution output per polluter per update period, i.e. every X number of ticks
 * 2. Spreading of pollution across chunks
 * 
 * What this class does *not* do is implement actions for what happens with the pollution, i.e. the pollution penalties
 */

public class PollutionGeneration {
	
	/**
	 * Generate pollution from polluters
	 * 
	 * @param world
	 * @return
	 */
	public static void generateFromPolluters( World world ) {
		// Get a *copy* of the pollutedChunks since we will updating all chunks and don't want to be modifying the collection while we're iterating over it
		//ConcurrentHashMap<ChunkKey, PollutionDataValue> pollutedChunks = new ConcurrentHashMap<ChunkKey, PollutionDataValue>( PollutionWorldData.getPollutedChunks( world ) );

		// Iterate over copy and update pollution in all these chunks, which may also cause pollution to spread to adjacent chunks
		for( Map.Entry<ChunkKey, PollutersPerChunk> entry : PollutersDB.allPolluters.entrySet() ){
			Chunk chunk = entry.getKey().getChunk(world);
			generateFromPolluters( world, chunk );
		}
	}

	/*
	 * update the pollution level in the chunk and neighboring chunks and return the new pollution value from the original (source) chunk
	 */
	public static PollutionDataValue generateFromPolluters( World world, Chunk chunk ) {
		PollutionDataValue pollutionJustAdded = new PollutionDataValue();
		PollutionDataValue curPollution = PollutionWorldData.getPollution( world, chunk );
		PollutersPerChunk ppc = PollutersDB.getPollutersPerChunk( world, chunk );
		if( ppc == null ){ return curPollution; }
		
		for( Map.Entry<BlockPos, DataPerPolluter> entry : ppc.polluters.entrySet() ) {
			DataPerPolluter dpp = entry.getValue();
			TileEntity      te  = dpp.te;
			String        name  = te.getBlockType().getUnlocalizedName();
			switch( name ) {

			case PollutersDB.POLLUTER_FURNACE:
				TileEntityFurnace furnace = (TileEntityFurnace)te;
				if( furnace.isBurning() ){
					int[] slots = furnace.getSlotsForFace(EnumFacing.DOWN);
					ItemStack fuelStack = furnace.getStackInSlot(slots[1]); // fuel slot is slot 1 for a vanilla furnace
					int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);					
					pollutionJustAdded.addTo( pollutionAdded( burnTime ) ); /* increase pollution levels */
				}
				break;

			case PollutersDB.POLLUTER_ADMINBLOCK:
				Logging.log( name );
				break;

			default:
				break;
			}
		}
		
		curPollution.addTo( pollutionJustAdded );
		PollutionWorldData.setPollution( curPollution, world, chunk );
		spreadPollution( world, chunk, pollutionJustAdded );
		return curPollution;
	}
	
	/**
	 * Spreads pollution in the surrounding area (chunks) using the following diamond pattern:
	 * .......
	 * ...s...
	 * ..sps..
	 * .spXps.
	 * ..sps..
	 * ...s...
	 * .......
	 * 
	 * X = pollution source
	 * p = primary spreading by a factor of Config.primaryPollutionSpreadFactor
	 * s = secondary spreading by a factor of Config.primaryPollutionSpreadFactor^2
	 * . = no spreading
	 * 
	 * @param world
	 * @param chunk
	 * @param pdv
	 */
	public static void spreadPollution( World world, Chunk chunk, PollutionDataValue pdv ) {
		if( Config.primaryPollutionSpreadFactor <= 0.0f ){ return; } // return immediately if spreading has been disabled

		Chunk n = ChunkMath.chunkNorthOf( world, chunk );
		Chunk s = ChunkMath.chunkSouthOf( world, chunk );
		Chunk e = ChunkMath.chunkEastOf( world, chunk );
		Chunk w = ChunkMath.chunkWestOf( world, chunk );

		double f = (double)Config.primaryPollutionSpreadFactor;
		PollutionDataValue primarySpread = pdv.mult(f);
		PollutionWorldData.increase( primarySpread, world, n );
		PollutionWorldData.increase( primarySpread, world, s );
		PollutionWorldData.increase( primarySpread, world, e );
		PollutionWorldData.increase( primarySpread, world, w );

		PollutionDataValue secondarySpread = pdv.mult(f*f);
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkNorthOf( world, n ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkWestOf( world, n ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkEastOf( world, n ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkWestOf( world, w ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkEastOf( world, e ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkWestOf( world, s ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkEastOf( world, s ) );
		PollutionWorldData.increase( secondarySpread, world, ChunkMath.chunkSouthOf( world, s ) );
	}

	public static PollutionDataValue pollutionAdded( int burnTime ) {
		if( burnTime == 0 ){ return new PollutionDataValue(); }
		/*
		 * Copied from TileEntityFurnace for reference
		 * 
		 * if (item == Items.STICK) return 100;
		 * if (item == Items.COAL) return 1600;
		 * if (item == Items.LAVA_BUCKET) return 20000;
		 * if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
		 * if (item == Items.BLAZE_ROD) return 2400;
		 * 
		 * And, let's figure coal coke is 6400 in most packs
		 */		
		//Logging.log( "burnTime " + burnTime );
		
		/* longer burn time is more efficient, i.e. pollutes less since it burns more efficiently */
		return new PollutionDataValue( Math.sqrt( 6400.0 / burnTime ) );
	}
}
