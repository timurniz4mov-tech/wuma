package com.timurniz4mov.winterapocalypse.armor;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import java.util.function.Supplier;

public enum WinterArmorMaterials implements ArmorMaterial {
    WARM_FUR("warm_fur", 13, new int[]{2, 4, 3, 1}, 12, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Items.LEATHER)),
    ARCTIC("arctic", 25, new int[]{3, 6, 5, 2}, 18, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.05F, () -> Ingredient.of(WinterItemLookup.frostCrystal()));

    private final String name;
    private final int durability;
    private final int[] defense;
    private final int enchantment;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockback;
    private final Supplier<Ingredient> repair;

    WinterArmorMaterials(String name, int durability, int[] defense, int enchantment, SoundEvent equipSound, float toughness, float knockback, Supplier<Ingredient> repair) {
        this.name = name; this.durability = durability; this.defense = defense; this.enchantment = enchantment;
        this.equipSound = equipSound; this.toughness = toughness; this.knockback = knockback; this.repair = repair;
    }
    @Override public int getDurabilityForType(net.minecraft.world.item.ArmorItem.Type type) { return durability * switch (type) { case HELMET -> 11; case CHESTPLATE -> 16; case LEGGINGS -> 15; case BOOTS -> 13; }; }
    @Override public int getDefenseForType(net.minecraft.world.item.ArmorItem.Type type) { return defense[switch (type) { case HELMET -> 0; case CHESTPLATE -> 1; case LEGGINGS -> 2; case BOOTS -> 3; }]; }
    @Override public int getEnchantmentValue() { return enchantment; }
    @Override public SoundEvent getEquipSound() { return equipSound; }
    @Override public Ingredient getRepairIngredient() { return repair.get(); }
    @Override public String getName() { return "winter_apocalypse:" + name; }
    @Override public float getToughness() { return toughness; }
    @Override public float getKnockbackResistance() { return knockback; }

    private static class WinterItemLookup {
        static net.minecraft.world.item.Item frostCrystal() { return com.timurniz4mov.winterapocalypse.ModItems.FROST_CRYSTAL.get(); }
    }
}
