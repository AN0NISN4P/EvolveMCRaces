package me.anoninsnap.evolveraces;

import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.raceclasses.EvolvedRace;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerRaceLists {
	private static ConfigurationSection config;
	private static ArrayList<LinkedHashMap<String, LinkedHashMap<String, ?>>> raceDefinitions;
	private static Map<String, EvolvedRace> allPossibleRaces;
	private static Map<UUID, EvolvedRace> currentPlayerRace;
	private static Map<UUID, Map<String, EvolvedRace>> storedPlayerRaces;


	/**
	 * Constructor for the PlayerRaceLists Class. This class is responsible for keeping track of Players' Races, both active and non-active races. <br>
	 * On Startup, the Races will be loaded from the Config File <i>(as Empty Vessels)</i> and stored in a List, which will be used for copying over to each player if the player isn't attached to a Race of that type yet
	 *
	 * @param raceConfig The Configuration Section, <b>RaceDefinitions</b>
	 */
	public PlayerRaceLists(ConfigurationSection raceConfig) {
		try {
			config = raceConfig;
			raceDefinitions = (ArrayList) config.get("Races");
		} catch (Exception e) {
			ConsoleLogger.warningLog("Races could not be loaded!");
			return;
		}

		allPossibleRaces = new HashMap<>();
		loadRaces();


		currentPlayerRace = new HashMap<>();
		storedPlayerRaces = new HashMap<>();
	}

	/**
	 * Checks if a player has any Race stored on the server
	 *
	 * @param player Player to check if has a Race
	 * @return True if the player was found with any race (Valid or not), false otherwise
	 */
	public static boolean hasAnyRace(Player player) {
		return storedPlayerRaces.containsKey(player.getUniqueId());
	}

	/**
	 * Get all stored races of a single player in a terribly formatted string
	 *
	 * @param player Player whose race is desired
	 * @return All races for the desired player with terrible formatting
	 */
	public static String getStoredRaces(Player player) {
		if (hasAnyRace(player)) {
			return storedPlayerRaces.get(player.getUniqueId()).toString();
		} else {
			return "No Race found";
		}
	}

	/**
	 * Get all the stored races as a String with terrible formatting (if any)
	 *
	 * @return Horribly formatted String of all Races stored. Including the UUID used as key for each race bunch
	 */
	public static String getStoredRaces() {
		return storedPlayerRaces.toString();
	}

	/**
	 * Loads all races from the Config given to the Constructor. Stores the empty Races in a list from which they can be copied from later
	 */
	public void loadRaces() {
		for (LinkedHashMap<String, LinkedHashMap<String, ?>> raceDefinition : raceDefinitions) {
			String raceName = raceDefinition.keySet().toArray()[0].toString();
			EvolvedRace evolvedRace = new EvolvedRace(raceName, raceDefinition.get(raceName));
			ConsoleLogger.debugLog(evolvedRace.toString());
			allPossibleRaces.put(raceName.toLowerCase(), evolvedRace);
		}
	}

	/**
	 * Creates a lvl 0 copy of all Races and stores them int the StoredRace list
	 *
	 * @param player Player to attach to each race
	 */
	public static void defaultRaces(Player player) {
		HashMap<String, EvolvedRace> tmp = new HashMap<>();
		for (String raceName : allPossibleRaces.keySet()) {
			tmp.put(raceName, allPossibleRaces.get(raceName).cloneToPlayer(player));
		}
		storedPlayerRaces.put(player.getUniqueId(), tmp);
	}

	/**
	 * Creates a lvl 0 copy of the specified Race and stores it as a storedPlayerRace.
	 *
	 * @param player   Player to create a Race for
	 * @param raceName Race to be created
	 */
	private static EvolvedRace defaultRace(Player player, String raceName) {
		ConsoleLogger.debugLog(ChatColor.DARK_BLUE + "Creating race: " + ChatColor.BLUE + raceName);
		ConsoleLogger.debugLog(ChatColor.DARK_AQUA + "Available Races: " + ChatColor.AQUA + allPossibleRaces);

		storedPlayerRaces.putIfAbsent(player.getUniqueId(), new HashMap<>());
		storedPlayerRaces.get(player.getUniqueId()).putIfAbsent(raceName, allPossibleRaces.get(raceName).cloneToPlayer(player));
		return storedPlayerRaces.get(player.getUniqueId()).get(raceName);
	}

	/**
	 * Get the currently active Race of a Player
	 *
	 * @param player Player whose Race you want
	 * @return Race of the player specified
	 */
	public static EvolvedRace getPlayerRace(Player player) {
		return currentPlayerRace.getOrDefault(player.getUniqueId(), null);
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
		UUID playerID = player.getUniqueId();
		if (!allPossibleRaces.containsKey(race) || !player.isOnline()) {
			return false;
		}

		// Check if player is currently assigned to a Race
		if (currentPlayerRace.containsKey(playerID) && !currentPlayerRace.get(playerID).getName().equalsIgnoreCase(race)) {
			// PLAYER HAS CURRENT RACE

			// Temporarily save current race
			EvolvedRace curr = currentPlayerRace.get(playerID);

			// Check for stored race to replace
			if (storedPlayerRaces.containsKey(playerID) && storedPlayerRaces.get(playerID).containsKey(race)) {
				// PLAYER HAS THE REQUESTED RACE STORED

				// Replace current race with stored race
				currentPlayerRace.replace(playerID, storedPlayerRaces.get(playerID).get(race));
			} else {
				// PLAYER DOES NOT HAVE THE REQUESTED RACE STORED

				// Replace current race with default new race
				currentPlayerRace.replace(playerID, defaultRace(player, race));
			}
			// Store temp race
			storedPlayerRaces.get(playerID).replace(race, curr);
		} else {
			// PLAYER HAS NO CURRENT RACE

			// Check if player has the requested race stored
			if (storedPlayerRaces.containsKey(playerID) && storedPlayerRaces.get(playerID).containsKey(race)) {
				// PLAYER HAS THE REQUESTED RACE STORED

				// Put current race as the requested race
				currentPlayerRace.put(playerID, storedPlayerRaces.get(playerID).get(race));
			} else {
				// PLAYER DOES NOT HAVE THE REQUESTED RACE STORED

				// Put current race as default new race
				currentPlayerRace.put(playerID, defaultRace(player, race));
			}
		}

		return true;
	}

	/**
	 * Copies the current Player's Race into the StoredPlayerRace List
	 *
	 * @param player Player whose race is to be stored
	 */
	public static void storeCurrentRace(Player player) {
		UUID playerID = player.getUniqueId();
		if (currentPlayerRace.containsKey(playerID)) {
			storedPlayerRaces.get(playerID).replace(currentPlayerRace.get(playerID).getName(), currentPlayerRace.get(playerID));
		}
	}

	/**
	 * Get all the loaded Races listed in a long String. Formatting is defined by the EvolvedRaces class
	 *
	 * @return All Races in an Unformatted String
	 */
	public static Set<String> getAllRaces() {
		return allPossibleRaces.keySet();
	}
}
