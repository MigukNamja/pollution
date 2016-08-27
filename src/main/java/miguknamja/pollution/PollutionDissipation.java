package miguknamja.pollution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import miguknamja.utils.ChunkKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PollutionDissipation {
	/**
	 * Dissipate pollution from all polluted chunks
	 * 
	 * @param world
	 * @return
	 */
	public static void dissipate( World world ) {
		// Get a *copy* of the pollutedChunks since we will updating all chunks and don't want to be modifying the collection while we're iterating over it
		ConcurrentHashMap<ChunkKey, PollutionDataValue> pollutedChunks = new ConcurrentHashMap<ChunkKey, PollutionDataValue>( PollutionWorldData.getPollutedChunks( world ) );

		// Iterate over copy and update pollution in all these chunks, which may also cause pollution to spread to adjacent chunks
		for( Map.Entry<ChunkKey, PollutionDataValue> entry : pollutedChunks.entrySet() ){
			Chunk chunk = entry.getKey().getChunk(world);
			PollutionDataValue pdv = dissipate( world, chunk );
			
			// Check to see if chunk is pollution-free and if it (no longer) contains any pollution generators
			if( pdv.isClean() && !PollutersDB.contains( world, chunk ) ) {
				// TODO : remove this chunk from our data structures
			}
		}
	}

	/**
	 * Dissipate pollution from this chunk
	 * 
	 * @param world
	 * @param chunk
	 * @return
	 */
	public static PollutionDataValue dissipate( World world, Chunk chunk ) {
		return PollutionWorldData.decreasePercentOfCurrent( Config.dissipationFactor / 100.0, world, chunk );
	}
}