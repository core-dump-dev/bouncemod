package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import core_dump.bouncemod.client.CubicBezier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
    private void bouncemod$squashHand(float partialTick, PoseStack poseStack, SubmitNodeCollector collector,
                                      PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState handState,
                                      CallbackInfo ci) {
        if (!BounceState.enabled) return;
        BounceConfig cfg = BounceState.config;
        if (!cfg.enableHandItem) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float time = (mc.level.getGameTime() + partialTick) / 20.0f;
        float phase = (time / (60.0f / Math.max(1f, cfg.bpm))) % 1.0f;
        
        // Инвертируем фазу, чтобы рука не была в противофазе с миром
        phase = 1.0f - phase;

        float squashVal = CubicBezier.sample(cfg.curve, phase);
        float squash = squashVal * cfg.amplitude;

        float sx = 1.0f + squash * 0.35f;
        float sy = 1.0f - squash * 0.30f;
        poseStack.scale(sx, sy, sx);
    }
}