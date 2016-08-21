package miguknamja.pollution;

import org.apache.logging.log4j.Logger;

import miguknamja.pollution.proxy.CommonProxy;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Pollution.MODID, name = Pollution.MODNAME, version = Pollution.VERSION, dependencies = "required-after:Forge@[12.18.1.2011,)", useMetadata = true)
public class Pollution
{
    public static final String MODID = "pollution";
    public static final String MODNAME = "Pollution";
    public static final String VERSION = "0.0.4";
    
    @SidedProxy(clientSide = "miguknamja.pollution.proxy.ClientProxy", serverSide = "miguknamja.pollution.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Pollution instance;
    
    public static Logger logger;

    private static int minPollutionLevel = 0;
    private static int maxPollutionLevel = 20; // TODO : read this value from a config
    private static int pollutionLevel = minPollutionLevel; // TODO : read from NBT based open chunk

    public static int decrement( BlockPos position ) {
    	int pollution = getPollution( position );
    	if( pollution > minPollutionLevel ) {
    		setPollution( pollution - 1, position );
    	}
        return pollution;
    }

    public static int increment( BlockPos position ) {
    	int pollution = getPollution( position );
    	if( pollution < maxPollutionLevel ) {
    		setPollution( pollution + 1, position );
    	}
        return pollution;
    }

    public static void setPollution( int newPollution, BlockPos position ) {
    	if( newPollution < minPollutionLevel || newPollution > maxPollutionLevel ) {
    		return;
    	} else {
    		pollutionLevel = newPollution; // TODO : save to NBT based open chunk
    	}
    }
    
    public static int getPollution(BlockPos position) {
        return pollutionLevel; // TODO : read from NBT based open chunk
    }

    public static double getPollutionPercent(BlockPos position) {
        return 100.0 * getPollution(position) / maxPollutionLevel;
    }
    
    public static String getPollutionString(BlockPos position) {
    	return "Pollution Level: " + getPollutionPercent(position) + "%";
    }
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        //System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        //event.registerServerCommand(new TeleportCommand());
    }
    
}
