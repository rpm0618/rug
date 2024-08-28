# Rug
A technical utility mod for Minecraft 1.8 (like carpet, but worse)

Very alpha quality

## Commands
- `/as`: Show the number of ticks until the next autosave
- `/chunkDebug`: Create logs of chunk events, or connect directly to a [viewer](https://github.com/rpm0618/mc_utils). The viewer must be running on the same computer as the minecraft server.
    - `/chunkDebug start`: Start recording chunk events
    - `/chunkDebug stop`: Stop recording chunk events and write them to a file
    - `/chunkDebug connect [port]`: Connect to the viewer linked above and show realtime chunk events
    - `/chunkDebug disconnect`: Stop sending realtime chunk events
- `/instantFall [<true|false>]`: Enable or disable instant fall (prints the current status when called with no arguments)
- `/instantScheduling [<true|false>]`: Enable or disable instant scheduling (prints the current status when called with no arguments)
