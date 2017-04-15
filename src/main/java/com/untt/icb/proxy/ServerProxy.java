package com.untt.icb.proxy;

import com.untt.icb.utility.LogHelper;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        LogHelper.info("ServerProxy: Pre Initialization Complete!");
    }

    @Override
    public void init()
    {
        LogHelper.info("ServerProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("ServerProxy: Post Initialization Complete!");
    }
}