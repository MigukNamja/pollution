package miguknamja.pollution;

import org.apache.logging.log4j.Logger;

import miguknamja.pollution.pollutersdb.PollutersDB;
import miguknamja.pollution.proxy.CommonProxy;
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
    public static final String VERSION = "0.0.6";
        
    @SidedProxy(clientSide = "miguknamja.pollution.proxy.ClientProxy", serverSide = "miguknamja.pollution.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Pollution instance;
    
    public static Logger logger;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	// TODO : Move to ServerPoxy and read these from a config or .json file
    	PollutersDB.registerPolluter("tile.furnace"); 
		PollutersDB.registerPolluter("tile.pollution.polluterblock");
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
