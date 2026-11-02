package core_dump.bouncemod.client;

import core_dump.bouncemod.client.sound.BounceMusicSound;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BounceModClient implements ClientModInitializer {

    public static final String MOD_ID = "bounce-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath(MOD_ID, "main"));

    public static KeyMapping toggleKey;
    public static KeyMapping settingsKey;

    private static BounceMusicSound musicSound;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[BounceMod] Init");
        BounceState.reload();

        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.bouncemod.toggle", InputConstants.KEY_F2, CATEGORY));

        settingsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.bouncemod.settings", InputConstants.KEY_F1, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        LOGGER.info("[BounceMod] Ready");
    }

    private void onClientTick(Minecraft client) {
        // Если игрок не в мире — принудительно останавливаем музыку
        if (client.player == null || client.level == null) {
            stopMusic(client);
            return;
        }

        while (toggleKey.consumeClick()) {
            BounceState.toggle();
            syncMusic(client);
            if (client.player != null) {
                client.player.sendSystemMessage(Component.translatable(
                        BounceState.enabled
                                ? "text.bouncemod.toggle.on"
                                : "text.bouncemod.toggle.off"));
            }
        }
        while (settingsKey.consumeClick()) {
            client.gui.setScreen(BounceConfigScreen.create(client.gui.screen()));
        }

        if (BounceState.enabled) {
            // Останавливаем фоновую музыку Minecraft, чтобы не было конфликта
            client.getMusicManager().stopPlaying();
            syncMusic(client);
        } else {
            stopMusic(client);
        }
    }

    private static void syncMusic(Minecraft client) {
        if (client == null || client.getSoundManager() == null) return;
        SoundManager sm = client.getSoundManager();

        if (musicSound == null) musicSound = new BounceMusicSound();

        // Пересоздаём звук, если громкость изменилась (чтобы применилось сразу)
        if (Math.abs(musicSound.getVolume() - BounceState.config.musicVolume) > 0.001f) {
            sm.stop(musicSound);
            musicSound = new BounceMusicSound();
        }

        if (!sm.isActive(musicSound)) sm.play(musicSound);
    }

    private static void stopMusic(Minecraft client) {
        if (client == null || client.getSoundManager() == null) return;
        if (musicSound != null && client.getSoundManager().isActive(musicSound)) {
            client.getSoundManager().stop(musicSound);
        }
    }
}