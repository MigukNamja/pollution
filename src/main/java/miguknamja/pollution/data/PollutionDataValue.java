package miguknamja.pollution.data;

import miguknamja.pollution.Config;

public class PollutionDataValue {
	public double pollutionLevel;
	public static final PollutionDataValue defaultData = new PollutionDataValue( Config.minPollutionLevel );
	
	public void addTo( PollutionDataValue rhs ) {
		this.pollutionLevel += rhs.pollutionLevel;
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

