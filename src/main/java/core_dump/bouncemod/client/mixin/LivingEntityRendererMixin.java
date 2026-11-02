package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {

    @Inject(method = "render", at = @At("HEAD"))
    private void bouncemod$applyBounce(T entity, float entityYaw, float partialTicks,
                                        PoseStack poseStack, SubmitNodeCollector collector,
                                        int packedLight, CallbackInfo ci) {
        if (!BounceState.enabled) return;
        BounceConfig cfg = BounceState.config;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (entity.distanceToSqr(mc.player) > cfg.range * cfg.range) return;
        if (!shouldBounce(entity, cfg)) return;

        float time = (entity.level().getGameTime() + partialTicks) / 20.0f;
        float beatDuration = 60.0f / Math.max(1f, cfg.bpm);
        float phase = (time / beatDuration) % 1.0f;
        float pingPong = phase < 0.5f ? phase * 2f : (1f - phase) * 2f;
        if (cfg.alternating && (entity.getId() & 1) != 0) pingPong = 1f - pingPong;
        float offset = (pingPong - 0.5f) * 2f * cfg.amplitude;
        poseStack.translate(0.0, offset, 0.0);
    }

    private boolean shouldBounce(T entity, BounceConfig cfg) {
        if (cfg.enableAllEntities) return true;
        if (entity instanceof Animal && cfg.enableAnimals) return true;
        if (entity instanceof Monster && cfg.enableHostile) return true;
        if (entity instanceof Player && cfg.enablePlayers) return true;
        return false;
    }
}