package com.timurniz4mov.winterapocalypse;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterApocalypse.MODID)
public class WorldEvents {
    private static BlockPos coldCorePos;

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
        if (level == null) return;
        BlockPos spawn = level.getSharedSpawnPos();

        // Search the spawn area by surface height, rather than scanning every Y level.
        for (int x = spawn.getX() - 100; x <= spawn.getX() + 100; x++) {
            for (int z = spawn.getZ() - 100; z <= spawn.getZ() + 100; z++) {
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                BlockPos p = new BlockPos(x, y, z);
                if (level.getBlockState(p).is(ModBlocks.COLD_CORE.get())) {
                    coldCorePos = p.immutable();
                    return;
                }
            }
        }

        double angle = level.getRandom().nextDouble() * Math.PI * 2.0;
        int distance = 10 + level.getRandom().nextInt(91);
        int x = spawn.getX() + (int)Math.round(Math.cos(angle) * distance);
        int z = spawn.getZ() + (int)Math.round(Math.sin(angle) * distance);
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        coldCorePos = new BlockPos(x, y, z);
        level.setBlock(coldCorePos, ModBlocks.COLD_CORE.get().defaultBlockState(), 3);
        WinterApocalypse.LOGGER.info("Cold Core created at {}", coldCorePos);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        if (!(event.player instanceof ServerPlayer player) || player.tickCount % 40 != 0) return;
        ServerLevel level = player.serverLevel();
        BlockPos playerPos = player.blockPosition();
        long day = level.getDayTime() / 24000L;
        double temperature = 18.0 - Math.min(12.0, day * 0.35);

        if (coldCorePos != null) {
            double distance = Math.sqrt(coldCorePos.distSqr(playerPos));
            double radius = 12.0 + day * 9.0;
            double influence = Math.max(0.0, 1.0 - distance / radius);
            temperature -= influence * 35.0;
        }
        float biomeTemp = level.getBiome(playerPos).value().getBaseTemperature();
        if (biomeTemp < 0.15F) temperature -= 4.0;

        player.displayClientMessage(Component.literal(
                "Температура: " + String.format(java.util.Locale.ROOT, "%.1f", temperature) + " °C"), true);

        if (temperature <= -10.0 && !player.isCreative() && !player.isSpectator()) {
            player.hurt(level.damageSources().freeze(), 1.0F);
        }
    }
}
