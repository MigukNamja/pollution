package miguknamja.pollution.effects;

import java.nio.FloatBuffer;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import miguknamja.pollution.Config;
import miguknamja.pollution.data.ClientData;
import miguknamja.pollution.data.PollutersDB;
import miguknamja.pollution.data.PollutersPerChunk;
import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import miguknamja.utils.ChunkKey;
import miguknamja.utils.Color;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PollutionEffects {
	/*
	 * Apply the effects of pollution
	 */
	public static void apply( World world, Chunk chunk ) {
		if( world.isRemote ){ return; } // don't run on the client
				
		if( !chunk.isLoaded() ){ return; } // Never update a chunk that's not loaded
		
		PollutionDataValue pdv = PollutionWorldData.getPollution(world, chunk); // pollution in this chunk
		
		if( belowIgnoreThreshold(pdv) ){ return; } // Bail early if pollution level is low enough to be ignored

		ClassInheritanceMultiMap<Entity>[] entClasses = chunk.getEntityLists();
		for( ClassInheritanceMultiMap<Entity> entClass : entClasses ) {
			Iterable<EntityLivingBase> elbs = entClass.getByClass(EntityLivingBase.class);
			for( EntityLivingBase elb : elbs ) {
				/*
				if( EntityPlayer.class.isInstance(elb) ) {
					EntityPlayer player = (EntityPlayer)elb;
					player.addChatComponentMessage(new TextComponentString(TextFormatting.YELLOW + "pollution has a price..."));
				}
				*/
				addPotionEffects( elb, pdv );
			}
		}
	}
	
	/*
	 * Calls apply() for every chunk in this world
	 */
	public static void apply( World world ) {
		if( world.isRemote ){ return; } // don't run on the client

		// Iterate over all chunks we know we have polluters in
		for( Map.Entry<ChunkKey,PollutersPerChunk> entry : PollutersDB.allPolluters.entrySet() ) {
			ChunkKey chunkKey = entry.getKey();
			apply( world, world.getChunkFromChunkCoords(chunkKey.xPosition, chunkKey.zPosition) );
		}		
		//Logging.log( "PollutionEffects.apply()" );
	}
	
	public static boolean belowIgnoreThreshold( PollutionDataValue pdv ) {
		return pdv.pollutionLevel < ( Config.minimumPollutionThreshold * Config.maxPollutionLevel );
	}
	
	private static Potion potion( PotionEffects effect ){ return Potion.getPotionById( effect.getValue() ); }

	// TODO : Add these potion effects and their threshold to a .json file
	private static void addPotionEffects( EntityLivingBase elb, PollutionDataValue pdv ) {
		if( !Config.doPotionEffects || belowIgnoreThreshold( pdv ) ) { return; }
		
		double percentOfMax = 100.0 * pdv.pollutionLevel / Config.maxPollutionLevel;
		int defaultStrength = Config.defaultPotionStrength;
		int defaultDuration = Config.defaultPotionDuration;

		if( percentOfMax > 90.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.wither ),   // effect
					defaultDuration * 20,             // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}
		
		if( percentOfMax > 70.0 ) {
			int ticks = (int)(Math.floor(defaultDuration / 3.0) * 20.0);
			if( percentOfMax > 80.0 ) {
				ticks = defaultDuration * 20;
				elb.addPotionEffect(new PotionEffect(
						potion( PotionEffects.poison ),   // effect
						ticks,                            // ticks
						defaultStrength,                  // strength
						false,                            // always false on server side
						true));			                  // show particle effects
			}
		}
		
		if( percentOfMax > 50.0 ) {
			int ticks = (int)(Math.floor(defaultDuration / 3.0) * 20.0);
			if( percentOfMax > 60.0 ) {
				ticks = defaultDuration * 20;
				elb.addPotionEffect(new PotionEffect(
						potion( PotionEffects.hunger ),   // effect
						ticks,                            // ticks
						defaultStrength,                  // strength
						false,                            // always false on server side
						true));			                  // show particle effects
			}
		}
		
		if( percentOfMax > 40.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.nausea ), // effect
					defaultDuration * 20,             // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}

		if( percentOfMax > 30.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.weakness ), // effect
					defaultDuration * 20,             // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}

		if( percentOfMax > 20.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.mining_fatigue ), // effect
					defaultDuration * 20,             // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}

		if( percentOfMax > 10.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.slowness ), // effect
					defaultDuration * 20,             // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}
	}
	
	public static Color getFogColor( PollutionDataValue pdv ) {
		int r = Config.defaultSmogColor_R;
		int g = Config.defaultSmogColor_G;
		int b = Config.defaultSmogColor_B;
		return( new Color( r, g, b ) );
	}
	
	public static float getFogDensity( PollutionDataValue pdv ) {
		if( !Config.doSmogEffect || belowIgnoreThreshold( pdv ) ) {
			return 0f;
		} else {
			float maxFogDensity = Config.defaultSmogDensity;
			double p = pdv.percent() / 100.0;
			return (float)(Math.pow(p, 2) * maxFogDensity); // from 0.0 to maxFogDensity, exponential
		}
	}
	
	/*
	 * Runs on client side only !
	 */
	public static void clientFog() {		
		PollutionDataValue pdv = ClientData.pdv;
		
		Color color = PollutionEffects.getFogColor( pdv );
		Float density = slewDensity( PollutionEffects.getFogDensity( pdv ) );
        final FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
        {
            fogColours.put(new float[]{color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, density});
            fogColours.flip();
        }

		GL11.glDisable( GL11.GL_FOG );
		GL11.glFogi(GL11.GL_FOG_MODE, glFogMode());
        GL11.glFog(GL11.GL_FOG_COLOR, fogColours );
		GL11.glFogf(GL11.GL_FOG_DENSITY, density);
        GL11.glHint( GL11.GL_FOG_HINT, GL11.GL_FASTEST );
        GL11.glEnable( GL11.GL_FOG );        
	}
	
	private static int glFogMode() {
		switch( Config.defaultSmogGL11Mode ){
		case "GL_LINEAR": return GL11.GL_LINEAR;
		case "GL_EXP": return GL11.GL_EXP;
		case "GL_EXP2": // fall through for default
		default: return GL11.GL_EXP2;
		}
	}

	/*
	 * Slowly adjust (slew) the fog density so the player doesn't experience a sudden change in pollution levels
	 */
	private static float curDensity = 0.0f;
	private static float targetDensity = 0.0f;
	private static float slewDensity( float density ) {		
		
		final float step = 0.001f; // how quickly to adjust
		
		//Logging.log( "density("+density+") prev("+prevDensity+") cur("+curDensity+") target("+targetDensity+")" );
		
		if( density != targetDensity ) { // update
			targetDensity = density;
		}

		float leftToSlew = targetDensity - curDensity;
		if( Math.abs(leftToSlew) <= step ) { // if close enough, snap to the target
			targetDensity = density;
			curDensity = density;
		} else { // otherwise, slowly adjust to the new density value
			curDensity += (step * leftToSlew);
		} 
		
		return curDensity;
	}
}
