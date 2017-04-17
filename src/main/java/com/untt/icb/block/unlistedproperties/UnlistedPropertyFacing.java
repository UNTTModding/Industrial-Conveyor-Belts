package com.untt.icb.block.unlistedproperties;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyFacing implements IUnlistedProperty<EnumFacing>
{
    private final String name;

    public UnlistedPropertyFacing(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean isValid(EnumFacing facing)
    {
        return true;
    }

    @Override
    public Class<EnumFacing> getType()
    {
        return EnumFacing.class;
    }

    @Override
    public String valueToString(EnumFacing facing)
    {
        return facing.toString();
    }
}