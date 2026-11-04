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

        // Стабильное чередование: чётные/нечётные по hash кода типа
        if (cfg.alternating && (state.entityType.hashCode() & 1) != 0) {
            squashVal = 1f - squashVal;
        }

        // Ограничиваем squash, чтобы модели не уходили в минус
        float squash = Math.max(-1f, Math.min(1f, squashVal * cfg.amplitude));

        // Если squash отрицательный — растягиваем, если положительный — сжимаем
        float sx = 1.0f + squash * 0.35f;
        float sy = 1.0f - squash * 0.30f;
        poseStack.scale(sx, sy, sx);
    }

    private boolean shouldBounce(EntityType<?> type, BounceConfig cfg) {
        if (cfg.enablePlayers && type == EntityTypes.PLAYER) return true;
        if (cfg.enablePassive && isPassive(type)) return true;
        if (cfg.enableHostile && isHostile(type)) return true;
        if (cfg.enableBosses && (type == EntityTypes.ENDER_DRAGON || type == EntityTypes.WITHER)) return true;
        if (cfg.enableVehicles && isVehicle(type)) return true;
        if (cfg.enableArmorStands && type == EntityTypes.ARMOR_STAND) return true;
        return false;
    }

    private boolean isPassive(EntityType<?> type) {
        // Животные, жители, големы, водные, летучие мыши, помощники
        return type == EntityTypes.ALLAY
            || type == EntityTypes.ARMADILLO
            || type == EntityTypes.AXOLOTL
            || type == EntityTypes.BAT
            || type == EntityTypes.BEE
            || type == EntityTypes.CAMEL
            || type == EntityTypes.CAT
            || type == EntityTypes.CHICKEN
            || type == EntityTypes.COD
            || type == EntityTypes.COPPER_GOLEM
            || type == EntityTypes.COW
            || type == EntityTypes.DOLPHIN
            || type == EntityTypes.DONKEY
            || type == EntityTypes.FOX
            || type == EntityTypes.FROG
            || type == EntityTypes.GLOW_SQUID
            || type == EntityTypes.GOAT
            || type == EntityTypes.HAPPY_GHAST
            || type == EntityTypes.HORSE
            || type == EntityTypes.IRON_GOLEM
            || type == EntityTypes.LLAMA
            || type == EntityTypes.MOOSHROOM
            || type == EntityTypes.MULE
            || type == EntityTypes.NAUTILUS
            || type == EntityTypes.OCELOT
            || type == EntityTypes.PANDA
            || type == EntityTypes.PARROT
            || type == EntityTypes.PIG
            || type == EntityTypes.POLAR_BEAR
            || type == EntityTypes.PUFFERFISH
            || type == EntityTypes.RABBIT
            || type == EntityTypes.SALMON
            || type == EntityTypes.SHEEP
            || type == EntityTypes.SNIFFER
            || type == EntityTypes.SNOW_GOLEM
            || type == EntityTypes.SQUID
            || type == EntityTypes.STRIDER
            || type == EntityTypes.SULFUR_CUBE
            || type == EntityTypes.TADPOLE
            || type == EntityTypes.TRADER_LLAMA
            || type == EntityTypes.TROPICAL_FISH
            || type == EntityTypes.TURTLE
            || type == EntityTypes.VILLAGER
            || type == EntityTypes.WANDERING_TRADER;
    }

    private boolean isHostile(EntityType<?> type) {
        // Монстры, слизни, магмовые кубы, боссы (кроме Дракона и Визера)
        return type == EntityTypes.BLAZE
            || type == EntityTypes.BOGGED
            || type == EntityTypes.BREEZE
            || type == EntityTypes.CAVE_SPIDER
            || type == EntityTypes.CREAKING
            || type == EntityTypes.CREEPER
            || type == EntityTypes.DROWNED
            || type == EntityTypes.ELDER_GUARDIAN
            || type == EntityTypes.ENDERMAN
            || type == EntityTypes.ENDERMITE
            || type == EntityTypes.EVOKER
            || type == EntityTypes.GHAST
            || type == EntityTypes.GUARDIAN
            || type == EntityTypes.HOGLIN
            || type == EntityTypes.HUSK
            || type == EntityTypes.ILLUSIONER
            || type == EntityTypes.MAGMA_CUBE
            || type == EntityTypes.PARCHED
            || type == EntityTypes.PHANTOM
            || type == EntityTypes.PIGLIN
            || type == EntityTypes.PIGLIN_BRUTE
            || type == EntityTypes.PILLAGER
            || type == EntityTypes.RAVAGER
            || type == EntityTypes.SHULKER
            || type == EntityTypes.SILVERFISH
            || type == EntityTypes.SKELETON
            || type == EntityTypes.SLIME
            || type == EntityTypes.STRAY
            || type == EntityTypes.VEX
            || type == EntityTypes.VINDICATOR
            || type == EntityTypes.WARDEN
            || type == EntityTypes.WITCH
            || type == EntityTypes.WITHER_SKELETON
            || type == EntityTypes.ZOGLIN
            || type == EntityTypes.ZOMBIE
            || type == EntityTypes.ZOMBIE_NAUTILUS
            || type == EntityTypes.ZOMBIE_VILLAGER
            || type == EntityTypes.ZOMBIFIED_PIGLIN;
    }

    private boolean isVehicle(EntityType<?> type) {
        String id = type.getDescriptionId();
        return id.contains("boat") || id.contains("raft") || id.contains("minecart");
    }
}