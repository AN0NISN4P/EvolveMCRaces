package me.anoninsnap.evolveraces;

import me.anoninsnap.evolveraces.commands.CommandManager;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.eventlisteners.PlayerHitListener;
import me.anoninsnap.evolveraces.eventlisteners.PlayerJoinListener;
import me.anoninsnap.evolveraces.timedevents.PlayerTimedEffects;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class EvolveRaces extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic

		// Default Config
		defaultConfigs();

		// Load races
		new PlayerRaceLists(getConfig().getConfigurationSection("RaceDefinitions"));

		// Set Commands
		getCommand("race").setExecutor(new CommandManager());

		// Start Recurring Events
		Runnable timedEvents = new PlayerTimedEffects(this);
		new PlayerHitListener(this);

		// Events
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

		// Schedule Recurring Events
		getServer().getScheduler().scheduleSyncRepeatingTask(this, timedEvents, 10, getConfig().getConfigurationSection("World").getInt("TimeBetweenTicks"));

		// Confirm the plugin is Up and Running
		ConsoleLogger.debugLog(ChatColor.DARK_GREEN + "EvolveMC Races has Started");
	}

	private void defaultConfigs() {
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		ConsoleLogger.LOG_TO_CONSOLE = getConfig().getBoolean("DebugConsole");
		ConsoleLogger.debugLog(ChatColor.GREEN + "Logging to Console: " + ConsoleLogger.LOG_TO_CONSOLE);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
