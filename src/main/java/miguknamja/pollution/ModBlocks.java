package miguknamja.pollution;

import miguknamja.pollution.blocks.PolluterBlock;
import miguknamja.pollution.blocks.bakedmodel.BakedModelBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    public static PolluterBlock polluterBlock;
    public static BakedModelBlock bakedModelBlock;
    //public static DataBlock dataBlock;
    //public static BlinkingBlock blinkingBlock;
    //public static PedestalBlock pedestalBlock;
    //public static TestContainerBlock testContainerBlock;

    public static void init() {
        polluterBlock = new PolluterBlock();
        bakedModelBlock = new BakedModelBlock();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    	polluterBlock.initModel();
        bakedModelBlock.initModel();
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        bakedModelBlock.initItemModel();
    }
}
