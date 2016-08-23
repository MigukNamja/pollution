package miguknamja.pollution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * This class implements the following actions:
 * 1. Scanning for and identifying pollution sources, i.e. "polluters"
 * 2. Calculating pollution output per polluter per update period, i.e. every X number of ticks
 * 3. Calculating the new pollution value for a chunk, based upon output per polluter in that chunk
 * 4. Spreading of pollution across chunks
 * 
 * What this class does *not* do is implement actions for what happens with the pollution, i.e. the pollution penalties
 */
import net.minecraft.world.chunk.Chunk;

public class PollutionUpdater {
    
	/* The update period for performing scans */
	private int ticksPerUpdatePeriod;

	/* Map (list) of polluters in this chunk */
	private HashMap<BlockPos, TileEntity> polluters;
	
	/* The known polluter blocks, read from a config or .json file  */
	private HashSet<String> registeredPolluters;

	public PollutionUpdater() {
		super();
		this.ticksPerUpdatePeriod = 20; // TODO : Read this from a config
		this.polluters = new HashMap<BlockPos, TileEntity>();
		this.registeredPolluters = new HashSet<String>();
		
		registeredPolluters.add("tile.furnace"); // TODO : Read this from a config or .json file
		registeredPolluters.add("tile.pollution.polluterblock"); // TODO : Read this from a config or .json file
	}
	
	public PollutionUpdater(int ticksPerUpdatePeriod) {
		this();
		this.ticksPerUpdatePeriod = ticksPerUpdatePeriod;
	}
	
	/* Scan for pollution sources in this chunk and place them in 'polluters' */
	public void scan( World world, BlockPos blockPos ) {
		if( world.isRemote )
			return; /* only run on the server */

		Chunk chunk = world.getChunkFromBlockCoords(blockPos);
		Map<BlockPos, TileEntity> tileEntities = chunk.getTileEntityMap();
		for( Map.Entry<BlockPos, TileEntity> entry : tileEntities.entrySet()){
			TileEntity te = entry.getValue();
			BlockPos pos = entry.getKey();
			if( isPolluter( te ) ) {
				System.out.println( "Found Polluter " + te.getBlockType().getUnlocalizedName() + " at " + pos.toString() );
				polluters.put( blockPos, te );
			}
		}
	}
	
	private boolean isPolluter( TileEntity te ) {
		String unlocalizedName = te.getBlockType().getUnlocalizedName();
		if( registeredPolluters.contains(unlocalizedName) ) {
			return true;
		} else {
			return false;
		}
	}

	public HashMap<BlockPos, TileEntity> getPolluters() {
		return polluters;
	}

	public void setPolluters(HashMap<BlockPos, TileEntity> polluters) {
		this.polluters = polluters;
	}

	public int getTicksPerUpdatePeriod() {
		return ticksPerUpdatePeriod;
	}

	public void setTicksPerUpdatePeriod(int ticksPerUpdatePeriod) {
		this.ticksPerUpdatePeriod = ticksPerUpdatePeriod;
	}
}
