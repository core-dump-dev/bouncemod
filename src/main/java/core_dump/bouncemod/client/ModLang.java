package core_dump.bouncemod.client;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public final class ModLang {

    private static final Map<String, String> EN = new HashMap<>();
    private static final Map<String, String> RU = new HashMap<>();

    static {
        // ===== ENGLISH =====
        EN.put("text.bouncemod.screen.title", "Squash Mod Settings");
        EN.put("text.bouncemod.category.general", "General");
        EN.put("text.bouncemod.category.targets", "Targets");

        EN.put("text.bouncemod.language", "Interface Language");
        EN.put("text.bouncemod.language.tooltip", "Language of this screen. 'Auto' follows the game language. (Reopen the screen to apply!)");

        EN.put("text.bouncemod.bpm", "BPM");
        EN.put("text.bouncemod.bpm.tooltip", "Beats per minute - how fast entities squash.");

        EN.put("text.bouncemod.amplitude", "Squash Amount");
        EN.put("text.bouncemod.amplitude.tooltip", "How strong the squash is. 0 = off, 5 = extreme.");

        EN.put("text.bouncemod.curve", "Squash Curve");

        EN.put("text.bouncemod.volume", "Music Volume");
        EN.put("text.bouncemod.volume.tooltip", "Volume of the bounce music (0 to 1).");

        EN.put("text.bouncemod.range", "Range (blocks)");
        EN.put("text.bouncemod.range.tooltip", "Max distance for entities to squash.");

        EN.put("text.bouncemod.players", "Players");
        EN.put("text.bouncemod.passive", "Passive & Friendly");
        EN.put("text.bouncemod.hostile", "Hostile");
        EN.put("text.bouncemod.neutral", "Neutral");
        EN.put("text.bouncemod.bosses", "Bosses");
        EN.put("text.bouncemod.utility", "Utility (Golems, Villagers, Mannequin)");
        EN.put("text.bouncemod.vehicles", "Vehicles");
        EN.put("text.bouncemod.armor_stands", "Armor Stands & Mannequins");
        EN.put("text.bouncemod.hand_item", "Held Item");

        EN.put("text.bouncemod.toggle.on", "Squash: ON");
        EN.put("text.bouncemod.toggle.off", "Squash: OFF");

        // ===== RUSSIAN =====
        RU.put("text.bouncemod.screen.title", "Настройки Squash Mod");
        RU.put("text.bouncemod.category.general", "Основное");
        RU.put("text.bouncemod.category.targets", "Цели");

        RU.put("text.bouncemod.language", "Язык интерфейса");
        RU.put("text.bouncemod.language.tooltip", "Язык этого экрана. «Авто» — язык игры. (Переоткройте экран, чтобы применить!)");

        RU.put("text.bouncemod.bpm", "BPM");
        RU.put("text.bouncemod.bpm.tooltip", "Ударов в минуту — как быстро сущности сжимаются.");

        RU.put("text.bouncemod.amplitude", "Сила сжатия");
        RU.put("text.bouncemod.amplitude.tooltip", "Насколько сильно сжимается модель. 0 — выкл, 5 — максимум.");

        RU.put("text.bouncemod.curve", "Кривая сжатия");

        RU.put("text.bouncemod.volume", "Громкость музыки");
        RU.put("text.bouncemod.volume.tooltip", "Громкость музыки (от 0 до 1).");

        RU.put("text.bouncemod.range", "Дальность (блоки)");
        RU.put("text.bouncemod.range.tooltip", "Максимальное расстояние, на котором сущности сжимаются.");

        RU.put("text.bouncemod.players", "Игроки");
        RU.put("text.bouncemod.passive", "Пассивные и дружелюбные");
        RU.put("text.bouncemod.hostile", "Враждебные");
        RU.put("text.bouncemod.neutral", "Нейтральные");
        RU.put("text.bouncemod.bosses", "Боссы");
        RU.put("text.bouncemod.utility", "Помощники (големы, жители, манекен)");
        RU.put("text.bouncemod.vehicles", "Транспорт");
        RU.put("text.bouncemod.armor_stands", "Стойки и манекены");
        RU.put("text.bouncemod.hand_item", "Предмет в руке");

        RU.put("text.bouncemod.toggle.on", "Squash: ВКЛ");
        RU.put("text.bouncemod.toggle.off", "Squash: ВЫКЛ");
    }

    private ModLang() {}

    public static Component t(String key) {
        ModLanguage lang = BounceState.config.language;
        if (lang == null || lang == ModLanguage.AUTO) {
            return Component.translatable(key);
        }
        Map<String, String> map = (lang == ModLanguage.RU) ? RU : EN;
        return Component.literal(map.getOrDefault(key, key));
    }

    public static String displayName(ModLanguage lang) {
        return switch (lang) {
            case AUTO -> "Auto / Авто";
            case EN   -> "English";
            case RU   -> "Русский";
        };
    }
}