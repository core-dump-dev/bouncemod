package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import core_dump.bouncemod.client.CubicBezier;
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

        Vec3 pos = new Vec3(state.x, state.y, state.z);
        if (pos.distanceToSqr(mc.player.position()) > cfg.range * cfg.range) return;

        if (!shouldBounce(state.entityType, cfg)) return;

        float time = (mc.level.getGameTime() + mc.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 20.0f;
        float phase = (time / (60.0f / Math.max(1f, cfg.bpm))) % 1.0f;
        float squashVal = CubicBezier.sample(cfg.curve, phase);

        if (cfg.alternating && (state.entityType.hashCode() & 1) != 0) squashVal = 1f - squashVal;

        float squash = squashVal * cfg.amplitude;
        poseStack.scale(1.0f + squash * 0.35f, 1.0f - squash * 0.30f, 1.0f + squash * 0.35f);
    }

    private boolean shouldBounce(EntityType<?> type, BounceConfig cfg) {
        if (cfg.enablePlayers && type == EntityTypes.PLAYER) return true;
        if (cfg.enablePassive && isPassive(type)) return true;
        if (cfg.enableHostile && isHostile(type)) return true;
        if (cfg.enableNeutral && isNeutral(type)) return true;
        if (cfg.enableBosses && (type == EntityTypes.ENDER_DRAGON || type == EntityTypes.WITHER)) return true;
        return false;
    }

    private boolean isPassive(EntityType<?> type) {
        return type == EntityTypes.COW || type == EntityTypes.PIG || type == EntityTypes.SHEEP
            || type == EntityTypes.CHICKEN || type == EntityTypes.GOAT
            || type == EntityTypes.HORSE || type == EntityTypes.DONKEY || type == EntityTypes.MULE
            || type == EntityTypes.LLAMA || type == EntityTypes.STRIDER
            || type == EntityTypes.WOLF || type == EntityTypes.CAT || type == EntityTypes.PARROT
            || type == EntityTypes.VILLAGER || type == EntityTypes.WANDERING_TRADER
            || type == EntityTypes.IRON_GOLEM || type == EntityTypes.SNOW_GOLEM
            || type == EntityTypes.AXOLOTL || type == EntityTypes.DOLPHIN
            || type == EntityTypes.TURTLE || type == EntityTypes.COD
            || type == EntityTypes.SALMON || type == EntityTypes.TROPICAL_FISH
            || type == EntityTypes.PUFFERFISH;
    }

    private boolean isHostile(EntityType<?> type) {
        return type == EntityTypes.ZOMBIE || type == EntityTypes.HUSK || type == EntityTypes.DROWNED
            || type == EntityTypes.SKELETON || type == EntityTypes.STRAY || type == EntityTypes.ENDERMAN
            || type == EntityTypes.CREEPER || type == EntityTypes.WITCH
            || type == EntityTypes.SHULKER || type == EntityTypes.WARDEN
            || type == EntityTypes.PILLAGER || type == EntityTypes.EVOKER
            || type == EntityTypes.VINDICATOR || type == EntityTypes.RAVAGER
            || type == EntityTypes.SLIME || type == EntityTypes.MAGMA_CUBE; // Слизни добавлены
    }

    private boolean isNeutral(EntityType<?> type) {
        return type == EntityTypes.BEE || type == EntityTypes.ZOMBIFIED_PIGLIN
            || type == EntityTypes.SPIDER || type == EntityTypes.CAVE_SPIDER;
    }
}