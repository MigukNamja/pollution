package miguknamja.pollution;

import org.apache.logging.log4j.Level;

import miguknamja.pollution.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String CATEGORY_GENERAL = "general";
    public static int ticksPerPolluterTileEntityScan = 100;
    public static int ticksPerPollutionUpdate = 20;
    
    private static final String CATEGORY_POLLUTION_LEVELS = "pollution levels";    
	public static double minPollutionLevel = 0.0;
	public static double maxPollutionLevel = 2000.0;
	
    private static final String CATEGORY_PLAYER_POLLUTION_EFFECTS = "player pollution effects";    
    public static boolean doPotionEffects = true;
    public static boolean doSmogEffect = true;
    public static double minimumPollutionThreshold = 0.1;
	public static int defaultPotionStrength = 3;
	public static int defaultPotionDuration = 5;
	public static int defaultSmogColor_R = 200;
	public static int defaultSmogColor_G = 120;
	public static int defaultSmogColor_B = 72;	
	public static float defaultSmogDensity = 0.4f; // change to 0.6f if changing GL11Mode to GL_EXP or GL_LINEAR
	public static String defaultSmogGL11Mode = "GL_EXP2";
    
    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
            //initOtherConfig(cfg);
        } catch (Exception e1) {
            Pollution.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        ticksPerPolluterTileEntityScan = cfg.getInt("ticksPerPolluterTileEntityScan", CATEGORY_GENERAL, ticksPerPolluterTileEntityScan, 20, ticksPerPolluterTileEntityScan*10, "Ticks per new polluter tile entity scan");
        ticksPerPollutionUpdate = cfg.getInt("ticksPerPollutionUpdate", CATEGORY_GENERAL, ticksPerPollutionUpdate, 1, ticksPerPollutionUpdate*10, "Ticks per new pollution measuring and application scan");
        
        minPollutionLevel = (double)cfg.getFloat("minPollutionLevel", CATEGORY_POLLUTION_LEVELS, (float)minPollutionLevel, 0f, (float)maxPollutionLevel/10f, "Minimum absolute pollution level. Recommend this stays at 0");
        maxPollutionLevel = (double)cfg.getFloat("maxPollutionLevel", CATEGORY_POLLUTION_LEVELS, (float)maxPollutionLevel, 0f, (float)maxPollutionLevel*10, "Maximum absolute pollution level. Recommend this stays at " + maxPollutionLevel);

        doPotionEffects = cfg.getBoolean("doPotionEffects", CATEGORY_PLAYER_POLLUTION_EFFECTS, doPotionEffects, "true to enable player potion effects, false to disable");
        doSmogEffect = cfg.getBoolean("doFogEffects", CATEGORY_PLAYER_POLLUTION_EFFECTS, doSmogEffect, "true to enable smog, false to disable");
        minimumPollutionThreshold = (double)cfg.getFloat("minimumPollutionThreshold", CATEGORY_POLLUTION_LEVELS, (float)minimumPollutionThreshold, 0f, 100f, "Threshold percentage above which pollution effects are applied. 0.0 to always apply. 100.0 to disable.");
        defaultPotionStrength = cfg.getInt("defaultPotionStrength", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultPotionStrength, 1, 5, "Default player effect potion strength (amplifier) for negative effects like poison");
        defaultPotionDuration = cfg.getInt("defaultPotionDuration", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultPotionDuration, 1, 60, "Default player effect potion duration in seconds");
        defaultSmogColor_R = cfg.getInt("defaultSmogColor_R", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultSmogColor_R, 0, 255, "Smog color R value. Default color is light brown.");
        defaultSmogColor_G = cfg.getInt("defaultSmogColor_G", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultSmogColor_G, 0, 255, "Smog color G value. Default color is light brown.");
        defaultSmogColor_B = cfg.getInt("defaultSmogColor_B", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultSmogColor_B, 0, 255, "Smog color B value. Default color is light brown.");
        defaultSmogDensity = cfg.getFloat("defaultSmogDensity", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultSmogDensity, 0f, 1f, "Smog density to pass to GL11.glFogf(GL11.GL_FOG_DENSITY, density)");
        defaultSmogGL11Mode = cfg.getString("defaultSmogGL11Mode", CATEGORY_PLAYER_POLLUTION_EFFECTS, defaultSmogGL11Mode, "OpenGL GL11 fog mode for smog. Valid values are GL_LINEAR, GL_EXP, and GL_EXP2.");
    }

    
    /*
    private static void initOtherConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_DIMENSIONS, "Other configuration");
    }
    */
}
