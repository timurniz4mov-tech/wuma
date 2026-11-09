package com.timurniz4mov.winterapocalypse;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ColdCoreBlock extends Block {
    public ColdCoreBlock(BlockBehaviour.Properties properties) { super(properties); }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, level, pos, oldState, moving);
        if (!level.isClientSide) level.scheduleTick(pos, this, 20);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        long day = level.getDayTime() / 24000L;
        int radius = Math.min(180, 12 + (int)day * 9);
        int attempts = Math.min(24, 3 + (int)day * 2);
        for (int i = 0; i < attempts; i++) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;
            if (dx * dx + dz * dz > radius * radius) continue;
            int x = pos.getX() + dx;
            int z = pos.getZ() + dz;
            if (!level.hasChunkAt(new BlockPos(x, level.getMinBuildHeight(), z))) continue;
            int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);
            BlockPos target = new BlockPos(x, y, z);
            BlockState at = level.getBlockState(target);
            BlockState below = level.getBlockState(target.below());

            if (at.getFluidState().getType() == Fluids.WATER || at.is(Blocks.WATER)) {
                level.setBlockAndUpdate(target, Blocks.ICE.defaultBlockState());
            } else if (at.isAir() && below.isSolidRender(level, target.below())) {
                level.setBlockAndUpdate(target, Blocks.SNOW.defaultBlockState());
            } else if (at.is(Blocks.SNOW) && random.nextFloat() < 0.35F) {
                level.setBlockAndUpdate(target, Blocks.SNOW_BLOCK.defaultBlockState());
            } else if (at.getBlock() instanceof SnowLayerBlock && random.nextFloat() < 0.5F) {
                int layers = at.getValue(SnowLayerBlock.LAYERS);
                if (layers < 8) level.setBlockAndUpdate(target, at.setValue(SnowLayerBlock.LAYERS, layers + 1));
                else level.setBlockAndUpdate(target, Blocks.SNOW_BLOCK.defaultBlockState());
            }
        }
        level.scheduleTick(pos, this, Math.max(5, 80 - (int)Math.min(day * 5, 65)));
    }
}
