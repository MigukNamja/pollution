package miguknamja.pollution.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import miguknamja.pollution.Config;
import miguknamja.pollution.Pollution;
import miguknamja.utils.ChunkKey;
import miguknamja.utils.DimensionIdKey;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
	  hashMap = new ConcurrentHashMap<ChunkKey, PollutionDataValue>();
  }
  public PollutionWorldData(String s) {
	  super(s);
	  hashMap = new ConcurrentHashMap<ChunkKey, PollutionDataValue>();
  }
  
  public static double decrement( World world, Chunk chunk ) {
	  double pollution = getPollution( world, chunk ).pollutionLevel;
	  if( pollution > Config.minPollutionLevel ) {
		  setPollution( pollution - 100.0, world, chunk );
	  }
	  return pollution;
  }

  public static double increment( World world, Chunk chunk ) {
	  double pollution = getPollution( world, chunk ).pollutionLevel;
  	if( pollution < Config.maxPollutionLevel ) {
  		setPollution( pollution + 100.0, world, chunk );
  	}
      return pollution;
  }

  /*
   * All updates to pollution levels should ultimately come through here
   */
  public static void setPollution( PollutionDataValue pdv, World world, Chunk chunk ) {
	  setPollution( pdv.pollutionLevel, world, chunk );
  }

  private static void setPollution( double newPollution, World world, Chunk chunk ) {
	  if( newPollution < Config.minPollutionLevel || newPollution > Config.maxPollutionLevel ) {
		  return;
	  } else {		  
		  PollutionDataValue value = new PollutionDataValue( newPollution );

		  /* 1 instance of the PollutionWorldData per dimension */
		  ChunkKey key = ChunkKey.getKey( world, chunk );
		  PollutionWorldData instance = get( world );
		  instance.hashMap.put( key, value );
		  
		  /* Mark for saving in NBT */
		  instance.markDirty();
	  }
  }
  
  /*
   * All requests for pollution levels should ultimately come through here
   */
  public static PollutionDataValue getPollution( World world, Chunk chunk ) {
	  	  
	  /* 1 instance of the PollutionWorldData per dimension */
	  ChunkKey key = ChunkKey.getKey( world, chunk );
	  PollutionWorldData instance = get( world );
	  return instance.hashMap.getOrDefault( key, PollutionDataValue.defaultData );
  }

  public static double getPollutionPercent( World world, Chunk chunk ) {
	  return 100.0 * getPollution(world, chunk).pollutionLevel / Config.maxPollutionLevel;
  }

  public static String getPollutionString( World world, Chunk chunk ) {
	  return "Pollution Level: " + getPollutionPercent(world, chunk) + "%";
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {	 
	  
	  NBTTagList tagList = nbt.getTagList(DATA_NAME, NBT_TAG_STRING);
	  
	  int i = 0;
	  while( i < tagList.tagCount() ) {
		  ChunkKey     key = new ChunkKey  ( tagList.getStringTagAt(i++) );
		  PollutionDataValue value = new PollutionDataValue( tagList.getStringTagAt(i++) );
		  hashMap.put( key, value );
	  }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	  
	  NBTTagList tagList = new NBTTagList();
	  nbt.setTag( DATA_NAME, tagList );
	  
      for( Map.Entry<ChunkKey, PollutionDataValue> entry : hashMap.entrySet()){
    	  String key   = entry.getKey().toString();
    	  String value = entry.getValue().toString();
		  tagList.appendTag(new NBTTagString(key));
		  tagList.appendTag(new NBTTagString(value));
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
  
  private ConcurrentHashMap<ChunkKey, PollutionDataValue> hashMap;  
  private static ConcurrentHashMap<DimensionIdKey, PollutionWorldData> instanceByDimension = new ConcurrentHashMap<DimensionIdKey, PollutionWorldData>();
  public static final int NBT_TAG_STRING = 8; // Reference : http://wiki.vg/NBT
}
