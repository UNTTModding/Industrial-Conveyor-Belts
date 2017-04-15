package com.untt.icb.client.model;

import com.untt.icb.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

import java.util.HashSet;
import java.util.Set;

public class ModelManager
{
    public static final ModelManager INSTANCE = new ModelManager();

    private final Set<Item> registeredItems = new HashSet<>();

    private ModelManager()
    {

    }

    public void registerBlockModels()
    {
        BlockRegistry.BLOCKS.stream().filter(block -> !registeredItems.contains(Item.getItemFromBlock(block))).forEach(this::registerBlockItemModel);
    }

    private void registerBlockItemModel(Block block)
    {
        final Item item = ItemBlock.getItemFromBlock(block);
    }

    private void registerItemModel(Item item)
    {
        final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName().toString(), "inventory");

        ModelBakery.registerItemVariants(item, modelResourceLocation);
        ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

        registeredItems.add(item);
    }
}