package core_dump.bouncemod.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
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
    public float amplitude = 2.0f;
    public float musicVolume = 0.2f;
    public float range = 64.0f;
    public SquashCurve curve = SquashCurve.HARD;

    // Entity categories
    public boolean enablePlayers = true;
    public boolean enablePassive = true;
    public boolean enableHostile = true;
    public boolean enableNeutral = true;
    public boolean enableBosses = true;
    public boolean enableUtility = true;
    public boolean enableVehicles = true;
    public boolean enableArmorStands = true;
    public boolean enableHandItem = true;

    public static BounceConfig load() {
        if (Files.exists(PATH)) {
            try {
                BounceConfig cfg = GSON.fromJson(Files.readString(PATH), BounceConfig.class);
                if (cfg != null) {
                    cfg.validate();
                    return cfg;
                }
            } catch (IOException | JsonParseException e) {
                BounceModClient.LOGGER.error("Failed to read config; using defaults", e);
            }
        }

        BounceConfig cfg = new BounceConfig();
        cfg.save();
        return cfg;
    }

    /** Restores missing enum values and clamps numeric settings to supported ranges. */
    public void validate() {
        if (language == null) language = ModLanguage.AUTO;
        if (curve == null) curve = SquashCurve.HARD;

        bpm = clampFinite(bpm, 135.0f, 30.0f, 300.0f);
        amplitude = clampFinite(amplitude, 2.0f, 0.0f, 5.0f);
        musicVolume = clampFinite(musicVolume, 0.2f, 0.0f, 1.0f);
        range = clampFinite(range, 64.0f, 4.0f, 256.0f);
    }

    private static float clampFinite(float value, float fallback, float min, float max) {
        if (!Float.isFinite(value)) {
            return fallback;
        }
        return Math.max(min, Math.min(max, value));
    }

    public void save() {
        validate();
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(this));
        } catch (IOException e) {
            BounceModClient.LOGGER.error("Failed to save config", e);
        }
    }
}
