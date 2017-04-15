package com.untt.icb.client.model;

import com.untt.icb.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
        final Item item = Item.getItemFromBlock(block);

        registerItemModel(item);
    }

    private void registerBlockItemModel(Block block, String modelLocation)
    {
        registerItemModel(Item.getItemFromBlock(block), modelLocation);
    }

    private void registerBlockItemModel(Block block, ModelResourceLocation fullModelLocation)
    {
        registerItemModel(Item.getItemFromBlock(block), fullModelLocation);
    }

    private void registerBlockItemModelForMeta(Block block, int metadata, String variant)
    {
        registerItemModelForMeta(Item.getItemFromBlock(block), metadata, variant);
    }

    public void registerItemModels()
    {
        //ItemRegistry.ITEMS.stream().filter(item -> !registeredItems.contains(item)).forEach(this::registerItemModel);
    }

    private void registerItemModel(Item item)
    {
        registerItemModel(item, item.getRegistryName().toString());
    }

    private void registerItemModel(Item item, String modelLocation)
    {
        final ModelResourceLocation fullModelLocation;

        fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");

        registerItemModel(item, fullModelLocation);
    }

    private void registerItemModel(Item item, ModelResourceLocation fullModelLocation)
    {
        ModelBakery.registerItemVariants(item, fullModelLocation);
        registerItemModel(item, MeshDefinitionFix.create(stack -> fullModelLocation));
    }

    private void registerItemModel(Item item, ItemMeshDefinition meshDefinition)
    {
        registeredItems.add(item);
        ModelLoader.setCustomMeshDefinition(item, meshDefinition);
    }

    private void registerItemModelForMeta(Item item, int metadata, String variant)
    {
        registerItemModelForMeta(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
    }

    private void registerItemModelForMeta(Item item, int metadata, ModelResourceLocation modelResourceLocation)
    {
        registeredItems.add(item);
        ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
    }
}