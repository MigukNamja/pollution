package miguknamja.moda.proxy;

import miguknamja.moda.ModA;
import miguknamja.moda.ModBlocks;
import miguknamja.moda.ModEntities;
import miguknamja.moda.ModItems;
import miguknamja.moda.blocks.bakedmodel.BakedModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        OBJLoader.INSTANCE.addDomain(ModA.MODID);
        ModelLoaderRegistry.registerLoader(new BakedModelLoader());

        // Typically initialization of models and such goes here:
        ModBlocks.initModels();
        ModItems.initModels();
        ModEntities.initModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);

        // Initialize our input handler so we can listen to keys
        //MinecraftForge.EVENT_BUS.register(new InputHandler());
        //KeyBindings.init();

        ModBlocks.initItemModels();
    }
}
