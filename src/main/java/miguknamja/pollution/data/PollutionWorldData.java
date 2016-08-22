package miguknamja.pollution.data;

import java.util.HashMap;

import miguknamja.pollution.Pollution;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapStorage;

/*
 *  Reference: https://mcforge.readthedocs.io/en/latest/datastorage/worldsaveddata/
 */
public class PollutionWorldData extends WorldSavedData {
  private static final String DATA_NAME = Pollution.MODID + "_PollutionData";
  private static PollutionWorldData instance = null;
  
  // Required constructors
  public PollutionWorldData() { super(DATA_NAME); }
  public PollutionWorldData(String s) { super(s); }
  
  public static PollutionWorldData getInstance() {
	  if( instance == null) {
		  instance = new PollutionWorldData();
	  }
	  
	  /* Just 1 instance of HashMap */
	  if( instance.hashMap == null ){
		  instance.hashMap = new HashMap<PollutionDataKey, PollutionDataValue>();
	  }
	  return instance;
  }

  public static int decrement( World world, BlockPos position ) {
	  int pollution = getPollution( world, position );
	  if( pollution > minPollutionLevel ) {
		  setPollution( pollution - 1, world, position );
	  }
	  return pollution;
  }

  public static int increment( World world, BlockPos position ) {
  	int pollution = getPollution( world, position );
  	if( pollution < maxPollutionLevel ) {
  		setPollution( pollution + 1, world, position );
  	}
      return pollution;
  }

  /*
   * All updates to pollution levels should ultimately come through here
   */
  public static void setPollution( int newPollution, World world, BlockPos position ) {	  
	  if( newPollution < minPollutionLevel || newPollution > maxPollutionLevel ) {
		  return;
	  } else {		  
		  PollutionDataValue value = new PollutionDataValue( newPollution );

		  /* make the key - keymaster ! */
		  Chunk chunk = world.getChunkFromBlockCoords(position);
		  int dim = world.provider.getDimension();
		  PollutionDataKey key = new PollutionDataKey( dim, chunk.xPosition, chunk.zPosition );

		  /* Just 1 instance of the hashMap */
		  PollutionWorldData i = getInstance();
		  i.hashMap.put( key, value );
	  }
  }
  
  /*
   * All requests for pollution levels should ultimately come through here
   */
  public static int getPollution( World world, BlockPos position ) {
	  
	  Chunk chunk = world.getChunkFromBlockCoords(position);
	  int dim = world.provider.getDimension();
	  PollutionDataKey key = new PollutionDataKey( dim, chunk.xPosition, chunk.zPosition );
	  
	  /* Just 1 instance of the hashMap */
	  PollutionWorldData i = getInstance();
	  PollutionDataValue data = i.hashMap.getOrDefault( key, PollutionDataValue.defaultData );

	  return data.pollutionLevel;
  }

  public static double getPollutionPercent( World world, BlockPos position ) {
	  return 100.0 * getPollution(world, position) / maxPollutionLevel;
  }

  public static String getPollutionString( World world, BlockPos position ) {
	  return "Pollution Level: " + getPollutionPercent(world, position) + "%";
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
	  // TODO Auto-generated method stub
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	  // TODO Auto-generated method stub
	  return null;
  }

  public static PollutionWorldData get(World world) {

	  MapStorage thisGlobalStorage = world.getMapStorage();

	  PollutionWorldData instance = (PollutionWorldData) thisGlobalStorage.getOrLoadData(PollutionWorldData.class, DATA_NAME);

	  /* No saved data. Must be a new world */
	  if (instance == null) {
		  instance = new PollutionWorldData();
		  thisGlobalStorage.setData(DATA_NAME, instance);
	  }
	  return instance;
  }
  
  private HashMap<PollutionDataKey, PollutionDataValue> hashMap;
  private static int minPollutionLevel = PollutionDataValue.minPollutionLevel;
  private static int maxPollutionLevel = 20; // TODO : read this value from a config
}
