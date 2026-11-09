package com.timurniz4mov.winterapocalypse;

import com.timurniz4mov.winterapocalypse.armor.WinterArmorMaterials;
import com.timurniz4mov.winterapocalypse.item.ColdSealItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WinterApocalypse.MODID);

    public static final RegistryObject<Item> WARM_FUR_HELMET = ITEMS.register("warm_fur_helmet", () -> new ArmorItem(WinterArmorMaterials.WARM_FUR, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> WARM_FUR_CHESTPLATE = ITEMS.register("warm_fur_chestplate", () -> new ArmorItem(WinterArmorMaterials.WARM_FUR, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> WARM_FUR_LEGGINGS = ITEMS.register("warm_fur_leggings", () -> new ArmorItem(WinterArmorMaterials.WARM_FUR, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> WARM_FUR_BOOTS = ITEMS.register("warm_fur_boots", () -> new ArmorItem(WinterArmorMaterials.WARM_FUR, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> ARCTIC_HELMET = ITEMS.register("arctic_helmet", () -> new ArmorItem(WinterArmorMaterials.ARCTIC, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> ARCTIC_CHESTPLATE = ITEMS.register("arctic_chestplate", () -> new ArmorItem(WinterArmorMaterials.ARCTIC, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ARCTIC_LEGGINGS = ITEMS.register("arctic_leggings", () -> new ArmorItem(WinterArmorMaterials.ARCTIC, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> ARCTIC_BOOTS = ITEMS.register("arctic_boots", () -> new ArmorItem(WinterArmorMaterials.ARCTIC, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> COLD_SEAL = ITEMS.register("cold_seal", () -> new ColdSealItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> FROST_CRYSTAL = ITEMS.register("frost_crystal", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEATER_ITEM = ITEMS.register("winter_heater", () -> new BlockItem(ModBlocks.HEATER.get(), new Item.Properties()));
}
