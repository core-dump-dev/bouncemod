package core_dump.bouncemod.client;

import core_dump.bouncemod.client.sound.BounceMusicSound;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BounceModClient implements ClientModInitializer {

    public static final String MOD_ID = "bounce-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding toggleKey;
    public static KeyBinding settingsKey;

    private static BounceMusicSound musicSound;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[BounceMod] Init");
        BounceState.reload();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bouncemod.toggle", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F2, "category.bouncemod"));

        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bouncemod.settings", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F1, "category.bouncemod"));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        LOGGER.info("[BounceMod] Ready");
    }

    private void onClientTick(MinecraftClient client) {
        while (toggleKey.wasPressed()) {
            BounceState.toggle();
            syncMusic(client);
            if (client.player != null) {
                client.player.sendMessage(
                        Text.translatable(BounceState.enabled
                                ? "text.bouncemod.toggle.on"
                                : "text.bouncemod.toggle.off"), true);
            }
        }
        while (settingsKey.wasPressed()) {
            client.setScreen(BounceConfigScreen.create(client.currentScreen));
        }
        if (BounceState.enabled) syncMusic(client);
    }

    private static void syncMusic(MinecraftClient client) {
        if (client == null) return;
        SoundManager sm = client.getSoundManager();
        if (sm == null) return;
        if (BounceState.enabled) {
            if (musicSound == null) musicSound = new BounceMusicSound();
            if (!sm.isPlaying(musicSound)) sm.play(musicSound);
        } else if (musicSound != null && sm.isPlaying(musicSound)) {
            sm.stop(musicSound);
        }
    }
}