package core_dump.bouncemod.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class BounceConfigScreen {

    private BounceConfigScreen() {}

    public static Screen create(Screen parent) {
        BounceConfig cfg = BounceState.config;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(ModLang.t("text.bouncemod.screen.title"))
                .setSavingRunnable(() -> {
                    BounceState.config = cfg;
                    cfg.save();
                });

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(
                ModLang.t("text.bouncemod.category.general"));

        // ===== Language =====
        general.addEntry(eb.startEnumSelector(
                        ModLang.t("text.bouncemod.language"),
                        ModLanguage.class,
                        cfg.language)
                .setDefaultValue(ModLanguage.AUTO)
                .setTooltip(ModLang.t("text.bouncemod.language.tooltip"))
                .setEnumNameProvider(e -> Component.literal(ModLang.displayName((ModLanguage) e)))
                .setSaveConsumer(v -> cfg.language = v)
                .build());

        // ===== BPM (целый слайдер) =====
        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.bpm"),
                        (int) cfg.bpm, 30, 300)
                .setDefaultValue(135)
                .setTooltip(ModLang.t("text.bouncemod.bpm.tooltip"))
                .setSaveConsumer(v -> cfg.bpm = v)
                .build());

        // ===== Squash Amount (int 0..200, реальное значение = /100) =====
        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.amplitude"),
                        (int) (cfg.amplitude * 100), 0, 200)
                .setDefaultValue(90)
                .setTooltip(ModLang.t("text.bouncemod.amplitude.tooltip"))
                .setSaveConsumer(v -> cfg.amplitude = v / 100f)
                .build());

        // ===== Alternating =====
        general.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.alternating"),
                        cfg.alternating)
                .setDefaultValue(false)
                .setTooltip(ModLang.t("text.bouncemod.alternating.tooltip"))
                .setSaveConsumer(v -> cfg.alternating = v)
                .build());

        // ===== Volume (int 0..100, реальное значение = /100) =====
        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.volume"),
                        (int) (cfg.musicVolume * 100), 0, 100)
                .setDefaultValue(70)
                .setTooltip(ModLang.t("text.bouncemod.volume.tooltip"))
                .setSaveConsumer(v -> cfg.musicVolume = v / 100f)
                .build());

        // ===== Range (целый слайдер) =====
        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.range"),
                        (int) cfg.range, 4, 128)
                .setDefaultValue(32)
                .setTooltip(ModLang.t("text.bouncemod.range.tooltip"))
                .setSaveConsumer(v -> cfg.range = v)
                .build());

        // ===== Targets =====
        ConfigCategory targets = builder.getOrCreateCategory(
                ModLang.t("text.bouncemod.category.targets"));

        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.animals"),
                        cfg.enableAnimals)
                .setDefaultValue(true)
                .setSaveConsumer(v -> cfg.enableAnimals = v)
                .build());

        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.hostile"),
                        cfg.enableHostile)
                .setDefaultValue(true)
                .setSaveConsumer(v -> cfg.enableHostile = v)
                .build());

        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.players"),
                        cfg.enablePlayers)
                .setDefaultValue(true)
                .setSaveConsumer(v -> cfg.enablePlayers = v)
                .build());

        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.all"),
                        cfg.enableAllEntities)
                .setDefaultValue(false)
                .setTooltip(ModLang.t("text.bouncemod.all.tooltip"))
                .setSaveConsumer(v -> cfg.enableAllEntities = v)
                .build());

        return builder.build();
    }
}