package miguknamja.pollution.events;
import miguknamja.pollution.Config;
import miguknamja.pollution.PollutionGeneration;
import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.data.PollutionWorldData;
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
    
    private void worldTick( WorldTickEvent event ) {
		//Logging.log("ServerEventHandler.onServerTick( WorldTickEvent )" + ticks);	
		
		if( ticks % Config.ticksPerPolluterTileEntityScan == 0 ) {
			PollutersDB.scan( event.world );
		}
		
		if( ticks % Config.ticksPerPollutionUpdate == 0 ) {
			// Generate pollution and do primary spreading
			PollutionGeneration.generateFromPolluters( event.world );
			
			// Apply effects
			PollutionEffects.apply( event.world );
			
			// Send pollution data to the clients
			PollutionWorldData.updatePlayers( event.world );
		}

		ticks++;
    }
}
