package miguknamja.pollution.events;

import miguknamja.pollution.effects.PollutionEffects;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler extends CommonEventHandler {

	
	public ClientEventHandler() {
		super();
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onFogColors(FogColors event) {
		PollutionEffects.setFogColor( event );

		//PollutionEffects.clientFog();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onFogDensity(FogDensity event) {
		PollutionEffects.setFogDensity( event );
	}
}
