package com.untt.icb.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import com.untt.icb.client.model.baked.BakedModelConveyor;
import com.untt.icb.reference.Names;
import com.untt.icb.tileentity.TileEntityConveyorBase;
import com.untt.icb.utility.LogHelper;
import com.untt.icb.utility.ResourceHelper;

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
    
    @SubscribeEvent
    public void tick(ClientTickEvent event){
		World world = Minecraft.getMinecraft().world;
		if (world == null)
			return;
		for (Entity e : world.loadedEntityList) {
			if (e instanceof EntityItem && e.ticksExisted % 15 == 0 && !(world.getTileEntity(new BlockPos(e)) instanceof TileEntityConveyorBase)) {
				e.getEntityData().setBoolean("onBelt", false);
			}
		}
    }
}