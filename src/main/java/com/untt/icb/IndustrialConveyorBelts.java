package com.untt.icb;

import com.untt.icb.proxy.CommonProxy;
import com.untt.icb.proxy.IProxy;
import com.untt.icb.reference.Reference;
import com.untt.icb.utility.LogHelper;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME,  version = Reference.VERSION)
public class IndustrialConveyorBelts
{
    @Mod.Instance(Reference.MOD_ID)
    public static IndustrialConveyorBelts instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy sidedProxy;
    private static CommonProxy commonProxy = new CommonProxy();
    
    public static SimpleNetworkWrapper networkWrapper;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        commonProxy.preInit();
        sidedProxy.preInit();

        LogHelper.info("Pre Initialization Complete!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        commonProxy.init();
        sidedProxy.init();

        commonProxy.registerEventHandler();
        sidedProxy.registerEventHandler();

        LogHelper.info("Initialization Complete!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        commonProxy.postInit();
        sidedProxy.postInit();

        LogHelper.info("Post Initialization Complete!");
    }
}