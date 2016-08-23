package miguknamja.pollution;

/*
 * This class implements the following actions:
 * 1. Scanning for and identifying pollution sources, i.e. "polluters"
 * 2. Calculating pollution output per polluter per update period, i.e. every X number of ticks
 * 3. Calculating the new pollution value for a chunk, based upon output per polluter in that chunk
 * 4. Spreading of pollution across chunks
 * 
 * What this class does *not* do is implement actions for what happens with the pollution, i.e. the pollution penalties
 */

public class PollutionUpdater {
    
	/* The update period for performing scans */
	private int ticksPerUpdatePeriod;
	
	public PollutionUpdater() {
		super();
		this.ticksPerUpdatePeriod = 20; // TODO : Read this from a config
	}
	
	public PollutionUpdater(int ticksPerUpdatePeriod) {
		this();
		this.ticksPerUpdatePeriod = ticksPerUpdatePeriod;
	}
	
	public int getTicksPerUpdatePeriod() {
		return ticksPerUpdatePeriod;
	}

	public void setTicksPerUpdatePeriod(int ticksPerUpdatePeriod) {
		this.ticksPerUpdatePeriod = ticksPerUpdatePeriod;
	}
}
