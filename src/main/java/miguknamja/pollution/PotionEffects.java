package miguknamja.pollution;

/*
 * Copied from net.minecraft.potion.Potion
 * 
 * For the life of me, I cannot figure out why Mojang didn't use constants for magic numbers like these.
 * 
 * REGISTRY.register(2, new ResourceLocation("slowness"), (new Potion(true, 5926017)).setPotionName("effect.moveSlowdown").setIconIndex(1, 0).registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2));
 * REGISTRY.register(4, new ResourceLocation("mining_fatigue"), (new Potion(true, 4866583)).setPotionName("effect.digSlowDown").setIconIndex(3, 0).registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "55FCED67-E92A-486E-9800-B47F202C4386", -0.10000000149011612D, 2));
 * REGISTRY.register(7, new ResourceLocation("instant_damage"), (new PotionHealth(true, 4393481)).setPotionName("effect.harm").setBeneficial());
 * REGISTRY.register(9, new ResourceLocation("nausea"), (new Potion(true, 5578058)).setPotionName("effect.confusion").setIconIndex(3, 1).setEffectiveness(0.25D));
 * REGISTRY.register(15, new ResourceLocation("blindness"), (new Potion(true, 2039587)).setPotionName("effect.blindness").setIconIndex(5, 1).setEffectiveness(0.25D));
 * REGISTRY.register(17, new ResourceLocation("hunger"), (new Potion(true, 5797459)).setPotionName("effect.hunger").setIconIndex(1, 1));
 * REGISTRY.register(18, new ResourceLocation("weakness"), (new PotionAttackDamage(true, 4738376, -4.0D)).setPotionName("effect.weakness").setIconIndex(5, 0).registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", 0.0D, 0));
 * REGISTRY.register(19, new ResourceLocation("poison"), (new Potion(true, 5149489)).setPotionName("effect.poison").setIconIndex(6, 0).setEffectiveness(0.25D));
 * REGISTRY.register(20, new ResourceLocation("wither"), (new Potion(true, 3484199)).setPotionName("effect.wither").setIconIndex(1, 2).setEffectiveness(0.25D));
 * REGISTRY.register(24, new ResourceLocation("glowing"), (new Potion(false, 9740385)).setPotionName("effect.glowing").setIconIndex(4, 2));
 * REGISTRY.register(27, new ResourceLocation("unluck"), (new Potion(true, 12624973)).setPotionName("effect.unluck").setIconIndex(6, 2).registerPotionAttributeModifier(SharedMonsterAttributes.LUCK, "CC5AF142-2BD2-4215-B636-2605AED11727", -1.0D, 0));
 */
public enum PotionEffects {
	slowness(2),
	mining_fatigue(4),
	instant_damage(7),
	nausea(9),
	blindness(15),
	hunger(17),
	weakness(18),
	poison(19),
	wither(20),
	glowing(24),
	unluck(27);
		
	private int value;
	private PotionEffects(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
};
