package com.example.timemorph.mixin;

import com.example.timemorph.TimeMorphConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Entity altindaki yuvarlak golgeyi Minecraft saatine gore degistirir. */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

    @Unique private Entity timemorph$current;

    private static final String RENDER =
        "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;"
      + "Lnet/minecraft/client/render/VertexConsumerProvider;I)V";

    private static final String RENDER_SHADOW =
        "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderShadow("
      + "Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;"
      + "Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/WorldView;F)V";

    @Inject(method = RENDER, at = @At("HEAD"))
    private void timemorph$capture(Entity entity, double x, double y, double z, float yaw, float tickDelta,
                                   MatrixStack matrices, VertexConsumerProvider vcp, int light, CallbackInfo ci) {
        this.timemorph$current = entity;
    }

    @ModifyArg(method = RENDER, at = @At(value = "INVOKE", target = RENDER_SHADOW), index = 3)
    private float timemorph$opacity(float opacity) {
        Entity e = this.timemorph$current;
        if (!applies(e)) return opacity;
        float m = TimeMorphConfig.INSTANCE.opacityMultiplier(e.getWorld().getTimeOfDay());
        return Math.max(0f, Math.min(1f, opacity * m));
    }

    @ModifyArg(method = RENDER, at = @At(value = "INVOKE", target = RENDER_SHADOW), index = 6)
    private float timemorph$radius(float radius) {
        Entity e = this.timemorph$current;
        if (!applies(e)) return radius;
        float m = TimeMorphConfig.INSTANCE.radiusMultiplier(e.getWorld().getTimeOfDay());
        return Math.min(32.0f, Math.max(0f, radius * m));
    }

    @Unique
    private static boolean applies(Entity e) {
        if (e == null) return false;
        TimeMorphConfig c = TimeMorphConfig.INSTANCE;
        return (e instanceof PlayerEntity) ? c.affectPlayers : c.affectMobs;
    }
}
