package com.untt.icb.proxy;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.untt.icb.IndustrialConveyorBelts;
import com.untt.icb.gui.GuiHandler;
import com.untt.icb.init.ICBBlocks;
import com.untt.icb.init.ICBRecipes;
import com.untt.icb.init.ICBTileEntities;
import com.untt.icb.network.MessageButton;
import com.untt.icb.reference.Reference;
import com.untt.icb.utility.LogHelper;

public class CommonProxy implements IProxy
{
    @Override
    public void preInit()
    {
        ICBBlocks.preInit();

        LogHelper.info("CommonProxy: Pre Initialization Complete!");
    }

    @Override
    public void init()
    {
        ICBTileEntities.init();

        ICBRecipes.init();
        
        NetworkRegistry.INSTANCE.registerGuiHandler(IndustrialConveyorBelts.instance, new GuiHandler());

        IndustrialConveyorBelts.networkWrapper=new SimpleNetworkWrapper(Reference.MOD_ID);
        IndustrialConveyorBelts.networkWrapper.registerMessage(MessageButton.class, MessageButton.class, 0, Side.SERVER);
        
        LogHelper.info("CommonProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("CommonProxy: Post Initialization Complete!");
    }

    @Override
    public void registerEventHandler()
    {

    }
}