package com.untt.icb.utility;

import net.minecraft.util.ResourceLocation;

import com.untt.icb.reference.Reference;

public class ResourceHelper
{
    public static String resource(String resource)
    {
        return String.format("%s:%s", Reference.MOD_ID.toLowerCase(), resource);
    }

    public static ResourceLocation getResource(String resource)
    {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), resource);
    }
}