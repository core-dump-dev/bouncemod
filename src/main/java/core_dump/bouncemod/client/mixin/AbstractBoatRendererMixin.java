package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import core_dump.bouncemod.client.CubicBezier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AbstractBoatRenderer;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBoatRenderer.class)
public class AbstractBoatRendererMixin {

    @Inject(method = "submit", at = @At("HEAD"))
    private void bouncemod$squashBoat(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraState, CallbackInfo ci) {
        if (!BounceState.enabled) return;
        BounceConfig cfg = BounceState.config;
        if (!cfg.enableVehicles) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() + mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 20.0f;
        float phase = (time / (60.0f / Math.max(1f, cfg.bpm))) % 1.0f;
        float squashVal = CubicBezier.sample(cfg.curve, phase);
        float squash = squashVal * cfg.amplitude;

        float sx = Math.max(0.2f, 1.0f + squash * 0.25f);
        float sy = Math.max(0.2f, 1.0f - squash * 0.25f);
        poseStack.scale(sx, sy, sx);
    }
}