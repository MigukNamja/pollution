package miguknamja.pollution.items;

import miguknamja.pollution.Pollution;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PollutionItemBase extends Item {
	
    public PollutionItemBase(String name) {
        setUnlocalizedName(Pollution.MODID+"."+name);
        setRegistryName(name);
        //setCreativeTab(Pollution.tabPollution);
        GameRegistry.register(this);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
    
    public abstract void initCrafting();
}
