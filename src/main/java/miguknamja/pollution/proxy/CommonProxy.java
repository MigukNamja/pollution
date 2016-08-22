package miguknamja.pollution.proxy;

import miguknamja.pollution.Config;
import miguknamja.pollution.ModBlocks;
import miguknamja.pollution.ModEntities;
import miguknamja.pollution.ModItems;
import miguknamja.pollution.Pollution;
import miguknamja.pollution.compat.MainCompatHandler;
import miguknamja.pollution.network.PacketHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import miguknamja.pollution.DimensionTickEvent;
import java.io.File;

public class CommonProxy {

    // Config instance
    public static Configuration config;
    DimensionTickEvent events = new DimensionTickEvent();
    
    public void preInit(FMLPreInitializationEvent e) {
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
        //NetworkRegistry.INSTANCE.registerGuiHandler(Pollution.instance, new GuiProxy());
    	MinecraftForge.EVENT_BUS.register(events);
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
