package me.anoninsnap.evolveraces;

import me.anoninsnap.evolveraces.commands.ConsoleLogCommand;
import me.anoninsnap.evolveraces.commands.GetRaceCommand;
import me.anoninsnap.evolveraces.commands.SetRaceCommand;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.eventlisteners.PlayerHitListener;
import me.anoninsnap.evolveraces.timedevents.PlayerTimedEffects;
import org.bukkit.plugin.java.JavaPlugin;

public final class EvolveRaces extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic
		ConsoleLogger.logToConsole = true;

		// Load races
		PlayerRaceLists.init();

		// Default Config
		defaultConfigs();

		// Set Commands
		getCommand("setrace").setExecutor(new SetRaceCommand());
		getCommand("getrace").setExecutor(new GetRaceCommand());
		getCommand("consoleLog").setExecutor(new ConsoleLogCommand());

		// Start Recurring Events
		Runnable timedEvents = new PlayerTimedEffects(this);
		new PlayerHitListener(this);


		// Schedule Recurring Events
		getServer().getScheduler().scheduleSyncRepeatingTask(this, timedEvents, 10, 10); // TODO: Config File
	}

	private void defaultConfigs() {
		getConfig().options().copyDefaults();
		saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
