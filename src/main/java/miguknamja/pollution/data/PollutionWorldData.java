package miguknamja.pollution.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import miguknamja.pollution.Pollution;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
  
  // Required constructors
  public PollutionWorldData() {
	  super(DATA_NAME);
	  hashMap = new HashMap<PollutionDataKey, PollutionDataValue>();
  }
  public PollutionWorldData(String s) {
	  super(s);
	  hashMap = new HashMap<PollutionDataKey, PollutionDataValue>();
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

		  /* 1 instance of the PollutionWorldData per dimension */
		  PollutionDataKey key = getKey( world, position );
		  PollutionWorldData instance = get( world );
		  instance.hashMap.put( key, value );
		  
		  /* Mark for saving in NBT */
		  instance.markDirty();
	  }
  }
  
  /*
   * All requests for pollution levels should ultimately come through here
   */
  public static int getPollution( World world, BlockPos position ) {
	  	  
	  /* 1 instance of the PollutionWorldData per dimension */
	  PollutionDataKey key = getKey( world, position );
	  PollutionWorldData instance = get( world );
	  PollutionDataValue data = instance.hashMap.getOrDefault( key, PollutionDataValue.defaultData );

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
	  
	  NBTTagList tagList = nbt.getTagList(DATA_NAME, NBT_TAG_STRING);
	  
	  int i = 0;
	  while( i < tagList.tagCount() ) {
		  PollutionDataKey     key = new PollutionDataKey  ( tagList.getStringTagAt(i++) );
		  PollutionDataValue value = new PollutionDataValue( tagList.getStringTagAt(i++) );
		  hashMap.put( key, value );
	  }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	  
	  NBTTagList tagList = new NBTTagList();
	  nbt.setTag( DATA_NAME, tagList );
	  
      for( Map.Entry<PollutionDataKey, PollutionDataValue> entry : hashMap.entrySet()){
		  tagList.appendTag(new NBTTagString(entry.getKey().toString()));
		  tagList.appendTag(new NBTTagString(entry.getValue().toString()));
	  }
	  
	  return nbt;
  }

  /* Required by WorldSavedData */
  public static PollutionWorldData get( World world ) {
	  int dim = world.provider.getDimension();
	  MapStorage thisDimensionStorage = world.getMapStorage();
	  
	  /* Try the hashMap first */
	  DimensionIdKey dimKey = new DimensionIdKey( dim );
	  PollutionWorldData instance = instanceByDimension.getOrDefault( dimKey, null );
	  if ( instance == null ) {
		  instance = (PollutionWorldData) thisDimensionStorage.getOrLoadData(PollutionWorldData.class, DATA_NAME);

		  /* No saved data. Must be a new world */
		  if (instance == null) {
			  instance = new PollutionWorldData();
			  thisDimensionStorage.setData( DATA_NAME, instance );
			  instanceByDimension.put( dimKey, instance );
		  }
	  }
	  
	  return instance;
  }
  
  private static PollutionDataKey getKey(World world, BlockPos position) {
	  Chunk chunk = world.getChunkFromBlockCoords(position);
	  int dim = world.provider.getDimension();
	  PollutionDataKey key = new PollutionDataKey( dim, chunk.xPosition, chunk.zPosition );
	  return key;
  }
  
  private HashMap<PollutionDataKey, PollutionDataValue> hashMap;  
  private static HashMap<DimensionIdKey, PollutionWorldData> instanceByDimension = new HashMap<DimensionIdKey, PollutionWorldData>();
  private static int minPollutionLevel = PollutionDataValue.minPollutionLevel;
  private static int maxPollutionLevel = 20; // TODO : read this value from a config
  public static final int NBT_TAG_STRING = 8; // Reference : http://wiki.vg/NBT
}
