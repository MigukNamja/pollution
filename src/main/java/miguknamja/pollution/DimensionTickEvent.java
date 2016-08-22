package miguknamja.pollution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


public class DimensionTickEvent {
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent evt) {
    	serverTick(false);
        if (evt.phase == TickEvent.Phase.START) {
            return;
        }
       
    }
    
    
    private void serverTick(boolean doEffects) {
		System.out.println("SERVERTICK being called");	
		// loop chunks in hash here?
		
        // save pollution here?
    }
}
