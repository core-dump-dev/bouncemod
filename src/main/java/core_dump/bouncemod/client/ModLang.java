package core_dump.bouncemod.client;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Локализация интерфейса мода с возможностью переопределить язык
 * независимо от языка игры.
 *
 * AUTO  -> используем Component.translatable (язык игры)
 * EN/RU -> используем захардкоженные строки через Component.literal
 */
public final class ModLang {

    private static final Map<String, String> EN = new HashMap<>();
    private static final Map<String, String> RU = new HashMap<>();

    static {
        // ===== ENGLISH =====
        EN.put("text.bouncemod.screen.title", "Squash Mod Settings");
        EN.put("text.bouncemod.category.general", "General");
        EN.put("text.bouncemod.category.targets", "Targets");

        EN.put("text.bouncemod.language", "Language");
        EN.put("text.bouncemod.language.tooltip",
                "Language of this settings screen. 'Auto' follows the game language.");

        EN.put("text.bouncemod.bpm", "BPM");
        EN.put("text.bouncemod.bpm.tooltip", "Beats per minute — how fast entities squash.");

        EN.put("text.bouncemod.amplitude", "Squash Amount");
        EN.put("text.bouncemod.amplitude.tooltip", "How strong the squash is. 0 = off, 2 = extreme.");

        EN.put("text.bouncemod.alternating", "Alternating Phase");
        EN.put("text.bouncemod.alternating.tooltip", "Neighbouring entities squash in opposite phase.");

        EN.put("text.bouncemod.volume", "Music Volume");
        EN.put("text.bouncemod.volume.tooltip", "Volume of the bounce music (0 to 1).");

        EN.put("text.bouncemod.range", "Range (blocks)");
        EN.put("text.bouncemod.range.tooltip", "Max distance for entities to squash.");

        EN.put("text.bouncemod.animals", "Animals");
        EN.put("text.bouncemod.hostile", "Hostile Mobs");
        EN.put("text.bouncemod.players", "Players");
        EN.put("text.bouncemod.all", "All Entities (override)");
        EN.put("text.bouncemod.all.tooltip",
                "If on, every living entity squashes regardless of the categories above.");

        // ===== RUSSIAN =====
        RU.put("text.bouncemod.screen.title", "Настройки Squash Mod");
        RU.put("text.bouncemod.category.general", "Основное");
        RU.put("text.bouncemod.category.targets", "Цели");

        RU.put("text.bouncemod.language", "Язык интерфейса");
        RU.put("text.bouncemod.language.tooltip",
                "Язык этого экрана настроек. «Авто» — язык игры.");

        RU.put("text.bouncemod.bpm", "BPM");
        RU.put("text.bouncemod.bpm.tooltip", "Ударов в минуту — как быстро сущности сжимаются.");

        RU.put("text.bouncemod.amplitude", "Сила сжатия");
        RU.put("text.bouncemod.amplitude.tooltip", "Насколько сильно сжимается модель. 0 — выкл, 2 — максимум.");

        RU.put("text.bouncemod.alternating", "Чередование фазы");
        RU.put("text.bouncemod.alternating.tooltip", "Соседние сущности сжимаются в противоположной фазе.");

        RU.put("text.bouncemod.volume", "Громкость музыки");
        RU.put("text.bouncemod.volume.tooltip", "Громкость музыки (от 0 до 1).");

        RU.put("text.bouncemod.range", "Дальность (блоки)");
        RU.put("text.bouncemod.range.tooltip", "Максимальное расстояние, на котором сущности сжимаются.");

        RU.put("text.bouncemod.animals", "Животные");
        RU.put("text.bouncemod.hostile", "Враждебные мобы");
        RU.put("text.bouncemod.players", "Игроки");
        RU.put("text.bouncemod.all", "Все сущности (переопределяет)");
        RU.put("text.bouncemod.all.tooltip",
                "Если включено, сжимаются все живые сущности, независимо от галочек выше.");
    }

    private ModLang() {}

    /** Возвращает Component с учётом выбранного в конфиге языка. */
    public static Component t(String key) {
        ModLanguage lang = BounceState.config.language;
        if (lang == null || lang == ModLanguage.AUTO) {
            return Component.translatable(key);
        }
        Map<String, String> map = (lang == ModLanguage.RU) ? RU : EN;
        return Component.literal(map.getOrDefault(key, key));
    }

    /** Отображаемое имя языка для дропдауна. */
    public static String displayName(ModLanguage lang) {
        return switch (lang) {
            case AUTO -> "Auto / Авто";
            case EN   -> "English";
            case RU   -> "Русский";
        };
    }
}