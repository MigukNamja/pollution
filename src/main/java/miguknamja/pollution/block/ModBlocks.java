package miguknamja.pollution.block;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    public static PolluterAdminBlock polluterAdminBlock;

    public static void init() {
    	polluterAdminBlock = new PolluterAdminBlock();

    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    	polluterAdminBlock.initModel();

    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
    }
}
