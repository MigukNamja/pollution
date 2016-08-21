package miguknamja.moda.blocks.bakedmodel;

import com.google.common.base.Function;

import miguknamja.moda.ModA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExampleBakedModel implements IBakedModel {

    public static final ModelResourceLocation BAKED_MODEL = new ModelResourceLocation(ModA.MODID + ":bakedmodelblock");

    private TextureAtlasSprite sprite;
    private VertexFormat format;

    public ExampleBakedModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.format = format;
        sprite = bakedTextureGetter.apply(new ResourceLocation(ModA.MODID, "blocks/isbmtexture"));
    }


    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v) {
        for (int e = 0; e < format.func_177345_h(); e++) {
            switch (format.func_177348_c(e).func_177375_c()) {
                case POSITION:
                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.func_177348_c(e).func_177369_e() == 0) {
                        u = sprite.func_94214_a(u);
                        v = sprite.func_94207_b(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.field_72450_a, (float) normal.field_72448_b, (float) normal.field_72449_c, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v1.func_178788_d(v2).func_72431_c(v3.func_178788_d(v2));

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        putVertex(builder, normal, v1.field_72450_a, v1.field_72448_b, v1.field_72449_c, 0, 0);
        putVertex(builder, normal, v2.field_72450_a, v2.field_72448_b, v2.field_72449_c, 0, 16);
        putVertex(builder, normal, v3.field_72450_a, v3.field_72448_b, v3.field_72449_c, 16, 16);
        putVertex(builder, normal, v4.field_72450_a, v4.field_72448_b, v4.field_72449_c, 16, 0);
        return builder.build();
    }

    @Override
    public List<BakedQuad> func_188616_a(IBlockState state, EnumFacing side, long rand) {

        if (side != null) {
            return Collections.emptyList();
        }


        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        Boolean north = extendedBlockState.getValue(BakedModelBlock.NORTH);
        Boolean south = extendedBlockState.getValue(BakedModelBlock.SOUTH);
        Boolean west = extendedBlockState.getValue(BakedModelBlock.WEST);
        Boolean east = extendedBlockState.getValue(BakedModelBlock.EAST);
        Boolean up = extendedBlockState.getValue(BakedModelBlock.UP);
        Boolean down = extendedBlockState.getValue(BakedModelBlock.DOWN);
        List<BakedQuad> quads = new ArrayList<BakedQuad>();
        double o = .4;

        // For each side we either cap it off if there is no similar block adjacent on that side
        // or else we extend so that we touch the adjacent block:

        if (up) {
            quads.add(createQuad(new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, 1, o), new Vec3d(1 - o, 1, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), sprite));
            quads.add(createQuad(new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, 1, 1 - o), new Vec3d(o, 1, o), new Vec3d(o, 1 - o, o), sprite));
            quads.add(createQuad(new Vec3d(o, 1, o), new Vec3d(1 - o, 1, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(o, 1 - o, o), sprite));
            quads.add(createQuad(new Vec3d(o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, 1, 1 - o), new Vec3d(o, 1, 1 - o), sprite));
        } else {
            quads.add(createQuad(new Vec3d(o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, o), new Vec3d(o, 1 - o, o), sprite));
        }

        if (down) {
            quads.add(createQuad(new Vec3d(1 - o, 0, o), new Vec3d(1 - o, o, o), new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, 0, 1 - o), sprite));
            quads.add(createQuad(new Vec3d(o, 0, 1 - o), new Vec3d(o, o, 1 - o), new Vec3d(o, o, o), new Vec3d(o, 0, o), sprite));
            quads.add(createQuad(new Vec3d(o, o, o), new Vec3d(1 - o, o, o), new Vec3d(1 - o, 0, o), new Vec3d(o, 0, o), sprite));
            quads.add(createQuad(new Vec3d(o, 0, 1 - o), new Vec3d(1 - o, 0, 1 - o), new Vec3d(1 - o, o, 1 - o), new Vec3d(o, o, 1 - o), sprite));
        } else {
            quads.add(createQuad(new Vec3d(o, o, o), new Vec3d(1 - o, o, o), new Vec3d(1 - o, o, 1 - o), new Vec3d(o, o, 1 - o), sprite));
        }

        if (east) {
            quads.add(createQuad(new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1, 1 - o, 1 - o), new Vec3d(1, 1 - o, o), new Vec3d(1 - o, 1 - o, o), sprite));
            quads.add(createQuad(new Vec3d(1 - o, o, o), new Vec3d(1, o, o), new Vec3d(1, o, 1 - o), new Vec3d(1 - o, o, 1 - o), sprite));
            quads.add(createQuad(new Vec3d(1 - o, 1 - o, o), new Vec3d(1, 1 - o, o), new Vec3d(1, o, o), new Vec3d(1 - o, o, o), sprite));
            quads.add(createQuad(new Vec3d(1 - o, o, 1 - o), new Vec3d(1, o, 1 - o), new Vec3d(1, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), sprite));
        } else {
            quads.add(createQuad(new Vec3d(1 - o, o, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, o, 1 - o), sprite));
        }

        if (west) {
            quads.add(createQuad(new Vec3d(0, 1 - o, 1 - o), new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, 1 - o, o), new Vec3d(0, 1 - o, o), sprite));
            quads.add(createQuad(new Vec3d(0, o, o), new Vec3d(o, o, o), new Vec3d(o, o, 1 - o), new Vec3d(0, o, 1 - o), sprite));
            quads.add(createQuad(new Vec3d(0, 1 - o, o), new Vec3d(o, 1 - o, o), new Vec3d(o, o, o), new Vec3d(0, o, o), sprite));
            quads.add(createQuad(new Vec3d(0, o, 1 - o), new Vec3d(o, o, 1 - o), new Vec3d(o, 1 - o, 1 - o), new Vec3d(0, 1 - o, 1 - o), sprite));
        } else {
            quads.add(createQuad(new Vec3d(o, o, 1 - o), new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, 1 - o, o), new Vec3d(o, o, o), sprite));
        }

        if (north) {
            quads.add(createQuad(new Vec3d(o, 1 - o, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, 1 - o, 0), new Vec3d(o, 1 - o, 0), sprite));
            quads.add(createQuad(new Vec3d(o, o, 0), new Vec3d(1 - o, o, 0), new Vec3d(1 - o, o, o), new Vec3d(o, o, o), sprite));
            quads.add(createQuad(new Vec3d(1 - o, o, 0), new Vec3d(1 - o, 1 - o, 0), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, o, o), sprite));
            quads.add(createQuad(new Vec3d(o, o, o), new Vec3d(o, 1 - o, o), new Vec3d(o, 1 - o, 0), new Vec3d(o, o, 0), sprite));
        } else {
            quads.add(createQuad(new Vec3d(o, 1 - o, o), new Vec3d(1 - o, 1 - o, o), new Vec3d(1 - o, o, o), new Vec3d(o, o, o), sprite));
        }
        if (south) {
            quads.add(createQuad(new Vec3d(o, 1 - o, 1), new Vec3d(1 - o, 1 - o, 1), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(o, 1 - o, 1 - o), sprite));
            quads.add(createQuad(new Vec3d(o, o, 1 - o), new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, o, 1), new Vec3d(o, o, 1), sprite));
            quads.add(createQuad(new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(1 - o, 1 - o, 1), new Vec3d(1 - o, o, 1), sprite));
            quads.add(createQuad(new Vec3d(o, o, 1), new Vec3d(o, 1 - o, 1), new Vec3d(o, 1 - o, 1 - o), new Vec3d(o, o, 1 - o), sprite));
        } else {
            quads.add(createQuad(new Vec3d(o, o, 1 - o), new Vec3d(1 - o, o, 1 - o), new Vec3d(1 - o, 1 - o, 1 - o), new Vec3d(o, 1 - o, 1 - o), sprite));
        }

        return quads;
    }

    @Override
    public ItemOverrideList func_188617_f() {
        return null;
    }

    @Override
    public boolean func_177555_b() {
        return false;
    }

    @Override
    public boolean func_177556_c() {
        return false;
    }

    @Override
    public boolean func_188618_c() {
        return false;
    }

    @Override
    public TextureAtlasSprite func_177554_e() {
        return sprite;
    }

    @Override
    public ItemCameraTransforms func_177552_f() {
        return ItemCameraTransforms.field_178357_a;
    }
}
