package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import core_dump.bouncemod.client.CubicBezier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartRenderer.class)
public class AbstractMinecartRendererMixin {

    @Inject(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("HEAD")
    )
    private void bouncemod$squashMinecart(MinecartRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState, CallbackInfo ci) {
        if (!BounceState.enabled) return;
        BounceConfig cfg = BounceState.config;
        if (!cfg.enableVehicles) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() + mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 20.0f;
        float phase = (time / (60.0f / Math.max(1f, cfg.bpm))) % 1.0f;
        float squashVal = CubicBezier.sample(cfg.curve, phase);

        if (cfg.alternating && (state.hashCode() & 1) != 0) squashVal = 1f - squashVal;

        float squash = Math.max(-1f, Math.min(1f, squashVal * cfg.amplitude));
        poseStack.scale(1.0f + squash * 0.35f, 1.0f - squash * 0.30f, 1.0f + squash * 0.35f);
    }
}