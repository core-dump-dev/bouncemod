package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import core_dump.bouncemod.client.CubicBezier;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("HEAD")
    )
    private void bouncemod$applyEntitySquash(EntityRenderState state, PoseStack poseStack,
                                             SubmitNodeCollector collector, CameraRenderState cameraState,
                                             CallbackInfo ci) {
        if (state instanceof LivingEntityRenderState) return;
        if (!BounceState.enabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        BounceConfig cfg = BounceState.config;

        Vec3 pos = new Vec3(state.x, state.y, state.z);
        if (pos.distanceToSqr(mc.player.position()) > cfg.range * cfg.range) return;

        EntityType<?> type = state.entityType;
        boolean apply = false;

        if (cfg.enableArmorStands && type == EntityTypes.ARMOR_STAND) apply = true;

        // Используем ConventionalEntityTypeTags из Fabric API
        if (cfg.enableVehicles && (type.builtInRegistryHolder().is(ConventionalEntityTypeTags.BOATS)
                || type.builtInRegistryHolder().is(ConventionalEntityTypeTags.MINECARTS))) {
            apply = true;
        }

        if (cfg.enableItems && (type == EntityTypes.ITEM || type == EntityTypes.EXPERIENCE_ORB)) apply = true;

        if (!apply) return;

        float time = (mc.level.getGameTime() + mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 20.0f;
        float phase = (time / (60.0f / Math.max(1f, cfg.bpm))) % 1.0f;
        float squashVal = CubicBezier.sample(cfg.curve, phase);

        if (cfg.alternating && (type.hashCode() & 1) != 0) squashVal = 1f - squashVal;

        float squash = squashVal * cfg.amplitude;
        poseStack.scale(1.0f + squash * 0.35f, 1.0f - squash * 0.30f, 1.0f + squash * 0.35f);
    }
}