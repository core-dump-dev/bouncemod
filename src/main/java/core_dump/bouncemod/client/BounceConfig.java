package core_dump.bouncemod.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BounceConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH =
            FabricLoader.getInstance().getConfigDir().resolve("bouncemod.json");

    public ModLanguage language = ModLanguage.AUTO;

    public float bpm = 135.0f;
    public float amplitude = 0.9f;
    public boolean alternating = false;
    public float musicVolume = 0.7f;
    public float range = 32.0f;

    public boolean enableAnimals = true;
    public boolean enableHostile = true;
    public boolean enablePlayers = true;
    public boolean enableAllEntities = false;

    public static BounceConfig load() {
        if (Files.exists(PATH)) {
            try {
                BounceConfig cfg = GSON.fromJson(Files.readString(PATH), BounceConfig.class);
                if (cfg != null) {
                    if (cfg.language == null) cfg.language = ModLanguage.AUTO;
                    return cfg;
                }
            } catch (IOException e) {
                BounceModClient.LOGGER.error("Failed to read config", e);
            }
        }
        BounceConfig cfg = new BounceConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(this));
        } catch (IOException e) {
            BounceModClient.LOGGER.error("Failed to save config", e);
        }
    }
}