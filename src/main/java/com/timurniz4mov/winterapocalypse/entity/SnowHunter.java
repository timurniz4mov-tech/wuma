package com.timurniz4mov.winterapocalypse.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

public class SnowHunter extends Zombie {
    public SnowHunter(EntityType<? extends Zombie> type, Level level) { super(type, level); this.setCanBreakDoors(false); }
    @Override protected boolean isSunSensitive() { return false; }
    @Override public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
        return hit;
    }
}
