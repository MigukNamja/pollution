package miguknamja.pollution.proxy;

import miguknamja.pollution.DimensionTickEvent;
import miguknamja.pollution.pollutersdb.PollutersDB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ServerProxy extends CommonProxy {
	
    @Override
    public void init(FMLInitializationEvent e) {
    	// TODO : Read these from a config or .json file
    	PollutersDB.registerPolluter("tile.furnace"); 
		PollutersDB.registerPolluter("tile.pollution.polluterblock");
    	MinecraftForge.EVENT_BUS.register(new DimensionTickEvent());
    	System.out.println( "CommonProxy.init" );
    }
}
