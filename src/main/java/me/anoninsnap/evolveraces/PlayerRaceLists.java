package me.anoninsnap.evolveraces;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRaceLists {
	private static Map<String, Map<Player, Integer>> activeRaces;
	private static Map<String, Map<Player, Integer>> otherRaces;

	private PlayerRaceLists() {
		if (!loadFromConfig()) {
			activeRaces = new HashMap<>();
			otherRaces = new HashMap<>();
		}
	}

	public static void init() {
		new PlayerRaceLists();
		if (activeRaces.isEmpty()) {
			activeRaces.put("human", new HashMap<>());
			activeRaces.put("vampire", new HashMap<>());
		}
		if (otherRaces.isEmpty()) {
			otherRaces.putAll(activeRaces);
		}
	}

	private boolean loadFromConfig() {

		return false;
	}


	/**
	 * Set the Race of a player into a list for track keeping
	 *
	 * @param player  The Player which is to be removed from their current active Race, and swapped to a new one
	 * @param newRace The Race which the Player should be set to
	 * @return False if Race does not exist
	 */
	public static boolean setRace(Player player, String newRace) {
		// Remove Player from current Race
		if (!activeRaces.containsKey(newRace) || !otherRaces.containsKey(newRace)) {
			return false;
		}

		// Loop through all Races until the Current Player Race is found
		for (String race : activeRaces.keySet()) {
			if (activeRaces.get(race).containsKey(player)) {
				if (race.equals(newRace)) {
					return true;
				}
				Integer level = activeRaces.get(race).get(player);
				otherRaces.get(race).replace(player, level);
				activeRaces.get(race).remove(player);
				break;
			}
		}

		// Default Level for a Race
		int level = 0;


		// Check for player level
		if (otherRaces.get(newRace).containsKey(player)) {
			level = otherRaces.get(newRace).get(player);
		}

		// Add player to Race List
		activeRaces.get(newRace).put(player, level);
		return true;
	}

	/**
	 * Get the current active Race for a given Player
	 *
	 * @param player Player whose Race is asked for
	 * @return Race if found. null otherwise
	 */
	public static String getPlayerRace(Player player) {
		for (String race : activeRaces.keySet()) {
			if (activeRaces.get(race).containsKey(player)) {
				return race;
			}
		}
		return null;
	}
}
