package miguknamja.pollution.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

	public static PollutionManual pollutionManual;
	public static PollutionProbe pollutionProbe;
	public static HandFan handFan;


    public static void init() {
    	//pollutionManual = new PollutionManual();
    	pollutionProbe = new PollutionProbe();
    	handFan = new HandFan();
    	
        //pollutionManual.initCrafting();
        pollutionProbe.initCrafting();
        handFan.initCrafting();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    	//pollutionManual.initModel();
    	pollutionProbe.initModel();
    	handFan.initModel();
    }

}
