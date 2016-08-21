package miguknamja.moda.proxy;

import miguknamja.moda.Config;
import miguknamja.moda.ModA;
import miguknamja.moda.ModBlocks;
import miguknamja.moda.ModEntities;
import miguknamja.moda.ModItems;
import miguknamja.moda.compat.MainCompatHandler;
import miguknamja.moda.network.PacketHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class CommonProxy {

    // Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "moda.cfg"));
        Config.readConfig();

        // Initialize our packet handler. Make sure the name is
        // 20 characters or less!
        PacketHandler.registerMessages(ModA.MODID);

        // Initialization of blocks and items typically goes here:
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        //ModDimensions.init();

        MainCompatHandler.registerWaila();
        //MainCompatHandler.registerTOP();

    }

    public void init(FMLInitializationEvent e) {
        //NetworkRegistry.INSTANCE.registerGuiHandler(ModA.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent e) {
        if (config.hasChanged()) {
            config.save();
        }
    }
}
