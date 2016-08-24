package miguknamja.pollution.data;

public class PollutionDataValue {
	public double pollutionLevel;
	public static final double minPollutionLevel = 0.0;
	public static final double maxPollutionLevel = 2000.0; // TODO : read this value from a config
	public static final PollutionDataValue defaultData = new PollutionDataValue( minPollutionLevel );
	
	public void addTo( PollutionDataValue rhs ) {
		this.pollutionLevel += rhs.pollutionLevel;
	}
	
	public PollutionDataValue() {
		pollutionLevel = minPollutionLevel;
	}

	public PollutionDataValue( double p ) {
		pollutionLevel = p;
	}
	
	public PollutionDataValue( String s ) {
		pollutionLevel = Double.parseDouble( s );
	}

	@Override
	public String toString() {
		return Double.toString(pollutionLevel);
	}
	
	public String getPollutionString() {
		return "Pollution Level: " + ((100.0 * pollutionLevel) / maxPollutionLevel) + "%";
	}
}

