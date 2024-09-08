package net.rpm0618.rug.rules;

public class Rules {
	@RugRule(description = "Beacons give out asynchronous block updates when powered")
	public static boolean asyncBeaconUpdates = false;

	@RugRule(description = "Handle the most common async crash in the chunk map")
	public static boolean asyncCrashFix = false;

	@RugRule(description = "Print information on attempted dungeon population locations")
	public static boolean debugDungeonPopulation = false;

	@RugRule(description = "Print information on attempted liquid pocket population locations")
	public static boolean debugLiquidPopulation = false;

	@RugRule(description = "Updating a sponge block triggers update suppression")
	public static boolean updateSuppressionSponge = false;
}
