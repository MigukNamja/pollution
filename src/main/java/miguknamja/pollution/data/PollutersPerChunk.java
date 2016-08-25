package miguknamja.pollution.data;

/*
 * A collection of polluters in a chunk
 * 
 * When pollution is calculated and updated per chunk, it uses
 * this list to get the list of polluters in a chunk, where chunk
 * is identified by BlockPos.
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.math.BlockPos;

public class PollutersPerChunk {
	public ConcurrentHashMap<BlockPos, DataPerPolluter> polluters;

	public PollutersPerChunk() {
		super();
		this.polluters = new ConcurrentHashMap<BlockPos, DataPerPolluter>();
	}
	
	@Override
	public String toString() {
		String s = new String();
		if( polluters == null ){ return s; }
		s = "PolluterPerChunk {\r\n";
		for( Map.Entry<BlockPos, DataPerPolluter> entry : polluters.entrySet() ) {
			BlockPos        pos = entry.getKey();
			DataPerPolluter dpp = entry.getValue();
			s += "pos(" + pos + ") data(" + dpp + ")\r\n";
		}
		s += "}\r\n";
		return s;
	}
}
