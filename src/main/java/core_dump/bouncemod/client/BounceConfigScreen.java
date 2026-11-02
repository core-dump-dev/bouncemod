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
                .setTitle(Component.translatable("text.bouncemod.screen.title"))
                .setSavingRunnable(() -> {
                    BounceState.config = cfg;
                    cfg.save();
                });

        ConfigEntryBuilder eb = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(
                Component.translatable("text.bouncemod.category.general"));

        general.addEntry(eb.startFloatField(Component.translatable("text.bouncemod.bpm"), cfg.bpm)
                .setDefaultValue(120f).setMin(30f).setMax(300f)
                .setTooltip(Component.translatable("text.bouncemod.bpm.tooltip"))
                .setSaveConsumer(v -> cfg.bpm = v).build());

        general.addEntry(eb.startFloatField(Component.translatable("text.bouncemod.amplitude"), cfg.amplitude)
                .setDefaultValue(0.9f).setMin(0f).setMax(2f)
                .setTooltip(Component.translatable("text.bouncemod.amplitude.tooltip"))
                .setSaveConsumer(v -> cfg.amplitude = v).build());

        general.addEntry(eb.startBooleanToggle(Component.translatable("text.bouncemod.alternating"), cfg.alternating)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("text.bouncemod.alternating.tooltip"))
                .setSaveConsumer(v -> cfg.alternating = v).build());

        general.addEntry(eb.startFloatField(Component.translatable("text.bouncemod.volume"), cfg.musicVolume)
                .setDefaultValue(0.7f).setMin(0f).setMax(1f)
                .setTooltip(Component.translatable("text.bouncemod.volume.tooltip"))
                .setSaveConsumer(v -> cfg.musicVolume = v).build());

        general.addEntry(eb.startFloatField(Component.translatable("text.bouncemod.range"), cfg.range)
                .setDefaultValue(32f).setMin(4f).setMax(128f)
                .setTooltip(Component.translatable("text.bouncemod.range.tooltip"))
                .setSaveConsumer(v -> cfg.range = v).build());

        ConfigCategory targets = builder.getOrCreateCategory(
                Component.translatable("text.bouncemod.category.targets"));

        targets.addEntry(eb.startBooleanToggle(Component.translatable("text.bouncemod.animals"), cfg.enableAnimals)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableAnimals = v).build());
        targets.addEntry(eb.startBooleanToggle(Component.translatable("text.bouncemod.hostile"), cfg.enableHostile)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enableHostile = v).build());
        targets.addEntry(eb.startBooleanToggle(Component.translatable("text.bouncemod.players"), cfg.enablePlayers)
                .setDefaultValue(true).setSaveConsumer(v -> cfg.enablePlayers = v).build());
        targets.addEntry(eb.startBooleanToggle(Component.translatable("text.bouncemod.all"), cfg.enableAllEntities)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("text.bouncemod.all.tooltip"))
                .setSaveConsumer(v -> cfg.enableAllEntities = v).build());

        return builder.build();
    }
}