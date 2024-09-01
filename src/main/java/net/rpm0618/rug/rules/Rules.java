package net.rpm0618.rug.rules;

public class Rules {
	@RugRule(description = "Beacons give out asynchronous block updates when powered")
	public static boolean asyncBeaconUpdates = false;

	@RugRule(description = "Print information on attempted dungeon population locations")
	public static boolean debugDungeonPopulation = false;

	@RugRule(description = "Print information on attempted liquid pocket population locations")
	public static boolean debugLiquidPopulation = false;
}
