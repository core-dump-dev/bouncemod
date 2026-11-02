package core_dump.bouncemod.client.mixin;

import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "render", at = @At("HEAD"))
    private void bouncemod$applyBounce(T entity, float yaw, float tickDelta, MatrixStack matrices,
                                        VertexConsumerProvider vc, int light, CallbackInfo ci) {
        if (!BounceState.enabled) return;
        BounceConfig cfg = BounceState.config;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (entity.squaredDistanceTo(mc.player) > cfg.range * cfg.range) return;
        if (!shouldBounce(entity, cfg)) return;

        float time = (entity.getWorld().getTime() + tickDelta) / 20.0f;
        float beatDuration = 60.0f / Math.max(1f, cfg.bpm);
        float phase = (time / beatDuration) % 1.0f;
        float pingPong = phase < 0.5f ? phase * 2f : (1f - phase) * 2f;
        if (cfg.alternating && (entity.getId() & 1) != 0) pingPong = 1f - pingPong;
        float offset = (pingPong - 0.5f) * 2f * cfg.amplitude;
        matrices.translate(0.0, offset, 0.0);
    }

    private boolean shouldBounce(T entity, BounceConfig cfg) {
        if (cfg.enableAllEntities) return true;
        if (entity instanceof PassiveEntity && cfg.enableAnimals) return true;
        if (entity instanceof HostileEntity && cfg.enableHostile) return true;
        if (entity instanceof PlayerEntity && cfg.enablePlayers) return true;
        return false;
    }
}