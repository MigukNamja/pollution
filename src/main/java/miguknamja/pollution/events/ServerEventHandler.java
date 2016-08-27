package miguknamja.pollution.events;
import miguknamja.pollution.Config;
import miguknamja.pollution.Pollution;
import miguknamja.pollution.data.PollutersDB;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ServerEventHandler extends CommonEventHandler {
	
	private long ticks;
	public ServerEventHandler() {
		ticks = 0;
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
    public void onWorldTick( WorldTickEvent event ) {
        if (event.phase == TickEvent.Phase.START) {
        	worldTick( event );
        }
    }
    
    private void worldTick( WorldTickEvent event ) {
		//Logging.log("ServerEventHandler.onServerTick( WorldTickEvent )" + ticks);	
		
		if( ticks % Config.ticksPerPolluterTileEntityScan == 0 ) {
			PollutersDB.scan( event.world );
		}
		
		if( ticks % Config.ticksPerPollutionUpdate == 0 ) {
			Pollution.doPollution( event.world );
		}

		ticks++;
    }
}
