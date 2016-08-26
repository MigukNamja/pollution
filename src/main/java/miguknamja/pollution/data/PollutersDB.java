package miguknamja.pollution.data;


import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import miguknamja.pollution.network.PacketHandler;
import miguknamja.pollution.network.PacketSendPollution;
import miguknamja.utils.ChunkKey;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class PollutersDB {
	/* The known polluter blocks, read from a config or .json file  */
	public static HashSet<String> registeredPolluters = new HashSet<String>();
	
	/* The collection of instances of all polluters across all dimensions */
	public static ConcurrentHashMap<ChunkKey,PollutersPerChunk> allPolluters = new ConcurrentHashMap<ChunkKey,PollutersPerChunk>();

	public PollutersDB() {
		super();		
	}

	// TODO : Read these from a .json file
	public static final String POLLUTER_FURNACE = "tile.furnace";
	public static final String POLLUTER_ADMINBLOCK = "tile.pollution.polluteradminblock";
	public static boolean isPolluter( TileEntity te ) {
		String unlocalizedName = te.getBlockType().getUnlocalizedName();
		if( registeredPolluters.contains(unlocalizedName) ) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void registerPolluter( TileEntity te ) {
		String unlocalizedName = te.getBlockType().getUnlocalizedName();
		registerPolluter( unlocalizedName );
	}

	public static void registerPolluter( String unlocalizedName ) {
		registeredPolluters.add( unlocalizedName );
	}
	
	
	public static PollutersPerChunk getPollutersPerChunk( World world, Chunk chunk ) {
		return allPolluters.getOrDefault(ChunkKey.getKey(world, chunk), null);
	}
	
	/*
	 * Add the polluter to the database into the chunk list at the given location
	 * If already there, do nothing to the database.
	 */
	public static void addPolluterInstance( World world, BlockPos blockPos, TileEntity polluter ) {
		
		/* First, get the collection of polluters in this chunk from the global HashMap of polluters */
		PollutersPerChunk ppc = allPolluters.getOrDefault(ChunkKey.getKey(world, blockPos),null);
		if( ppc == null ) {
			/* This chunk didn't have an existing collection, so we create one */
			ppc = new PollutersPerChunk();
			allPolluters.put( ChunkKey.getKey(world, blockPos), ppc );
		}
		
		if( !ppc.polluters.containsKey( blockPos ) ) {
			ppc.polluters.put( blockPos, new DataPerPolluter(polluter) );
		}
	}

	/*
	 * Remove the polluter from the database from the chunk list at the given location
	 */
	public static void removePolluterInstance( World world, BlockPos blockPos, TileEntity polluter ) {
		
		/* First, get the collection of polluters in this chunk from the global HashMap of polluters */
		PollutersPerChunk ppc = allPolluters.getOrDefault(ChunkKey.getKey(world, blockPos),null);
		if( ppc != null ) {
			ppc.polluters.remove( blockPos );
			
			/* If the collection is now empty, remove it from global HashMap */
			if( ppc.polluters.isEmpty() ) {
				allPolluters.remove( ChunkKey.getKey(world, blockPos) );
			}
		}		
	}

	/*
	 * Call this method whenever you wish to check the polluters in this chunk
	 * against the current TileEntities in the chunk.
	 * 
	 * When a polluter block is broken it can no longer polluter and it should
	 * be removed from the polluter in the chunk it was in.
	 * 
	 * Likewise, when a polluter block is added (placed) in the chunk, it will need
	 * to be added to the list of polluter in the chunk.
	 *
	 * Scan for pollution sources in this chunk and place them in 'polluters'
	 */
	public static void scan( World world, Chunk chunk ) {
		if( world.isRemote )
			return; /* only run on the server */

		Map<BlockPos, TileEntity> tileEntities = chunk.getTileEntityMap();

		audit( world, chunk, tileEntities ); // remove old TileEntities
		populate( world, tileEntities ); // add the new TileEntities
	}
	public static void scan( World world, BlockPos blockPos ) {
		Chunk chunk = world.getChunkFromBlockCoords(blockPos);
		scan( world, chunk );
	}

	
	/*
	 * Calls scan() for every loaded chunk in this world 
	 */
	public static void scan( World world ) {
		if( world.isRemote ){ return; } // don't run on the client
		
		assert( world instanceof WorldServer );
		WorldServer worldServer = (WorldServer) world;

		// Iterate over all chunks
		for( Chunk chunk : worldServer.getChunkProvider().getLoadedChunks() ) {
        	scan( world, chunk );
		}
		//Logging.log( "PollutersDB.scan()" );
	}
	
	/*
	 * Remove polluters from our collection that no longer exist in the chunk as TileEnties
	 */ 
	private static void audit( World world, Chunk chunk, Map<BlockPos, TileEntity> tileEntities ) {
		PollutersPerChunk ppc = allPolluters.getOrDefault(ChunkKey.getKey(world, chunk),null);
		if( ppc == null ) { return; } // no polluters to consider removing
				
		/*
		 * Iterate through the collection of polluters in this chunk.
		 * Remove any that are no longer a TileEntity in this chunk.
		 */
		for( Map.Entry<BlockPos, DataPerPolluter> entry : ppc.polluters.entrySet() ) {
			BlockPos        pos = entry.getKey();
			DataPerPolluter dpp = entry.getValue();
			if( !tileEntities.containsKey(pos) ) {
				removePolluterInstance( world, pos, dpp.te );
			}
		}
	}
	
	/*
	 * Scan this chunk for new TileEntities to add to our collection of polluters
	 */
	private static void populate( World world, Map<BlockPos, TileEntity> tileEntities ) {
		for( Map.Entry<BlockPos, TileEntity> entry : tileEntities.entrySet()){
			BlockPos  pos = entry.getKey();
			TileEntity te = entry.getValue();
			if( isPolluter( te ) ) {
				//Logging.log( "Found Polluter " + te.getBlockType().getUnlocalizedName() + " at " + pos.toString() );
				addPolluterInstance( world, pos, te );
			}
		}		
	}
	
	public static String toStr() {
		String s = new String();
		
		if( allPolluters == null ){ return s; }
		s = "PollutersDB {\r\n";
		for( Map.Entry<ChunkKey,PollutersPerChunk> entry : allPolluters.entrySet() ) {
			ChunkKey          key = entry.getKey();
			PollutersPerChunk ppc = entry.getValue();
			s += "key(" + key + ")\r\n" + ppc.toString();
		}
		s += "}\r\n";
		return s;
	}

	public static void updatePlayers( World world ) {
		//Predicate<EntityPlayerMP> filter = (p)-> true;
		for( EntityPlayerMP player : world.getPlayers( EntityPlayerMP.class, (p)-> true ) ) {
			Chunk chunk = world.getChunkFromBlockCoords( player.getPosition() );
			PollutionDataValue pdv = PollutionWorldData.getPollution( world, chunk );
			PacketHandler.INSTANCE.sendTo(new PacketSendPollution(pdv), player);
		}		
	}
}
