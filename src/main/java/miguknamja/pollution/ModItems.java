package miguknamja.pollution;

import miguknamja.pollution.items.PollutionProbe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    //public static FirstItem firstItem;
    public static PollutionProbe pollutionProbe;
    //public static MultiModelItem multiModelItem;

    public static void init() {
        //firstItem = new FirstItem();
    	pollutionProbe = new PollutionProbe();
        //multiModelItem = new MultiModelItem();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    	pollutionProbe.initModel();
        //multiModelItem.initModel();
    }

}
