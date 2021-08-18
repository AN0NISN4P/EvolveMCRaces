package me.anoninsnap.evolveraces.timedevents;

import me.anoninsnap.evolveraces.EvolveRaces;
import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.effects.CustomEffect;
import me.anoninsnap.evolveraces.effects.CustomEffectType;
import me.anoninsnap.evolveraces.raceclasses.EvolvedRace;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerTimedEffects extends BukkitRunnable implements Listener {
	private final Plugin plugin;
	private final Server server;
	private List<Player> onlinePlayers;

	private int timeInterval;

	// All Players
	private List<Material> pathBlocks = new ArrayList<>();

	private int pathEffectLevel;
	private PotionEffectType pathEffect;

	// Race Specifics
	private int vampireBurnThreshold;

	public PlayerTimedEffects(EvolveRaces plugin) {
		onlinePlayers = new ArrayList<>();
		this.plugin = plugin;
		server = plugin.getServer();
		onlinePlayers.addAll(server.getOnlinePlayers());
		server.getPluginManager().registerEvents(this, plugin);

		loadFromConfig(plugin.getConfig());
	}

	public void loadFromConfig(FileConfiguration config) {
		// Load In Configs
		ConfigurationSection worldConfig = config.getConfigurationSection("World");
		ConfigurationSection paths = worldConfig.getConfigurationSection("Path");
		ConfigurationSection races = worldConfig.getConfigurationSection("Race");

		// World Config
		timeInterval = worldConfig.getInt("TimeBetweenTicks");

		// Path Config
		pathEffect = PotionEffectType.getByName(paths.getString("PotionEffect"));
		pathEffectLevel = paths.getInt("EffectLevel");
		for (Object block : paths.getList("Blocks")) {
			pathBlocks.add(Material.valueOf(block.toString()));
		}

		// Race Config
		vampireBurnThreshold = races.getConfigurationSection("Vampire").getInt("BurnThreshold");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		onlinePlayers.add(e.getPlayer());
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		onlinePlayers.remove(e.getPlayer());
	}

	@Override
	public void run() {
		for (Player player : onlinePlayers) {
			addRaceCustomEffects(player);
			addRacePotionEffects(player);
			addGeneralEffects(player);
		}
	}

	private void addGeneralEffects(Player player) {
		Block blockUnderPlayer = player.getLocation().add(0, -0.7d, 0).getBlock();
		if (pathBlocks.contains(blockUnderPlayer.getType())) {
			CustomEffect.apply(player, pathEffect, timeInterval + 5, pathEffectLevel);
		}
	}

	public void addRaceCustomEffects(Player player) {
		EvolvedRace race = PlayerRaceLists.getPlayerRace(player);
		if (race == null) {
			return;
		} else if (race.getName().equalsIgnoreCase("vampire")) {
			byte skyLightLevel = player.getLocation().getBlock().getLightFromSky();
			if (skyLightLevel > vampireBurnThreshold) {
				CustomEffect.apply(player, CustomEffectType.BURN, 40, 0);
			}
		}
	}

	public void addRacePotionEffects(Player player) {
		EvolvedRace race = PlayerRaceLists.getPlayerRace(player);
		if (race == null) {
			return;
		} else if (race.getName().equalsIgnoreCase("vampire")) {
			CustomEffect.apply(player, PotionEffectType.INCREASE_DAMAGE, timeInterval + 5, 1);
		}
	}

}
