package com.timurniz4mov.winterapocalypse.client;

import com.timurniz4mov.winterapocalypse.WinterApocalypse;
import com.timurniz4mov.winterapocalypse.entity.FrostGiant;
import com.timurniz4mov.winterapocalypse.entity.ModEntities;
import com.timurniz4mov.winterapocalypse.entity.SnowHunter;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterApocalypse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModRenderers {
    @SubscribeEvent public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SNOW_HUNTER.get(), ctx -> new SnowHunterRenderer(ctx));
        event.registerEntityRenderer(ModEntities.FROST_GIANT.get(), ctx -> new FrostGiantRenderer(ctx));
    }
    private static final ResourceLocation ZOMBIE_TEXTURE = new ResourceLocation("winter_apocalypse", "textures/entity/snow_hunter.png");
    private static class SnowHunterRenderer extends MobRenderer<SnowHunter, ZombieModel<SnowHunter>> {
        SnowHunterRenderer(EntityRendererProvider.Context ctx) { super(ctx, new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.5F); }
        @Override public ResourceLocation getTextureLocation(SnowHunter entity) { return ZOMBIE_TEXTURE; }
    }
    private static class FrostGiantRenderer extends MobRenderer<FrostGiant, ZombieModel<FrostGiant>> {
        FrostGiantRenderer(EntityRendererProvider.Context ctx) { super(ctx, new ZombieModel<>(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.8F); }
        @Override public ResourceLocation getTextureLocation(FrostGiant entity) { return new ResourceLocation("winter_apocalypse", "textures/entity/frost_giant.png"); }
        @Override protected void scale(FrostGiant entity, PoseStack poseStack, float partialTick) { poseStack.scale(2.15F, 2.15F, 2.15F); }
    }
}
