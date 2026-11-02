package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(
        method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
        at = @At("HEAD")
    )
    private void bouncemod$applyBounce(LivingEntityRenderState state, PoseStack poseStack,
                                        SubmitNodeCollector collector, CameraRenderState cameraState,
                                        CallbackInfo ci) {
        if (!BounceState.enabled) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        BounceConfig cfg = BounceState.config;

        Vec3 entityPos = new Vec3(state.x, state.y, state.z);
        if (entityPos.distanceToSqr(mc.player.position()) > cfg.range * cfg.range) return;

        if (!shouldBounce(state, cfg)) return;

        float time = (mc.level.getGameTime() + mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 20.0f;
        float beatDuration = 60.0f / Math.max(1f, cfg.bpm);
        float phase = (time / beatDuration) % 1.0f;
        float pingPong = phase < 0.5f ? phase * 2f : (1f - phase) * 2f;

        if (cfg.alternating && (state.entityType.hashCode() & 1) != 0) {
            pingPong = 1f - pingPong;
        }

        float squash = pingPong * cfg.amplitude;
        float sx = 1.0f + squash * 0.35f;
        float sy = 1.0f - squash * 0.30f;
        poseStack.scale(sx, sy, sx);
    }

    private boolean shouldBounce(LivingEntityRenderState state, BounceConfig cfg) {
        if (cfg.enableAllEntities) return true;

        EntityType<?> type = state.entityType;

        if (cfg.enableAnimals && isAnimal(type)) return true;
        if (cfg.enableHostile && isMonster(type)) return true;
        if (cfg.enablePlayers && type == EntityTypes.PLAYER) return true;

        return false;
    }

    private boolean isAnimal(EntityType<?> type) {
        // Жители и странствующие торговцы — тоже "животные" для нашей категории
        if (type == EntityTypes.VILLAGER || type == EntityTypes.WANDERING_TRADER) return true;
        return type == EntityTypes.COW || type == EntityTypes.PIG || type == EntityTypes.SHEEP
            || type == EntityTypes.CHICKEN || type == EntityTypes.HORSE;
    }

    private boolean isMonster(EntityType<?> type) {
        return type == EntityTypes.ZOMBIE || type == EntityTypes.SKELETON
            || type == EntityTypes.CREEPER || type == EntityTypes.SPIDER;
    }
}