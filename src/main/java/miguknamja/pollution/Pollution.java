package miguknamja.pollution;

import org.apache.logging.log4j.Logger;

import miguknamja.pollution.commands.PollutionCommand;
import miguknamja.pollution.data.PollutionWorldData;
import miguknamja.pollution.effects.PollutionEffects;
import miguknamja.pollution.proxy.CommonProxy;
import miguknamja.utils.Logging;
import miguknamja.utils.ModBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = Pollution.MODID, name = Pollution.MODNAME, version = Pollution.VERSION, dependencies = "required-after:Forge@["+Pollution.MIN_FORGE_VER+",)", useMetadata = true)
public class Pollution implements ModBase
{
    public static final String MODID = "pollution";
    public static final String MODNAME = "Pollution";
    public static final String MIN_FORGE_VER = "12.18.1.2011";
    public static final String VERSION = "0.1.0";

    public static void doPollution( World world ) {
		// TODO : move all the code below into Pollution.java and called it something like 'mainLoop'
		
		// Generate pollution and do primary spreading
		PollutionGeneration.generateFromPolluters( world );
		
		// Take into account weather and time of day on pollution
		//PollutionWeather.applyWeather( world );

		// Passively Dissipate (reduce) pollution
		PollutionDissipation.dissipate( world );
		
		// Apply effects
		PollutionEffects.apply( world );
		
		// Send pollution data to the clients
		PollutionWorldData.updatePlayers( world );
    }
    
    @SidedProxy(clientSide = "miguknamja.pollution.proxy.ClientProxy", serverSide = "miguknamja.pollution.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static Pollution instance;
    
    public static Logger logger;
    
    @Override
    public String getModId() {
        return MODID;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
    	Logging.log( "Pollution: server is loading" );
        event.registerServerCommand(new PollutionCommand());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        Logging.log( "Pollution: server is starting" );
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
    	Logging.log( "Pollution: Server is stopping. Shutting down gracefully." );
    }

    /*
    public static CreativeTabs tabPollution = new CreativeTabs("Pollution") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return ModItems.pollutionManual;
        }
    };
    */
    
    @Override
    public void openManual(EntityPlayer player, int bookIndex, String page) {
    	/*
        GuiPollutionManual.locatePage = page;
        player.openGui(Pollution.instance, bookIndex, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        */
    }    
}
