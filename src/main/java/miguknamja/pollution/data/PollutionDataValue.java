package miguknamja.pollution.data;

import miguknamja.pollution.Config;

public class PollutionDataValue {
	public double pollutionLevel;
	public static final PollutionDataValue defaultData = new PollutionDataValue( Config.minPollutionLevel );

	/**
	 * Returns true if pollution is at minimum
	 * 
	 * @return
	 */
	public boolean isClean() {
		return this.pollutionLevel == Config.minPollutionLevel;
	}
	
	/**
	 * Ensure pollution is within allowed bounds
	 */
	private void normalize() {
		if( pollutionLevel < Config.minPollutionLevel ){ pollutionLevel = Config.minPollutionLevel; }
		else if( pollutionLevel > Config.maxPollutionLevel ){ pollutionLevel = Config.maxPollutionLevel; }
	}
	
	/**
	 * Mutable. Warning : modifies this !
	 * @param rhs
	 * @return
	 */
	public void addTo( PollutionDataValue rhs ) {
		this.pollutionLevel += rhs.pollutionLevel;
		normalize();
	}
	
	/**
	 * Immutable. Does not modify this. Instead, returns a new value.
	 * @param rhs
	 * @return new modified PollutionDataValue, which is different from this
	 */
	public PollutionDataValue add( PollutionDataValue that ) {
		PollutionDataValue pdv = new PollutionDataValue( this.pollutionLevel + that.pollutionLevel );
		pdv.normalize();
		return( pdv );
	}
	
	/**
	 * Immutable. Does not modify this. Instead, returns a new value.
	 * @param rhs
	 * @return new modified PollutionDataValue, which is different from this
	 */
	public PollutionDataValue mult( double factor ) {
		PollutionDataValue pdv = new PollutionDataValue( this.pollutionLevel * factor );
		pdv.normalize();
		return( pdv );
	}
	
	/**
	 * Immutable. Does not modify this. Instead, returns a new value.
	 * @return new modified PollutionDataValue
	 */
	public PollutionDataValue negative() {
		PollutionDataValue pdv = new PollutionDataValue( -this.pollutionLevel );
		pdv.normalize();
		return( pdv );
	}

	public PollutionDataValue() {
		pollutionLevel = Config.minPollutionLevel;
	}

	public PollutionDataValue( double p ) {
		pollutionLevel = p;
	}
	
	public PollutionDataValue( String s ) {
		pollutionLevel = Double.parseDouble( s );
	}
	
	public double percent() {
		return (100.0 * pollutionLevel) / Config.maxPollutionLevel;
	}
	
	public void setPercent( double percent ) {
		pollutionLevel = (percent / 100.0) * Config.maxPollutionLevel;
	}

	@Override
	public String toString() {
		return Double.toString(pollutionLevel);
	}
	
	public String getPollutionString() {
		return "Pollution Level: " + percent() + "%";
	}
}

