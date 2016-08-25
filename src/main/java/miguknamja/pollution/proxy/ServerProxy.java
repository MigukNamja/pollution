package miguknamja.pollution.proxy;

/*
 * This class executes on dedicated servers only.
 * It does *not* execute in single-player !!
 * 
 * Only put code in here which should *not* be running in single-player mode
 * and only running on a server.
 */

import miguknamja.utils.Logging;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy {
	
    @Override
    public void preInit(FMLPreInitializationEvent e) {    	
    	Logging.log( "ServerProxy.preInit()" );
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {    	
    	Logging.log( "ServerProxy.init()" );
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {    	
    	Logging.log( "ServerProxy.postInit()" );
        super.postInit(e);
    }
}
