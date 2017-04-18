package com.untt.icb.event;

import com.untt.icb.client.model.baked.BakedModelConveyor;
import com.untt.icb.reference.Names;
import com.untt.icb.utility.LogHelper;
import com.untt.icb.utility.ResourceHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerModelConveyorReplace
{
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        replaceBlockModel(Names.Blocks.CONVEYOR, "normal", event);
        replaceBlockModel(Names.Blocks.CONVEYOR, "inventory", event);
    }

    private static void replaceBlockModel(String blockName, String variant, ModelBakeEvent event)
    {
        ModelResourceLocation modelVariantLocation = new ModelResourceLocation(ResourceHelper.resource(blockName), variant);

        try
        {
            IBakedModel standard = event.getModelRegistry().getObject(modelVariantLocation);

            if (standard instanceof IPerspectiveAwareModel)
            {
                IBakedModel finalModel;

                finalModel = new BakedModelConveyor((IPerspectiveAwareModel) standard, DefaultVertexFormats.BLOCK);

                event.getModelRegistry().putObject(modelVariantLocation, finalModel);
            }
        }

        catch(Exception e)
        {
            LogHelper.error(e);
        }
    }
}