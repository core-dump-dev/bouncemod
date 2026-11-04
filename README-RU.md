<h3 align="center">
  <a href="https://github.com/core-dump-dev/bouncemod/blob/main/README.md">English</a> | 
  <a href="https://github.com/core-dump-dev/bouncemod/blob/main/README-RU.md">Русский</a>
</h3>

# Bounce Mod (Squash)

Клиентский мод на Fabric для Minecraft 26.3, который привносит мем «Bouncing Yaris» (Подпрыгивающая Ярис) в игру.

Все живые сущности, транспорт и даже предмет в руке могут сжиматься и растягиваться в такт музыке.

## Особенности
- **Синхронизация с музыкой:** Сущности сжимаются и растягиваются под кастомный BPM (по умолчанию 135).
- **Гибкая настройка сжатия:** Регулировка силы сжатия (до 5.0) и выбор кривой (Hard Squash, Ease In-Out).
- **Интеграция с музыкой:** Мод проигрывает свою музыку и автоматически ставит на паузу фоновую музыку Minecraft, пока эффект активен.
- **Категории целей:** Выбор групп, на которые действует эффект:
  - Игроки
  - Пассивные и дружелюбные мобы
  - Враждебные мобы
  - Нейтральные мобы
  - Боссы
  - Помощники (Големы, жители)
  - Транспорт (Лодки, вагонетки)
  - Стойки и манекены
  - Предмет в руке

## Управление
- **F1** — Открыть меню настроек.
- **F2** — Включить/выключить эффект сжатия и музыку.

## Требования
- Minecraft **26.3**
- [Fabric Loader](https://fabricmc.net/) (0.20.1 или новее)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Cloth Config API](https://modrinth.com/mod/cloth-config)
- (Опционально) [Mod Menu](https://modrinth.com/mod/modmenu) для доступа к настройкам из списка модов.

## Авторы и вдохновение
- **Идея и аудио:** Взяты из видео с мемом «Bouncing Yaris»: [https://www.youtube.com/watch?v=hSN6-AMdDOo](https://www.youtube.com/watch?v=hSN6-AMdDOo)
- **Оригинальная музыка:** Niko B - "Why's this dealer?" [https://www.youtube.com/watch?v=SerTJpflwMM](https://www.youtube.com/watch?v=SerTJpflwMM)

## Сборка из исходников

Если вы хотите собрать мод самостоятельно, вы можете использовать приложенный скрипт `build.bat` (для Windows).

1. Убедитесь, что у вас установлен **JDK 25**.
2. Запустите `build.bat` двойным кликом (или через терминал).
3. Скрипт автоматически:
   - Остановит запущенные процессы Gradle.
   - Очистит папки `.gradle` и `build`.
   - Скомпилирует мод.
4. После успешной сборки `.jar` файл появится в папке:
   `build\libs\bounce-mod-1.0.0.jar`