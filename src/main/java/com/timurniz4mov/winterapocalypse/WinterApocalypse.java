package com.timurniz4mov.winterapocalypse;

import com.mojang.logging.LogUtils;
import com.timurniz4mov.winterapocalypse.entity.ModEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WinterApocalypse.MODID)
public class WinterApocalypse {
    public static final String MODID = "winter_apocalypse";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WinterApocalypse() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModBlocks.ITEMS.register(bus);
        ModItems.ITEMS.register(bus);
        ModEntities.TYPES.register(bus);
    }
}
