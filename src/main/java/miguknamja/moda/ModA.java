package miguknamja.moda;

import org.apache.logging.log4j.Logger;

import miguknamja.moda.proxy.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = ModA.MODID, name = ModA.MODNAME, version = ModA.VERSION, dependencies = "required-after:Forge@[12.18.1.2011,)", useMetadata = true)
public class ModA
{
    public static final String MODID = "moda";
    public static final String MODNAME = "Mod A";
    public static final String VERSION = "0.0.3";
    
    @SidedProxy(clientSide = "miguknamja.moda.proxy.ClientProxy", serverSide = "miguknamja.moda.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModA instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
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
