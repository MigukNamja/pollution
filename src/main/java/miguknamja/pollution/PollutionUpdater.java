package miguknamja.pollution;

import java.util.Map;

import miguknamja.pollution.data.DataPerPolluter;
import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.data.PollutersPerChunk;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * This class implements the following actions:
 * 1. Calculating pollution output per polluter per update period, i.e. every X number of ticks
 * 2. Spreading of pollution across chunks
 * 
 * What this class does *not* do is implement actions for what happens with the pollution, i.e. the pollution penalties
 */

public class PollutionUpdater {
    
	/* The update period for performing scans */
	public static int ticksPerUpdatePeriod;
	
	/*
	 * update the pollution level in the chunk and return the new pollution value
	 */
	public static PollutionDataValue calcPollution( World world, BlockPos chunkPos ) {
		PollutionDataValue curPollution = PollutionWorldData.getPollution( world, chunkPos );
		PollutersPerChunk ppc = PollutersDB.getPollutersPerChunk( world, chunkPos );
		if( ppc == null ){ return curPollution; }
		
		for( Map.Entry<BlockPos, DataPerPolluter> entry : ppc.polluters.entrySet() ) {
			DataPerPolluter dpp = entry.getValue();
			TileEntity      te  = dpp.te;
			String        name  = te.getBlockType().getUnlocalizedName();
			switch( name ) {

			case "tile.furnace":
				TileEntityFurnace furnace = (TileEntityFurnace)te;
				if( furnace.isBurning() ){
					int[] slots = furnace.getSlotsForFace(EnumFacing.DOWN);
					ItemStack fuelStack = furnace.getStackInSlot(slots[1]); // fuel slot is slot 1 for a vanilla furnace
					int burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);					
					curPollution.addTo( pollutionAdded( burnTime ) ); /* increase pollution levels */
				}
				break;

			case "tile.pollution.polluterblock":
				System.out.println( name );
				break;

			default:
				break;
			}
		}
		
		PollutionWorldData.setPollution( curPollution, world, chunkPos );
		return curPollution;
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
		System.out.println( "burnTime " + burnTime );
		
		/* longer burn time is more efficient, i.e. pollutes less since it burns more efficiently */
		return new PollutionDataValue( Math.sqrt( 6400.0 / burnTime ) );
	}
}
