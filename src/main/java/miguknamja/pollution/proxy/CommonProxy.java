package miguknamja.pollution.proxy;

import miguknamja.pollution.Config;
import miguknamja.pollution.ModBlocks;
import miguknamja.pollution.ModEntities;
import miguknamja.pollution.ModItems;
import miguknamja.pollution.Pollution;
import miguknamja.pollution.compat.MainCompatHandler;
import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.events.ServerEventHandler;
import miguknamja.pollution.network.PacketHandler;
import miguknamja.utils.Logging;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy {

    // Config instance
    public static Configuration config;
    
    public void preInit(FMLPreInitializationEvent e) {
    	Logging.log( "CommonProxy.preInit()" );
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "pollution.cfg"));
        Config.readConfig();

        // Initialize our packet handler. Make sure the name is
        // 20 characters or less!
        PacketHandler.registerMessages(Pollution.MODID);

        // Initialization of blocks and items typically goes here:
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        //ModDimensions.init();

        MainCompatHandler.registerWaila();
        //MainCompatHandler.registerTOP();        
    }

    public void init(FMLInitializationEvent e) {
    	Logging.log( "CommonProxy.init()" );
        //NetworkRegistry.INSTANCE.registerGuiHandler(Pollution.instance, new GuiProxy());
    	MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }

    public void postInit(FMLPostInitializationEvent e) {
    	Logging.log( "CommonProxy.postInit()" );
        if (config.hasChanged()) {
            config.save();
        }
        
    	// TODO : Read these from a config or .json file
    	PollutersDB.registerPolluter("tile.furnace"); 
		PollutersDB.registerPolluter("tile.pollution.polluterblock");
    }
}
