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
import net.minecraft.world.entity.MobCategory;
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
        // 1. Декоративные
        if (type == EntityTypes.ARMOR_STAND || type == EntityTypes.MANNEQUIN) {
            return cfg.enableArmorStands;
        }

        // 2. Игроки
        if (type == EntityTypes.PLAYER) return cfg.enablePlayers;

        // 3. Боссы
        if (type == EntityTypes.ENDER_DRAGON || type == EntityTypes.WITHER) return cfg.enableBosses;

        // 4. Нейтральные (Minecraft не имеет отдельной категории NEUTRAL, поэтому задаем список вручную)
        if (type == EntityTypes.ENDERMAN || type == EntityTypes.PIGLIN || type == EntityTypes.ZOMBIFIED_PIGLIN
            || type == EntityTypes.BEE || type == EntityTypes.WOLF || type == EntityTypes.POLAR_BEAR
            || type == EntityTypes.PANDA || type == EntityTypes.GOAT || type == EntityTypes.DOLPHIN
            || type == EntityTypes.LLAMA || type == EntityTypes.TRADER_LLAMA || type == EntityTypes.NAUTILUS
            || type == EntityTypes.ZOMBIE_NAUTILUS) {
            return cfg.enableNeutral;
        }

        // 5. Остальные по стандартным категориям Minecraft (работает и для модов!)
        MobCategory category = type.getCategory();
        return switch (category) {
            case CREATURE, WATER_CREATURE, AMBIENT, WATER_AMBIENT, AXOLOTLS -> cfg.enablePassive;
            case MONSTER -> cfg.enableHostile;
            case MISC -> cfg.enableUtility; // Големы, жители и прочие
            default -> false;
        };
    }
}