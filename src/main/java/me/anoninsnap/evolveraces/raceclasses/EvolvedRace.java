package me.anoninsnap.evolveraces.raceclasses;

import me.anoninsnap.evolveraces.effects.CustomEffectType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedHashMap;
import java.util.Map;

public class EvolvedRace {
	private Player player;
	private String raceName;
	private int raceLevel;
	private int baseHealth;
	private int baseMovementSpeed;

	private Map<Integer, Map<PotionEffectType, Integer>> vanillaEffectsEachLevel;
	private Map<Integer, Map<CustomEffectType, Integer>> customEffectsEachLevel;

	public EvolvedRace(Player player, String race, LinkedHashMap config) {
		this.player = player;
		raceName = race;
		raceLevel = 0;
		baseHealth = (int) config.get("BaseHealth");
		baseMovementSpeed = (int) config.get("BaseMovementSpeed");

		player.sendMessage(ChatColor.GOLD + "You can join the " + ChatColor.YELLOW + raceName + ChatColor.GOLD + " which has these stats:");
		player.sendMessage(ChatColor.DARK_RED + "HP: " + ChatColor.WHITE + baseHealth);
		player.sendMessage(ChatColor.AQUA + "SPEED: " + ChatColor.WHITE + baseMovementSpeed);
	}

	public String getName() {
		return raceName.toLowerCase();
	}

	public void applyEffects() {

	}

}
