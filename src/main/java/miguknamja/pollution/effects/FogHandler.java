package miguknamja.pollution.effects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import miguknamja.pollution.data.ClientData;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.utils.Color;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FogHandler {

	
	public FogHandler() {
		super();
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onFogColors(FogColors event) {
		//event.setRed( color.r );
		//event.setGreen( color.g );
		//event.setBlue( color.b );
		doFog();
		//System.out.println( "FogHandler.onFogColors(" + PollutionEffects.getFogDensity( pdv ) + ")" );
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onFogDensity(FogDensity event) {
		//event.setDensity( PollutionEffects.getFogDensity( pdv ) );
		doFog();
		event.setCanceled(true);
		//System.out.println( "FogHandler.onFogDensity(" + PollutionEffects.getFogDensity( pdv ) + ")" );
	}
	
	@SideOnly(Side.CLIENT)
	private void doFog() {
		PollutionDataValue pdv = ClientData.pdv;
		if( PollutionEffects.belowIgnoreThreshold(pdv)) { GL11.glDisable( GL11.GL_FOG ); }

		Color color = PollutionEffects.getFogColor( pdv );
		Float density = PollutionEffects.getFogDensity( pdv );
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
        final FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
        {
            fogColours.put(new float[]{color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, density});
            fogColours.flip();
        }
        GL11.glFog(GL11.GL_FOG_COLOR, fogColours );
		GL11.glFogf(GL11.GL_FOG_DENSITY, density);
        GL11.glHint( GL11.GL_FOG_HINT, GL11.GL_NICEST );
        GL11.glEnable( GL11.GL_FOG );        
	}
}
