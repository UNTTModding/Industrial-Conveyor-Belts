package com.untt.icb.proxy;

import com.untt.icb.init.ICBBlocks;
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
        LogHelper.info("CommonProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("CommonProxy: Post Initialization Complete!");
    }
}