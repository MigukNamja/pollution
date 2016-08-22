package miguknamja.pollution.data;

public class PollutionDataValue {
	public static final int minPollutionLevel = 0;
	
	public PollutionDataValue( int p ) {
		pollutionLevel = p;
	}
	
	public final int pollutionLevel;
	
	public static final PollutionDataValue defaultData = new PollutionDataValue( minPollutionLevel );
}
