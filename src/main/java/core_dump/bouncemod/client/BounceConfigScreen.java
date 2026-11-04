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

        // ============ GENERAL ============
        ConfigCategory general = builder.getOrCreateCategory(
                ModLang.t("text.bouncemod.category.general"));

        general.addEntry(eb.startEnumSelector(ModLang.t("text.bouncemod.language"), ModLanguage.class, cfg.language)
                .setDefaultValue(ModLanguage.AUTO)
                .setTooltip(ModLang.t("text.bouncemod.language.tooltip"))
                .setEnumNameProvider(e -> Component.literal(ModLang.displayName((ModLanguage) e)))
                .setSaveConsumer(v -> cfg.language = v).build());

        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.bpm"), (int) cfg.bpm, 30, 300)
                .setDefaultValue(135).setTooltip(ModLang.t("text.bouncemod.bpm.tooltip"))
                .setSaveConsumer(v -> cfg.bpm = v).build());

        // Сила сжатия (дефолт 200)
        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.amplitude"), (int) (cfg.amplitude * 100), 0, 500)
                .setDefaultValue(200).setTooltip(ModLang.t("text.bouncemod.amplitude.tooltip"))
                .setSaveConsumer(v -> cfg.amplitude = v / 100f).build());

        general.addEntry(eb.startEnumSelector(ModLang.t("text.bouncemod.curve"), SquashCurve.class, cfg.curve)
                .setDefaultValue(SquashCurve.HARD)
                .setEnumNameProvider(e -> Component.literal(((SquashCurve) e).display))
                .setSaveConsumer(v -> cfg.curve = v).build());

        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.volume"), (int) (cfg.musicVolume * 100), 0, 100)
                .setDefaultValue(20).setTooltip(ModLang.t("text.bouncemod.volume.tooltip"))
                .setSaveConsumer(v -> cfg.musicVolume = v / 100f).build());

        general.addEntry(eb.startIntSlider(ModLang.t("text.bouncemod.range"), (int) cfg.range, 4, 256)
                .setDefaultValue(64).setTooltip(ModLang.t("text.bouncemod.range.tooltip"))
                .setSaveConsumer(v -> cfg.range = v).build());

        // ============ TARGETS ============
        ConfigCategory targets = builder.getOrCreateCategory(
                ModLang.t("text.bouncemod.category.targets"));

        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.players"), cfg.enablePlayers)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enablePlayers = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.passive"), cfg.enablePassive)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enablePassive = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.hostile"), cfg.enableHostile)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableHostile = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.neutral"), cfg.enableNeutral)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableNeutral = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.bosses"), cfg.enableBosses)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableBosses = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.utility"), cfg.enableUtility)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableUtility = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.vehicles"), cfg.enableVehicles)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableVehicles = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.armor_stands"), cfg.enableArmorStands)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableArmorStands = v).build());
        targets.addEntry(eb.startBooleanToggle(ModLang.t("text.bouncemod.hand_item"), cfg.enableHandItem)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableHandItem = v).build());

        return builder.build();
    }
}