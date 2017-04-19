package com.untt.icb.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

import com.untt.icb.client.model.ModelManager;
import com.untt.icb.event.EventHandlerClientTickConveyor;
import com.untt.icb.event.EventHandlerModelConveyorReplace;
import com.untt.icb.utility.LogHelper;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        ModelManager.INSTANCE.registerBlockModels();

        LogHelper.info("ClientProxy: Pre Initialization Complete!");
    }

    @Override
    public void init()
    {
    	Render<EntityItem> previous = (Render<EntityItem>) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityItem.class);
		Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntityItem.class, new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) 
		{
			@Override
			public void doRender(EntityItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
				if (entity.getEntityData().getBoolean("onBelt"))
				{	
					float f1 = shouldBob() ? MathHelper.sin(((float) entity.getAge() + Minecraft.getMinecraft().getRenderPartialTicks()) / 10.0F + entity.hoverStart) * 0.1F + 0.1F : 0;
					GlStateManager.translate(0f, -f1, 0f);
					
				}
				if (previous != null)
					previous.doRender(entity, x, y, z, entityYaw, partialTicks);
				else
					super.doRender(entity, x, y, z, entityYaw, partialTicks);
			}
		});

        LogHelper.info("ClientProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("ClientProxy: Post Initialization Complete!");
    }

    @Override
    public void registerEventHandler()
    {
        MinecraftForge.EVENT_BUS.register(new EventHandlerModelConveyorReplace());
        MinecraftForge.EVENT_BUS.register(new EventHandlerClientTickConveyor());
    }
}