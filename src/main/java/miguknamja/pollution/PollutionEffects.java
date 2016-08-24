package miguknamja.pollution;

import miguknamja.pollution.data.PollutionDataValue;
import miguknamja.pollution.data.PollutionWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class PollutionEffects {
	/*
	 * Apply the effects of pollution
	 */
	public static void apply( World world, BlockPos chunkPos ) {
		if( world.isRemote ){ return; } // server only
				
		Chunk chunk = world.getChunkFromBlockCoords(chunkPos);

		if( !chunk.isLoaded() ){ return; } // Never update a chunk that's not loaded
		
		PollutionDataValue pdv = PollutionWorldData.getPollution(world, chunkPos); // pollution in this chunk
		
		if( belowIgnoreThreshold(pdv) ){ return; } // Bail early if pollution level is low enough to be ignored

		ClassInheritanceMultiMap<Entity>[] entClasses = chunk.getEntityLists();
		for( ClassInheritanceMultiMap<Entity> entClass : entClasses ) {
			Iterable<EntityLivingBase> elbs = entClass.getByClass(EntityLivingBase.class);
			for( EntityLivingBase elb : elbs ) {
				if( EntityPlayer.class.isInstance(elb) ) {
					EntityPlayer player = (EntityPlayer)elb;
					player.addChatComponentMessage(new TextComponentString(TextFormatting.YELLOW + "pollution has a price..."));
				}
				addPotionEffects( elb, pdv );
			}
		}
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
}
