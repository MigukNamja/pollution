package miguknamja.pollution.effects;

import java.nio.FloatBuffer;
import java.util.Map;

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

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

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
		return pdv.pollutionLevel < ( 0.1 * PollutionDataValue.maxPollutionLevel ); /* ignore anything below 10% of max for now */
	}
	
	private static Potion potion( PotionEffects effect ){ return Potion.getPotionById( effect.getValue() ); }
	
	private static void addPotionEffects( EntityLivingBase elb, PollutionDataValue pdv ) {
		if( belowIgnoreThreshold( pdv ) ) { return; }
		
		double percentOfMax = 100.0 * pdv.pollutionLevel / PollutionDataValue.maxPollutionLevel;
		int defaultStrength = 3;

		if( percentOfMax > 90.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.wither ),   // effect
					10 * 20,                          // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}
		
		if( percentOfMax > 70.0 ) {
			int ticks = 3 * 20;
			if( percentOfMax > 80.0 ) {
				ticks = 10 * 20;
				elb.addPotionEffect(new PotionEffect(
						potion( PotionEffects.poison ),   // effect
						ticks,                            // ticks
						defaultStrength,                  // strength
						false,                            // always false on server side
						true));			                  // show particle effects
			}
		}
		
		if( percentOfMax > 50.0 ) {
			int ticks = 3 * 20;
			if( percentOfMax > 60.0 ) {
				ticks = 10 * 20;
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
					10 * 20,                          // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}

		if( percentOfMax > 30.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.weakness ), // effect
					10 * 20,                          // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}

		if( percentOfMax > 20.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.mining_fatigue ), // effect
					10 * 20,                          // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}

		if( percentOfMax > 10.0 ) {
			elb.addPotionEffect(new PotionEffect(
					potion( PotionEffects.slowness ), // effect
					10 * 20,                          // ticks
					defaultStrength,                  // strength
					false,                            // always false on server side
					true));			                  // show particle effects			
		}
	}
	
	public static Color getFogColor( PollutionDataValue pdv ) {
		//double p = pdv.percent() / 100.0;
		return( new Color( 200, 120, 72 ) ); // medium brown/yellow color that scales with pollution
	}
	
	public static float getFogDensity( PollutionDataValue pdv ) {
		if( belowIgnoreThreshold( pdv )) {
			return 0f;
		} else {
			double p = pdv.percent() / 100.0;
			return (float)(Math.pow(p, 2) * 0.6f); // from 0.0 to 0.6, exponential
		}
	}

	/*
	 * Runs on client side only !
	 */
	public static void clientFog() {		
		PollutionDataValue pdv = ClientData.pdv;
		
		Color color = PollutionEffects.getFogColor( pdv );
		Float density = PollutionEffects.getFogDensity( pdv );
        final FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
        {
            fogColours.put(new float[]{color.r / 255.0f, color.g / 255.0f, color.b / 255.0f, density});
            fogColours.flip();
        }

		GL11.glDisable( GL11.GL_FOG );
		GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
        GL11.glFog(GL11.GL_FOG_COLOR, fogColours );
		GL11.glFogf(GL11.GL_FOG_DENSITY, density);
        GL11.glHint( GL11.GL_FOG_HINT, GL11.GL_FASTEST );
        GL11.glEnable( GL11.GL_FOG );        
	}
}
