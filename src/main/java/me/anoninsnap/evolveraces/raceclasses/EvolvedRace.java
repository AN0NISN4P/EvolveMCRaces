package me.anoninsnap.evolveraces.raceclasses;

import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.effects.CustomBuffType;
import me.anoninsnap.evolveraces.effects.CustomEffect;
import me.anoninsnap.evolveraces.effects.CustomEffectType;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class EvolvedRace {
	private Player player;
	private String raceName;
	private int raceLevel;
	private double baseHealth;
	private float baseMovementSpeed;
	List<Map<String, List<?>>> levelUpgrades;

	private Map<CustomEffectType, Integer> activeCustomEffect;
	private Map<PotionEffectType, Integer> activePotionEffect;

	private boolean active;

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
		baseHealth = Double.parseDouble(config.get("BaseHealth").toString());
		baseMovementSpeed = Float.parseFloat(config.get("BaseMovementSpeed").toString());
		levelUpgrades = new ArrayList<>();
		for (LinkedHashMap<Integer, ?> levels : (List<LinkedHashMap<Integer, ?>>) config.get("Levels")) {
			levelUpgrades.add((Map<String, List<?>>) levels.get(Integer.parseInt(levels.keySet().toArray()[0].toString())));
		}
		active = false;
		activeCustomEffect = new HashMap<>();
		activePotionEffect = new HashMap<>();
	}

	public EvolvedRace(Player player, String raceName, double baseHealth, float baseMovementSpeed, List<Map<String, List<?>>> levelUpgrades) {
		this.player = player;
		this.raceName = raceName;
		this.raceLevel = 0;
		this.baseHealth = baseHealth;
		this.baseMovementSpeed = baseMovementSpeed;
		this.levelUpgrades = levelUpgrades;
		activeCustomEffect = new HashMap<>();
		activePotionEffect = new HashMap<>();
		active = false;
	}

	public void init() {
		active = true;
		applyStats();
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
	 *
	 * @param player Player to be attached to the Race
	 * @param level  Level to set the player to
	 * @return EvolvedRace of same type with level specified in method arguments
	 */
	public EvolvedRace cloneToPlayer(Player player, Integer level) {
		EvolvedRace clone = new EvolvedRace(player, raceName, baseHealth, baseMovementSpeed, levelUpgrades);
		clone.levelUp(level);
		return clone;
	}

	/**
	 * Creates a copy of the selected Race and returns it
	 *
	 * @param player Player to be attached to the Race
	 * @return EvolvedRace of the same type, level 1
	 */
	public EvolvedRace cloneToPlayer(Player player) {
		return cloneToPlayer(player, 1);
	}


	public void levelUp() {
		levelUp(1);
	}

	public void levelUp(int levels) {
		ConsoleLogger.debugLog("Level: " + ChatColor.DARK_GREEN + raceLevel);

		// Set level to the actual level
		raceLevel += levels;

		// Clear Active Effect Lists
		activePotionEffect.clear();
		activeCustomEffect.clear();

		// Get Buffs and Effects from Config
		List<LinkedHashMap<String, ?>> buffs = (List<LinkedHashMap<String, ?>>) levelUpgrades.get(raceLevel - 1).get("Buffs");
		List<LinkedHashMap<String, ?>> effects = (List<LinkedHashMap<String, ?>>) levelUpgrades.get(raceLevel - 1).get("Effects");

		ConsoleLogger.debugLog("Level: " + ChatColor.GREEN + raceLevel);

		// Check for buffs
		if (buffs != null && buffs.size() != 0) {
			for (LinkedHashMap<String, ?> buff : buffs) {
				handleBuff((String) buff.get("buff"), (int) buff.get("amount"));
			}

		}
		// Check for effects
		if (effects != null && effects.size() != 0) {
			// Loop through all Effects and apply if possible
			for (LinkedHashMap<String, ?> effect : effects) {
				// Check if Effect Type is a Vanilla Potion
				ConsoleLogger.debugLog(effect.entrySet().toString());
				if (PotionEffectType.getByName((String) effect.get("effect")) != null) {
					activePotionEffect.put(PotionEffectType.getByName((String) effect.get("effect")), (Integer) effect.get("level"));
				} else {
					// Attempt to add a Custom Effect to the Custom Effect List
					try {
						activeCustomEffect.put(Enum.valueOf(CustomEffectType.class, (String) effect.get("effect")), (Integer) effect.get("level"));
					} catch (Exception e) {
						ConsoleLogger.warningLog("Effect could not be found: " + effect.get("effect"));
					}
				}
			}
		}
		// If Race is active, apply stats
		if (active) {
			applyStats();
		}
	}

	/**
	 * Handles the buff changes to a character
	 *
	 * @param buffAction String matching Enum for BuffActions
	 * @param integer    Amount over Base Stats
	 */
	private void handleBuff(String buffAction, Integer integer) {
		switch (Enum.valueOf(CustomBuffType.class, buffAction)) {
			case HEALTH_UP -> baseHealth += integer;
			case MOVEMENT_SPEED_UP -> baseMovementSpeed += integer;
		}
	}

	/**
	 * Applies the Race Stats to the Player
	 */
	private void applyStats() {
		if (player.getMaxHealth() > baseHealth) {
			// HP goes down
			player.setHealth(baseHealth);
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHealth);
		} else {
			// HP goes up
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHealth);
			player.setHealth(baseHealth);
		}

		player.setWalkSpeed(baseMovementSpeed);
	}


	@Override
	public String toString() {
		return ChatColor.BLUE + "\t" + raceName + "\n" +
				ChatColor.YELLOW + "Health: " + ChatColor.RED + baseHealth + "\n" +
				ChatColor.YELLOW + "Speed:  " + ChatColor.RED + baseMovementSpeed + "\n" +
				ChatColor.YELLOW + "First Level Effects\n" +
				ChatColor.AQUA + levelUpgrades.get(0);
	}

	/**
	 * Disables the Race, setting it as the Inactive Race
	 */
	public void disable() {
		player.setHealthScale(20);
		active = false;
	}

	/**
	 * Sets the Race Level 1 below the specified value, then runs the levelUp() method to reach the correct Level
	 *
	 * @param lvl Level to set the Race to
	 */
	public void setLevel(int lvl) {
		assert player != null;
		raceLevel = lvl - 1;
		levelUp();
	}
}
