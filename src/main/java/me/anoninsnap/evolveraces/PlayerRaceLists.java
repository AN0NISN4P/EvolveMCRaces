package me.anoninsnap.evolveraces;

import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.raceclasses.EvolvedRace;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlayerRaceLists {
	private static ConfigurationSection config;
	private static Map<Player, EvolvedRace> currentPlayerRace;
	private static Map<Player, Map<String, EvolvedRace>> storedPlayerRaces;

	public PlayerRaceLists(ConfigurationSection raceConfig) {
		config = raceConfig;
		currentPlayerRace = new HashMap<>();
		storedPlayerRaces = new HashMap<>();
	}

	/**
	 * Checks if a Player has existing races stored
	 *
	 * @param player The Player being checked for an existing Race
	 * @return True if the Player has any race stored
	 */
	public static boolean checkForRaces(Player player) {
		return storedPlayerRaces.containsKey(player);
	}

	/**
	 * Creates default races for the player. These Races will be made from a Config File and all be set to lvl 0
	 *
	 * @param player Player used as Key for getting Races
	 */
	public static void defaultRaces(Player player) {
		List<LinkedHashMap<String, LinkedHashMap>> raceConfigs = (List<LinkedHashMap<String, LinkedHashMap>>) config.getList("Races");
		ConsoleLogger.debugLog("RaceConfig Length:      " + ChatColor.AQUA + raceConfigs.size());
		ConsoleLogger.debugLog("Key set from First Map: " + ChatColor.AQUA + raceConfigs.get(0).keySet());
		HashMap<String, EvolvedRace> defaultRaceMap = new HashMap<>();

		for (LinkedHashMap<String, LinkedHashMap> raceConfig : raceConfigs) {
			String raceName = raceConfig.keySet().iterator().next();
			defaultRaceMap.put(raceName, new EvolvedRace(player, raceName, raceConfig.get(raceName)));
		}
		storedPlayerRaces.put(player, defaultRaceMap);
	}

	private static EvolvedRace defaultRace(Player player, String raceName) {
		ConsoleLogger.debugLog(ChatColor.YELLOW + "Race being made: " + ChatColor.AQUA + raceName);
		ConsoleLogger.debugLog(ChatColor.YELLOW + "Race Info:       " + ChatColor.AQUA + config.get(raceName));
//		EvolvedRace race = new EvolvedRace(player, raceName, (LinkedHashMap) config.get(raceName));
		return null;
	}

	public static EvolvedRace getPlayerRace(Player player) {
		return currentPlayerRace.get(player);
	}


	/**
	 * If possible, stores the current race and loads a stored race.<br>
	 * If the desired Race does not exists as a stored version for the user, a default version will be created for them to use.
	 *
	 * @param player Player whose race is being changed
	 * @param race   The new race for the player
	 * @return True unless unexpectedly interrupted
	 */
	public static boolean setRace(Player player, String race) {
		// Check if player is currently assigned to a Race
		if (currentPlayerRace.containsKey(player)) {
			// PLAYER HAS CURRENT RACE

			// Temporarily save current race
			EvolvedRace curr = currentPlayerRace.get(player);

			// Check for stored race to replace
			if (storedPlayerRaces.get(player).containsKey(race)) {
				// PLAYER HAS THE REQUESTED RACE STORED

				// Replace current race with stored race
				currentPlayerRace.replace(player, storedPlayerRaces.get(player).get(race));
			} else {
				// PLAYER DOES NOT HAVE THE REQUESTED RACE STORED

				// Replace current race with default new race
				currentPlayerRace.replace(player, defaultRace(player, race));
			}
			// Store temp race
			storedPlayerRaces.get(player).replace(race, curr);
		} else {
			// PLAYER HAS NO CURRENT RACE

			// Check if player has the requested race stored
			if (storedPlayerRaces.get(player).containsKey(race)) {
				// PLAYER HAS THE REQUESTED RACE STORED

				// Put current race as the requested race
				currentPlayerRace.put(player, storedPlayerRaces.get(player).get(race));
			} else {
				// PLAYER DOES NOT HAVE THE REQUESTED RACE STORED

				// Put current race as default new race
				currentPlayerRace.put(player, defaultRace(player, race));
			}
		}

		return true;
	}
}
