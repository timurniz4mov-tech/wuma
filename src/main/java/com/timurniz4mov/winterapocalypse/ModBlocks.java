package com.timurniz4mov.winterapocalypse;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WinterApocalypse.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WinterApocalypse.MODID);

    public static final RegistryObject<Block> COLD_CORE = BLOCKS.register("cold_core",
            () -> new ColdCoreBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(5.0F, 1200.0F).lightLevel(state -> 8).noOcclusion()));
    public static final RegistryObject<Item> COLD_CORE_ITEM = ITEMS.register("cold_core", () -> new BlockItem(COLD_CORE.get(), new Item.Properties()));

    public static final RegistryObject<Block> HEATER = BLOCKS.register("winter_heater",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(4.0F).lightLevel(state -> 15)));
}
