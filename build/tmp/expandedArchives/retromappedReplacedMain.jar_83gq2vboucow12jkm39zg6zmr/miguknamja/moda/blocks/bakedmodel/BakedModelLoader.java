package miguknamja.moda.blocks.bakedmodel;

import miguknamja.moda.ModA;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BakedModelLoader implements ICustomModelLoader {

    public static final ExampleModel EXAMPLE_MODEL = new ExampleModel();


    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.func_110624_b().equals(ModA.MODID) && "bakedmodelblock".equals(modelLocation.func_110623_a());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return EXAMPLE_MODEL;
    }

    @Override
    public void func_110549_a(IResourceManager resourceManager) {

    }
}
