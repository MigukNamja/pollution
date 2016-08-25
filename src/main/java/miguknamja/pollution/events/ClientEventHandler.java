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
		//event.setRed( color.r );
		//event.setGreen( color.g );
		//event.setBlue( color.b );
		PollutionEffects.clientFog();
		//System.out.println( "FogHandler.onFogColors(" + PollutionEffects.getFogDensity( pdv ) + ")" );
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onFogDensity(FogDensity event) {
		/*
		PollutionDataValue pdv = ClientData.pdv;
		if( PollutionEffects.belowIgnoreThreshold( pdv ) ) {
			event.setDensity( 0f );
		} else {
			event.setDensity( PollutionEffects.getFogDensity( pdv ) );
		}
		*/
		PollutionEffects.clientFog();
		event.setCanceled(true);
		//System.out.println( "FogHandler.onFogDensity(" + PollutionEffects.getFogDensity( pdv ) + ")" );
	}
}
