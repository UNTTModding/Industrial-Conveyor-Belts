package com.untt.icb.proxy;

import com.untt.icb.client.model.ModelManager;
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
        LogHelper.info("ClientProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("ClientProxy: Post Initialization Complete!");
    }
}