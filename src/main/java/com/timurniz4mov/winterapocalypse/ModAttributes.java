package com.timurniz4mov.winterapocalypse;

import com.timurniz4mov.winterapocalypse.entity.ModEntities;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterApocalypse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    @SubscribeEvent public static void register(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SNOW_HUNTER.get(), Zombie.createAttributes().add(Attributes.MAX_HEALTH, 26.0).add(Attributes.ATTACK_DAMAGE, 4.5).add(Attributes.MOVEMENT_SPEED, 0.27).build());
        event.put(ModEntities.FROST_GIANT.get(), Zombie.createAttributes().add(Attributes.MAX_HEALTH, 110.0).add(Attributes.ATTACK_DAMAGE, 15.0).add(Attributes.MOVEMENT_SPEED, 0.19).add(Attributes.KNOCKBACK_RESISTANCE, 0.9).build());
    }
}
