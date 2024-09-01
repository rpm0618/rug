# Rug
A technical utility mod for Minecraft 1.8.8 (like carpet, but worse)

Very alpha quality

## Commands
- `/as`: Show the number of ticks until the next autosave
- `/chunkDebug <start|stop|connect [port]|disconnect>`: Create logs of chunk events, or connect directly to a [viewer](https://github.com/rpm0618/mc_utils). The viewer must be running on the same computer as the minecraft server.
  - `/chunkDebug start`: Start recording chunk events
  - `/chunkDebug stop`: Stop recording chunk events and write them to a file
  - `/chunkDebug connect [port]`: Connect to the viewer linked above and show realtime chunk events
  - `/chunkDebug disconnect`: Stop sending realtime chunk events
- `/hash <dump|info [x z]>`: Get information about the chunk hashmap
  - `/hash dump`: Create a dump of the chunk hashmap
  - `/hash info [x z]`: Information about the position of the provided (or current, if no coordinates are provided) chunk in the hashmap
- `/instantFall [<true|false>]`: Enable or disable instant fall (prints the current status when called with no arguments)
- `/instantScheduling [<true|false>]`: Enable or disable instant scheduling (prints the current status when called with no arguments)
- `/lagspike <durationMs>`: Created a lag spike for the requested duration
- `/regenerate [x z]`: Regenerate the given chunk (or the one the player is currently in)
- `/repopulate [x z] [<now|async>]`: Schedule the given chunk to be repopulated. If `now` is passed, repopulate the chunk immediately. If `async` is passed, repopulate on an asynchronous thread.
- `/rug [<list|Rule Name [Rule Value]>]`: View and adjust rules to modify game behavior
  - `/rug`: Display rules that are currently changed from default
  - `/rug list`: Display all rules
  - `/rug <Rule Name>`: Display information about the give rule
  - `/rug <Rule Name> <Rule Value>`: Set the rule to the given value

## Rules
- `asyncBeaconUpdates <true|false>`: If true, beacon blocks give our asynchronous updates when redstone powered.
- `debugDungeonPopulation <true|false>`: If true, print information on attempted dungeon population locations.
- `debugLiquidPopulation <true|false>`: If true, print information on attempted liquid pocket population locations.

## Installation
Use the [ornithe](https://ornithemc.net/) installer to install Minecraft 1.8.8 using Fabric as the mod loader (Installation is similar to fabric in later versions). 
Run Minecraft (using `fabric-server-launch.jar` if a server) once, then place the jar for this mod and the latest version of [OSL](https://modrinth.com/mod/osl) in the mods folder.

## Development
With a recent JDK installed, `./gradlew idea` or `./gradlew eclipse` (`gradlew.bat` on Windows) will generate project files for the respective IDEs.

If you don't want to use and IDE, you can use `./gradlew runClient` or `./gradlew runServer` to run changes as you're developing, and `./gradlew build` to build the final jar.
