package miguknamja.pollution.pollutersdb;


import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import miguknamja.utils.ChunkKey;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PollutersDB {
	/* The known polluter blocks, read from a config or .json file  */
	public static HashSet<String> registeredPolluters = new HashSet<String>();
	
	/* The collection of instances of all polluters across all dimensions */
	public static ConcurrentHashMap<ChunkKey,PollutersPerChunk> allPolluters = new ConcurrentHashMap<ChunkKey,PollutersPerChunk>();

	public PollutersDB() {
		super();		
	}

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
	
	
	public static PollutersPerChunk getPollutersPerChunk( World world, BlockPos chunkPos ) {
		return allPolluters.getOrDefault(ChunkKey.getKey(world, chunkPos), null);
	}
	
	/*
	 * Add the polluter to the database into the chunk list at the given location
	 */
	public static void addPolluterInstance( World world, BlockPos blockPos, TileEntity polluter ) {
		
		/* First, get the collection of polluters in this chunk from the global HashMap of polluters */
		PollutersPerChunk ppc = allPolluters.getOrDefault(ChunkKey.getKey(world, blockPos),null);
		if( ppc == null ) {
			/* This chunk didn't have an existing collection, so we create one */
			ppc = new PollutersPerChunk();
			allPolluters.put( ChunkKey.getKey(world, blockPos), ppc );
		}
		
		ppc.polluters.put( blockPos, new DataPerPolluter(polluter) );
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
	public static void scan( World world, BlockPos blockPos ) {
		if( world.isRemote )
			return; /* only run on the server */

		Chunk chunk = world.getChunkFromBlockCoords(blockPos);
		Map<BlockPos, TileEntity> tileEntities = chunk.getTileEntityMap();

		audit( world, blockPos, tileEntities ); // remove old TileEntities
		populate( world, blockPos, tileEntities ); // add the new TileEntities
	}
	
	/*
	 * Remove polluters from our collection that no longer exist in the chunk as TileEnties
	 */ 
	private static void audit( World world, BlockPos blockPos, Map<BlockPos, TileEntity> tileEntities ) {
		PollutersPerChunk ppc = allPolluters.getOrDefault(ChunkKey.getKey(world, blockPos),null);
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
	private static void populate( World world, BlockPos blockPos, Map<BlockPos, TileEntity> tileEntities ) {
		for( Map.Entry<BlockPos, TileEntity> entry : tileEntities.entrySet()){
			BlockPos  pos = entry.getKey();
			TileEntity te = entry.getValue();
			if( isPolluter( te ) ) {
				System.out.println( "Found Polluter " + te.getBlockType().getUnlocalizedName() + " at " + pos.toString() );
				addPolluterInstance( world, pos, te );
			}
		}		
	}
	
	// TODO : Return the contents of the entire database as a string
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
}
