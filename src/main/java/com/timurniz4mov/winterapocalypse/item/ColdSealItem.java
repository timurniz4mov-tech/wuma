package com.timurniz4mov.winterapocalypse.item;

import com.timurniz4mov.winterapocalypse.ColdCoreBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class ColdSealItem extends Item {
    public ColdSealItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (!(state.getBlock() instanceof ColdCoreBlock)) return InteractionResult.PASS;
        int level = state.getValue(ColdCoreBlock.SLOWDOWN);
        if (level >= 3) {
            if (!context.getLevel().isClientSide) context.getPlayer().displayClientMessage(Component.literal("Ядро уже максимально замедлено!"), true);
            return InteractionResult.CONSUME;
        }
        if (!context.getLevel().isClientSide) {
            context.getLevel().setBlock(context.getClickedPos(), state.setValue(ColdCoreBlock.SLOWDOWN, level + 1), 3);
            if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) context.getItemInHand().shrink(1);
            if (context.getPlayer() != null) context.getPlayer().displayClientMessage(Component.literal("Холод замедлен! Уровень: " + (level + 1) + "/3"), true);
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }
}
