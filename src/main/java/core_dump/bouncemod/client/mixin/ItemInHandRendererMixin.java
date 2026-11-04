package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceAnimation;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
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
    private void bouncemod$beginHandTransform(float partialTick, PoseStack poseStack,
            SubmitNodeCollector collector, PlayerRenderState playerState,
            FirstPersonHandsAndItemsRenderState handState, CallbackInfo ci) {
        // Isolate our transform from the caller and from subsequent render submissions.
        poseStack.pushPose();

        if (!BounceState.enabled) return;
        BounceConfig cfg = BounceState.config;
        if (!cfg.enableHandItem) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        float squash = Math.max(-1.0f, Math.min(1.0f, BounceAnimation.getSquash(mc, cfg)));
        float sx = 1.0f + squash * 0.35f;
        float sy = 1.0f - squash * 0.30f;
        poseStack.scale(sx, sy, sx);
        poseStack.translate(0.0, -squash * 0.15f, 0.0);
    }

    @Inject(method = "submitHandsWithItems", at = @At("TAIL"))
    private void bouncemod$endHandTransform(float partialTick, PoseStack poseStack,
            SubmitNodeCollector collector, PlayerRenderState playerState,
            FirstPersonHandsAndItemsRenderState handState, CallbackInfo ci) {
        poseStack.popPose();
    }
}
