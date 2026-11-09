package com.timurniz4mov.winterapocalypse;

import com.timurniz4mov.winterapocalypse.entity.FrostGiant;
import com.timurniz4mov.winterapocalypse.entity.ModEntities;
import com.timurniz4mov.winterapocalypse.entity.SnowHunter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterApocalypse.MODID)
public class WorldEvents {
    private static BlockPos coldCorePos;

    @SubscribeEvent public static void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
        if (level == null) return;
        BlockPos spawn = level.getSharedSpawnPos();
        for (int x = spawn.getX() - 100; x <= spawn.getX() + 100; x++) {
            for (int z = spawn.getZ() - 100; z <= spawn.getZ() + 100; z++) {
                BlockPos p = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), z);
                if (level.getBlockState(p).is(ModBlocks.COLD_CORE.get())) { coldCorePos = p.immutable(); return; }
            }
        }
        double angle = level.getRandom().nextDouble() * Math.PI * 2.0;
        int distance = 10 + level.getRandom().nextInt(91);
        int x = spawn.getX() + (int)Math.round(Math.cos(angle) * distance);
        int z = spawn.getZ() + (int)Math.round(Math.sin(angle) * distance);
        coldCorePos = new BlockPos(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z), z);
        level.setBlock(coldCorePos, ModBlocks.COLD_CORE.get().defaultBlockState(), 3);
    }

    @SubscribeEvent public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        if (!(event.player instanceof ServerPlayer player) || player.tickCount % 40 != 0) return;
        ServerLevel level = player.serverLevel();
        BlockPos pos = player.blockPosition();
        long day = level.getDayTime() / 24000L;
        double temperature = 18.0 - Math.min(12.0, day * 0.35);
        if (coldCorePos != null && level.dimension() == Level.OVERWORLD) {
            double distance = Math.sqrt(coldCorePos.distSqr(pos));
            int slowdown = level.getBlockState(coldCorePos).hasProperty(ColdCoreBlock.SLOWDOWN) ? level.getBlockState(coldCorePos).getValue(ColdCoreBlock.SLOWDOWN) : 0;
            double radius = (12.0 + day * 9.0) / (1.0 + slowdown * 0.65);
            temperature -= Math.max(0.0, 1.0 - distance / radius) * 35.0;
        }
        if (level.getBiome(pos).value().getBaseTemperature() < 0.15F) temperature -= 4.0;
        if (level.isRaining() && level.getBiome(pos).value().getBaseTemperature() < 0.15F) temperature -= 4.0;

        if (wearingFullSet(player, false)) temperature += 8.0;
        if (wearingFullSet(player, true)) temperature += 20.0;
        if (nearHeater(level, pos)) temperature += 22.0;

        player.displayClientMessage(Component.literal("Температура: " + String.format(java.util.Locale.ROOT, "%.1f", temperature) + " °C"), true);
        if (temperature <= -10.0 && !player.isCreative() && !player.isSpectator()) player.hurt(level.damageSources().freeze(), 1.0F);

        if (player.tickCount % 1200 == 0) maybeSpawnSnowMob(level, player);
        if (player.tickCount % 2400 == 0) maybeSpawnFrostMaster(level, player);
    }

    private static boolean wearingFullSet(ServerPlayer p, boolean arctic) {
        if (arctic) return p.getInventory().armor.get(3).is(ModItems.ARCTIC_HELMET.get()) && p.getInventory().armor.get(2).is(ModItems.ARCTIC_CHESTPLATE.get()) && p.getInventory().armor.get(1).is(ModItems.ARCTIC_LEGGINGS.get()) && p.getInventory().armor.get(0).is(ModItems.ARCTIC_BOOTS.get());
        return p.getInventory().armor.get(3).is(ModItems.WARM_FUR_HELMET.get()) && p.getInventory().armor.get(2).is(ModItems.WARM_FUR_CHESTPLATE.get()) && p.getInventory().armor.get(1).is(ModItems.WARM_FUR_LEGGINGS.get()) && p.getInventory().armor.get(0).is(ModItems.WARM_FUR_BOOTS.get());
    }

    private static boolean nearHeater(ServerLevel level, BlockPos center) {
        for (BlockPos p : BlockPos.betweenClosed(center.offset(-6, -3, -6), center.offset(6, 3, 6))) {
            if (p.distSqr(center) <= 36 && level.getBlockState(p).is(ModBlocks.HEATER.get())) return true;
        }
        return false;
    }

    private static boolean snowyGround(ServerLevel level, BlockPos p) {
        return level.getBlockState(p.below()).is(Blocks.SNOW_BLOCK) || level.getBlockState(p.below()).is(Blocks.SNOW);
    }

    private static void maybeSpawnSnowMob(ServerLevel level, ServerPlayer player) {
        if (level.getRandom().nextInt(3) != 0 || level.getBiome(player.blockPosition()).value().getBaseTemperature() > 0.15F) return;
        int x = player.getBlockX() + level.getRandom().nextInt(31) - 15;
        int z = player.getBlockZ() + level.getRandom().nextInt(31) - 15;
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        BlockPos spawn = new BlockPos(x, y, z);
        if (!level.hasChunkAt(spawn) || !snowyGround(level, spawn) || level.getMaxLocalRawBrightness(spawn) > 8) return;
        boolean giant = level.getRandom().nextInt(12) == 0;
        if (level.getEntitiesOfClass(FrostGiant.class, player.getBoundingBox().inflate(48)).size() >= 1 && giant) return;
        if (level.getEntitiesOfClass(SnowHunter.class, player.getBoundingBox().inflate(32)).size() >= 4 && !giant) return;
        Entity mob = giant ? ModEntities.FROST_GIANT.get().create(level) : ModEntities.SNOW_HUNTER.get().create(level);
        if (mob == null) return;
        mob.moveTo(x + 0.5, y, z + 0.5, level.getRandom().nextFloat() * 360F, 0F);
        if (mob instanceof net.minecraft.world.entity.Mob living) ForgeEventFactory.onFinalizeSpawn(living, level, level.getCurrentDifficultyAt(spawn), MobSpawnType.NATURAL, null, null);
        level.addFreshEntity(mob);
    }

    private static void maybeSpawnFrostMaster(ServerLevel level, ServerPlayer player) {
        if (level.getRandom().nextInt(5) != 0 || level.getBiome(player.blockPosition()).value().getBaseTemperature() > 0.15F) return;
        if (!level.getEntitiesOfClass(WanderingTrader.class, player.getBoundingBox().inflate(48), t -> t.getCustomName() != null && t.getCustomName().getString().equals("Мастер зимы")).isEmpty()) return;
        int x = player.getBlockX() + level.getRandom().nextInt(25) - 12;
        int z = player.getBlockZ() + level.getRandom().nextInt(25) - 12;
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        BlockPos spawn = new BlockPos(x, y, z);
        if (!level.hasChunkAt(spawn) || !snowyGround(level, spawn)) return;
        WanderingTrader trader = net.minecraft.world.entity.EntityType.WANDERING_TRADER.create(level);
        if (trader == null) return;
        trader.moveTo(x + 0.5, y, z + 0.5, 0F, 0F);
        trader.setCustomName(Component.literal("Мастер зимы"));
        trader.setCustomNameVisible(true);
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 5), new ItemStack(ModItems.WARM_FUR_HELMET.get()), 8, 8, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 9), new ItemStack(ModItems.WARM_FUR_CHESTPLATE.get()), 6, 12, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 7), new ItemStack(ModItems.WARM_FUR_LEGGINGS.get()), 6, 10, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.WARM_FUR_BOOTS.get()), 8, 7, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 14), new ItemStack(ModItems.ARCTIC_HELMET.get()), 3, 15, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 22), new ItemStack(ModItems.ARCTIC_CHESTPLATE.get()), 3, 20, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 18), new ItemStack(ModItems.ARCTIC_LEGGINGS.get()), 3, 18, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 12), new ItemStack(ModItems.ARCTIC_BOOTS.get()), 3, 14, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 4), new ItemStack(ModItems.COLD_SEAL.get()), 6, 6, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 7), new ItemStack(ModItems.HEATER_ITEM.get()), 4, 10, 0.05F));
        trader.getOffers().add(new MerchantOffer(new ItemStack(Items.EMERALD, 2), new ItemStack(ModItems.FROST_CRYSTAL.get()), 12, 4, 0.05F));
        level.addFreshEntity(trader);
    }
}
