package com.untt.icb.client.model.baked;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.untt.icb.block.BlockConveyor;
import com.untt.icb.utility.LogHelper;
import com.untt.icb.utility.ResourceHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;
import java.util.function.Function;

public class BakedModelConveyor implements IPerspectiveAwareModel
{
    private static IModel modelConveyorFlat;
    private static IModel modelConveyorSlopedUp;
    private static IModel modelConveyorSlopedDown;
    private static IModel modelConveyorTurnLeft;
    private static IModel modelConveyorTurnRight;

    private IPerspectiveAwareModel modelDefault;

    private final Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    protected final VertexFormat format;
    protected final Map<Map<ArrayList<Boolean>, EnumFacing>, IBakedModel> cache = new HashMap<>();

    public BakedModelConveyor(IPerspectiveAwareModel modelDefault, VertexFormat format)
    {
        try
        {
            modelConveyorFlat = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/conveyor"));
            modelConveyorSlopedUp = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/conveyor_sloped_up"));
            modelConveyorSlopedDown = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/conveyor_sloped_down"));
            modelConveyorTurnLeft = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/conveyor_turn"));
            modelConveyorTurnRight = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/conveyor_turn_reverse"));
        }

        catch (Exception e)
        {
            LogHelper.error(e);
        }

        this.modelDefault = modelDefault;

        this.textureGetter = resourceLocation -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(resourceLocation.toString());
        this.format = format;
    }

    private IBakedModel getActualModel(ArrayList<Boolean> state, EnumFacing facing)
    {
        IBakedModel bakedModel;

        Map<ArrayList<Boolean>, EnumFacing> cacheKey = Maps.newHashMap();
        cacheKey.put(state, facing);

        if (cache.containsKey(cacheKey))
            bakedModel = cache.get(cacheKey);

        else
        {
            IModel modelConveyor = modelConveyorFlat;

            if (state.get(0))
                modelConveyor = modelConveyorSlopedUp;

            if (state.get(1))
                modelConveyor = modelConveyorSlopedDown;

            if (state.get(2))
                modelConveyor = modelConveyorTurnLeft;

            if (state.get(3))
            {
                modelConveyor = modelConveyorTurnRight;
                facing = facing.rotateY();
            }

            bakedModel = modelConveyor.bake(new TRSRTransformation(facing), format, textureGetter::apply);

            cache.put(cacheKey, bakedModel);
        }

        return bakedModel;
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(@Nullable IBlockState blockState, @Nullable EnumFacing side, long rand)
    {
        ArrayList<Boolean> state = new ArrayList<>(Arrays.asList(false, false, false, false));
        EnumFacing facing = EnumFacing.NORTH;

        if (blockState instanceof IExtendedBlockState)
        {
            IExtendedBlockState extendedBlockState = (IExtendedBlockState) blockState;

            if (extendedBlockState.getUnlistedNames().contains(BlockConveyor.FACING))
                facing = extendedBlockState.getValue(BlockConveyor.FACING);

            if (extendedBlockState.getUnlistedNames().contains(BlockConveyor.SLOPE_UP))
                state.set(0, extendedBlockState.getValue(BlockConveyor.SLOPE_UP));

            if (extendedBlockState.getUnlistedNames().contains(BlockConveyor.SLOPE_DOWN))
                state.set(1, extendedBlockState.getValue(BlockConveyor.SLOPE_DOWN));

            if (extendedBlockState.getUnlistedNames().contains(BlockConveyor.TURN_LEFT))
                state.set(2, extendedBlockState.getValue(BlockConveyor.TURN_LEFT));

            if (extendedBlockState.getUnlistedNames().contains(BlockConveyor.TURN_RIGHT))
                state.set(3, extendedBlockState.getValue(BlockConveyor.TURN_RIGHT));
        }

        return getActualModel(state, facing).getQuads(blockState, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return modelDefault.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return modelDefault.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return modelDefault.isBuiltInRenderer();
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture()
    {
        return modelDefault.getParticleTexture();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return modelDefault.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ConveyorItemOverrideList.INSTANCE;
    }

    private static class ConveyorItemOverrideList extends ItemOverrideList
    {
        static ConveyorItemOverrideList INSTANCE = new ConveyorItemOverrideList();

        ConveyorItemOverrideList()
        {
            super(ImmutableList.of());
        }

        @Override
        @Nonnull
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            return originalModel;
        }
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        Pair<? extends IBakedModel, Matrix4f> pair = modelDefault.handlePerspective(cameraTransformType);

        return Pair.of(this, pair.getRight());
    }
}