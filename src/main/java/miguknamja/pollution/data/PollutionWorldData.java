package miguknamja.pollution.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import miguknamja.pollution.Pollution;
import miguknamja.utils.ChunkKey;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
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
  
  public static double decrement( World world, BlockPos chunkPos ) {
	  double pollution = getPollution( world, chunkPos ).pollutionLevel;
	  if( pollution > PollutionDataValue.minPollutionLevel ) {
		  setPollution( pollution - 100.0, world, chunkPos );
	  }
	  return pollution;
  }

  public static double increment( World world, BlockPos chunkPos ) {
	  double pollution = getPollution( world, chunkPos ).pollutionLevel;
  	if( pollution < PollutionDataValue.maxPollutionLevel ) {
  		setPollution( pollution + 100.0, world, chunkPos );
  	}
      return pollution;
  }

  /*
   * All updates to pollution levels should ultimately come through here
   */
  public static void setPollution( PollutionDataValue pdv, World world, BlockPos chunkPos ) {
	  setPollution( pdv.pollutionLevel, world, chunkPos );
  }

  private static void setPollution( double newPollution, World world, BlockPos chunkPos ) {
	  if( newPollution < PollutionDataValue.minPollutionLevel || newPollution > PollutionDataValue.maxPollutionLevel ) {
		  return;
	  } else {		  
		  PollutionDataValue value = new PollutionDataValue( newPollution );

		  /* 1 instance of the PollutionWorldData per dimension */
		  ChunkKey key = ChunkKey.getKey( world, chunkPos );
		  PollutionWorldData instance = get( world );
		  instance.hashMap.put( key, value );
		  
		  /* Mark for saving in NBT */
		  instance.markDirty();
	  }
  }
  
  /*
   * All requests for pollution levels should ultimately come through here
   */
  public static PollutionDataValue getPollution( World world, BlockPos chunkPos ) {
	  	  
	  /* 1 instance of the PollutionWorldData per dimension */
	  ChunkKey key = ChunkKey.getKey( world, chunkPos );
	  PollutionWorldData instance = get( world );
	  return instance.hashMap.getOrDefault( key, PollutionDataValue.defaultData );
  }

  public static double getPollutionPercent( World world, BlockPos chunkPos ) {
	  return 100.0 * getPollution(world, chunkPos).pollutionLevel / PollutionDataValue.maxPollutionLevel;
  }

  public static String getPollutionString( World world, BlockPos chunkPos ) {
	  return "Pollution Level: " + getPollutionPercent(world, chunkPos) + "%";
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
