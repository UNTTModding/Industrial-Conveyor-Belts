package com.untt.icb.block.unlistedproperties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyBoolean implements IUnlistedProperty<Boolean>
{
    private final String name;

    public UnlistedPropertyBoolean(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean isValid(Boolean bool)
    {
        return true;
    }

    @Override
    public Class<Boolean> getType()
    {
        return Boolean.class;
    }

    @Override
    public String valueToString(Boolean bool)
    {
        return bool.toString();
    }
}