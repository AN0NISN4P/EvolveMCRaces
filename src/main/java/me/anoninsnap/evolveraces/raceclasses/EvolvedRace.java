package me.anoninsnap.evolveraces.raceclasses;

import me.anoninsnap.evolveraces.effects.CustomEffect;
import me.anoninsnap.evolveraces.effects.CustomEffectType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EvolvedRace {
	private Player player;
	private String raceName;
	private int raceLevel;
	private int baseHealth;
	private int baseMovementSpeed;
	List<Map<String, ?>> levelUpgrades;

	private Map<CustomEffectType, Integer> activeCustomEffect;
	private Map<PotionEffectType, Integer> activePotionEffect;

	/**
	 * Creates a base Race without attached Player or Race. <br>
	 * Loads race from Config and automatically sets the levelUpgrades to be easily obtainable
	 *
	 * @param race   Name of the Race to be made
	 * @param config Map containing Health, Speed, and Levels (See Config for more)
	 */
	public EvolvedRace(String race, LinkedHashMap<String, ?> config) {
		player = null;
		raceName = race;
		raceLevel = 0;
		baseHealth = (int) config.get("BaseHealth");
		baseMovementSpeed = (int) config.get("BaseMovementSpeed");
		levelUpgrades = new ArrayList<>();
		for (LinkedHashMap<Integer, ?> levels : (List<LinkedHashMap<Integer, ?>>) config.get("Levels")) {
			levelUpgrades.add((Map<String, ?>) levels.get(Integer.parseInt(levels.keySet().toArray()[0].toString())));
		}

		activeCustomEffect = new HashMap<>();
		activePotionEffect = new HashMap<>();
	}

	public EvolvedRace(Player player, String raceName, int raceLevel, int baseHealth, int baseMovementSpeed, List<Map<String, ?>> levelUpgrades) {
		this.player = player;
		this.raceName = raceName;
		this.raceLevel = raceLevel;
		this.baseHealth = baseHealth;
		this.baseMovementSpeed = baseMovementSpeed;
		this.levelUpgrades = levelUpgrades;
		activeCustomEffect = new HashMap<>();
		activePotionEffect = new HashMap<>();
	}

	public String getName() {
		return raceName.toLowerCase();
	}

	/**
	 * Applies the effects described in the config file, everytime the timeinterval has passed
	 *
	 * @param interval Interval between effects being applied
	 */
	public void applyEffects(int interval) {
		for (CustomEffectType customEffectType : activeCustomEffect.keySet()) {
			CustomEffect.apply(player, customEffectType, interval + 5, activeCustomEffect.get(customEffectType));
		}
		for (PotionEffectType potionEffectType : activePotionEffect.keySet()) {
			CustomEffect.apply(player, potionEffectType, interval + 5, activePotionEffect.get(potionEffectType));
		}
	}

	/**
	 * Creates a copy of the selected Race and sets it to the correct Level before returning
	 * @param player Player to be attached to the Race
	 * @param level Level to set the player to
	 * @return EvolvedRace of same type with level specified in method arguments
	 */
	public EvolvedRace cloneToPlayer(Player player, Integer level) {
		EvolvedRace clone = cloneToPlayer(player);
		clone.levelUp(level);
		return clone;
	}

	/**
	 * Creates a copy of the selected Race and returns it
	 * @param player Player to be attached to the Race
	 * @return EvolvedRace of the same type, level 1
	 */
	public EvolvedRace cloneToPlayer(Player player) {
		return new EvolvedRace(player, raceName, raceLevel, baseHealth, baseMovementSpeed, levelUpgrades);
	}


	public void levelUp() {
		levelUp(1);

//		Enum.valueOf(CustomEffectType.class, "BURN");

	}

	public void levelUp(int levels) {
//		ConsoleLogger.debugLog(ChatColor.YELLOW + levelUpgrades.keySet().toString());
//		ConsoleLogger.debugLog(ChatColor.YELLOW + levelUpgrades.entrySet().toString());
//		List<LinkedHashMap> buffs = levelUpgrades.get("Buffs");
//		List<LinkedHashMap> effects = levelUpgrades.get("Effects");
	}


	@Override
	public String toString() {
		return ChatColor.BLUE + "\t" + raceName + "\n" +
				ChatColor.YELLOW + "Health: " + ChatColor.RED + baseHealth + "\n" +
				ChatColor.YELLOW + "Speed:  " + ChatColor.RED + baseMovementSpeed + "\n" +
				ChatColor.YELLOW + "First Level Effects\n" +
				ChatColor.AQUA + levelUpgrades.get(0);
	}
}
