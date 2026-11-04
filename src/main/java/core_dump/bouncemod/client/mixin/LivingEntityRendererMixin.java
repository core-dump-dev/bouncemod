package core_dump.bouncemod.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import core_dump.bouncemod.client.BounceAnimation;
import core_dump.bouncemod.client.BounceConfig;
import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
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
        if (!BounceAnimation.isInRange(state.x, state.y, state.z, mc, cfg.range)) return;
        if (!shouldBounce(state.entityType, cfg)) return;

        float squash = BounceAnimation.getSquash(mc, cfg);
        float sx = Math.max(0.2f, 1.0f + squash * 0.25f);
        float sy = Math.max(0.2f, 1.0f - squash * 0.25f);
        poseStack.scale(sx, sy, sx);
    }

    private boolean shouldBounce(EntityType<?> type, BounceConfig cfg) {
        // 1. Декоративные (Стойки для брони и Манекены)
        if (type == EntityTypes.ARMOR_STAND || type == EntityTypes.MANNEQUIN) {
            return cfg.enableArmorStands;
        }
        
        // 2. Игроки
        if (type == EntityTypes.PLAYER) return cfg.enablePlayers;
        
        // 3. Помощники (Големы, жители, торговцы)
        if (type == EntityTypes.ALLAY || type == EntityTypes.COPPER_GOLEM || type == EntityTypes.IRON_GOLEM 
            || type == EntityTypes.SNOW_GOLEM || type == EntityTypes.VILLAGER || type == EntityTypes.WANDERING_TRADER) {
            return cfg.enableUtility;
        }
        
        // 4. Остальные категории
        if (isPassive(type)) return cfg.enablePassive;
        if (isHostile(type)) return cfg.enableHostile;
        if (isNeutral(type)) return cfg.enableNeutral;
        if (isBoss(type)) return cfg.enableBosses;
        if (isVehicle(type)) return cfg.enableVehicles;
        return false;
    }

    private boolean isPassive(EntityType<?> type) {
        return type == EntityTypes.ARMADILLO || type == EntityTypes.AXOLOTL || type == EntityTypes.BAT
            || type == EntityTypes.CAMEL || type == EntityTypes.CAMEL_HUSK || type == EntityTypes.CAT
            || type == EntityTypes.CHICKEN || type == EntityTypes.COD || type == EntityTypes.COW
            || type == EntityTypes.DONKEY || type == EntityTypes.FOX || type == EntityTypes.FROG
            || type == EntityTypes.GIANT || type == EntityTypes.GLOW_SQUID || type == EntityTypes.HAPPY_GHAST
            || type == EntityTypes.HORSE || type == EntityTypes.MOOSHROOM || type == EntityTypes.MULE
            || type == EntityTypes.OCELOT || type == EntityTypes.PARROT || type == EntityTypes.PIG
            || type == EntityTypes.RABBIT || type == EntityTypes.SALMON || type == EntityTypes.SHEEP
            || type == EntityTypes.SKELETON_HORSE || type == EntityTypes.SNIFFER || type == EntityTypes.SQUID
            || type == EntityTypes.STRIDER || type == EntityTypes.SULFUR_CUBE || type == EntityTypes.TADPOLE
            || type == EntityTypes.TROPICAL_FISH || type == EntityTypes.TURTLE || type == EntityTypes.ZOMBIE_HORSE;
    }

    private boolean isHostile(EntityType<?> type) {
        return type == EntityTypes.BLAZE || type == EntityTypes.BOGGED || type == EntityTypes.BREEZE
            || type == EntityTypes.CAVE_SPIDER || type == EntityTypes.CREAKING || type == EntityTypes.CREEPER
            || type == EntityTypes.DROWNED || type == EntityTypes.ELDER_GUARDIAN || type == EntityTypes.ENDERMITE
            || type == EntityTypes.EVOKER || type == EntityTypes.GHAST || type == EntityTypes.GUARDIAN
            || type == EntityTypes.HOGLIN || type == EntityTypes.HUSK || type == EntityTypes.ILLUSIONER
            || type == EntityTypes.MAGMA_CUBE || type == EntityTypes.PARCHED || type == EntityTypes.PHANTOM
            || type == EntityTypes.PIGLIN_BRUTE || type == EntityTypes.PILLAGER || type == EntityTypes.RAVAGER
            || type == EntityTypes.SHULKER || type == EntityTypes.SILVERFISH || type == EntityTypes.SKELETON
            || type == EntityTypes.SLIME || type == EntityTypes.SPIDER || type == EntityTypes.STRAY
            || type == EntityTypes.VEX || type == EntityTypes.VINDICATOR || type == EntityTypes.WARDEN
            || type == EntityTypes.WITCH || type == EntityTypes.WITHER_SKELETON || type == EntityTypes.ZOGLIN
            || type == EntityTypes.ZOMBIE || type == EntityTypes.ZOMBIE_VILLAGER;
    }

    private boolean isNeutral(EntityType<?> type) {
        return type == EntityTypes.BEE || type == EntityTypes.DOLPHIN || type == EntityTypes.ENDERMAN
            || type == EntityTypes.GOAT || type == EntityTypes.LLAMA || type == EntityTypes.NAUTILUS
            || type == EntityTypes.PANDA || type == EntityTypes.PIGLIN || type == EntityTypes.POLAR_BEAR
            || type == EntityTypes.PUFFERFISH || type == EntityTypes.TRADER_LLAMA || type == EntityTypes.WOLF
            || type == EntityTypes.ZOMBIE_NAUTILUS || type == EntityTypes.ZOMBIFIED_PIGLIN;
    }

    private boolean isBoss(EntityType<?> type) {
        return type == EntityTypes.ENDER_DRAGON || type == EntityTypes.WITHER;
    }

    private boolean isVehicle(EntityType<?> type) {
        String id = type.getDescriptionId();
        return id.contains("boat") || id.contains("raft") || id.contains("minecart");
    }
}