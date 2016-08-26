package miguknamja.pollution.events;
import miguknamja.pollution.Config;
import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.effects.PollutionEffects;
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

	/*
	@SubscribeEvent(priority=EventPriority.NORMAL)
    public void onServerTick( ServerTickEvent event ) {
		System.out.println("ServerEventHandler.onServerTick( ServerTickEvent )");	
        if (event.phase == TickEvent.Phase.START) {
        	serverTick(false);
        }
    }
    */
    
    private void worldTick( WorldTickEvent event ) {
		//Logging.log("ServerEventHandler.onServerTick( WorldTickEvent )" + ticks);	
		
		if( ticks % Config.ticksPerPolluterTileEntityScan == 0 ) {
			PollutersDB.scan( event.world );
		}
		
		if( ticks % Config.ticksPerPollutionUpdate == 0 ) {
			PollutionEffects.apply( event.world );
			
			// Send pollution data to the clients now
			PollutersDB.updatePlayers( event.world );
		}

		ticks++;
    }
}
