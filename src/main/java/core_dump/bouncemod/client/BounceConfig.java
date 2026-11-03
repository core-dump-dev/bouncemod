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
    public float musicVolume = 0.2f;
    public float range = 64.0f;

    public SquashCurve curve = SquashCurve.HARD;

    // --- Цели ---
    public boolean enablePlayers = true;
    public boolean enablePassive = true;
    public boolean enableHostile = true;
    public boolean enableNeutral = true;
    public boolean enableBosses = true;
    public boolean enableVehicles = true; // лодки, вагонетки
    public boolean enableArmorStands = true;
    public boolean enableHandItem = true; // предмет в руке
    public boolean enableItems = false;   // выпавшие предметы

    public static BounceConfig load() {
        if (Files.exists(PATH)) {
            try {
                BounceConfig cfg = GSON.fromJson(Files.readString(PATH), BounceConfig.class);
                if (cfg != null) {
                    if (cfg.language == null) cfg.language = ModLanguage.AUTO;
                    if (cfg.curve == null) cfg.curve = SquashCurve.HARD;
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