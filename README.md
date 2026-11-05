<h3 align="center">
  <a href="https://github.com/core-dump-dev/bouncemod/blob/main/README.md">English</a> | 
  <a href="https://github.com/core-dump-dev/bouncemod/blob/main/README-RU.md">Русский</a>
</h3>

# Bounce Mod (Squash)

A client-side Fabric mod for Minecraft 26.3 that brings the "Bouncing Yaris" meme into the game. 

All living entities, vehicles, and even the item in your hand can squash and stretch to the beat of the music.

<img src="media\preview.gif" alt="Bounce Mod Preview" width="600">


## Features
- **Sync to music:** Entities squash and stretch to a custom BPM (default 135).
- **Customizable Squash:** Adjust the squash amount (up to 5.0) and choose between different squash curves (Hard Squash, Ease In-Out).
- **Music integration:** The mod plays its own music and automatically pauses the vanilla Minecraft background music while the effect is active.
- **Target Categories:** Choose exactly which groups are affected:
  - Players
  - Passive & Friendly Mobs
  - Hostile Mobs
  - Neutral Mobs
  - Bosses
  - Utility (Golems, Villagers)
  - Vehicles (Boats, Minecarts)
  - Armor Stands & Mannequins
  - Held Item (First-person view)

## Controls
- **F1** — Open the configuration screen.
- **F2** — Toggle the squash effect and music on/off.

## Requirements
- Minecraft **26.3**
- [Fabric Loader](https://fabricmc.net/) (0.20.1 or newer)
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Cloth Config API](https://modrinth.com/mod/cloth-config)
- (Optional) [Mod Menu](https://modrinth.com/mod/modmenu) to access settings from the mod list.

## Credits & Inspiration
- **Idea & Audio:** Taken from the "Bouncing Yaris" meme video: [https://www.youtube.com/watch?v=hSN6-AMdDOo](https://www.youtube.com/watch?v=hSN6-AMdDOo)
- **Original Music:** Niko B - "Why's this dealer?" [https://www.youtube.com/watch?v=SerTJpflwMM](https://www.youtube.com/watch?v=SerTJpflwMM)

## Building from source

If you want to build the mod yourself, you can use the included `build.bat` script (for Windows).

1. Make sure you have **JDK 25** installed.
2. Double-click `build.bat` (or run it from the terminal).
3. The script will automatically:
   - Stop any running Gradle daemons.
   - Clean the `.gradle` and `build` folders.
   - Compile the mod.
4. Once the build is successful, the `.jar` file will appear in:
   `build\libs\bounce-mod-1.0.0.jar`