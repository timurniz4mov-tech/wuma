package com.timurniz4mov.winterapocalypse.entity;

import com.timurniz4mov.winterapocalypse.WinterApocalypse;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WinterApocalypse.MODID);
    public static final RegistryObject<EntityType<SnowHunter>> SNOW_HUNTER = TYPES.register("snow_hunter", () -> EntityType.Builder.of(SnowHunter::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).build("snow_hunter"));
    public static final RegistryObject<EntityType<FrostGiant>> FROST_GIANT = TYPES.register("frost_giant", () -> EntityType.Builder.of(FrostGiant::new, MobCategory.MONSTER).sized(1.35F, 4.2F).clientTrackingRange(10).build("frost_giant"));
}
