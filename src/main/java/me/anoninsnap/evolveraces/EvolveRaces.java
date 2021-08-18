package me.anoninsnap.evolveraces;

import me.anoninsnap.evolveraces.commands.ConsoleLogCommand;
import me.anoninsnap.evolveraces.commands.GetRaceCommand;
import me.anoninsnap.evolveraces.commands.SetRaceCommand;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.eventlisteners.PlayerHitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EvolveRaces extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic
		ConsoleLogger.logToConsole = true;


		PlayerRaceLists.init();
		getCommand("setrace").setExecutor(new SetRaceCommand());
		getCommand("getrace").setExecutor(new GetRaceCommand());
		getCommand("consoleLog").setExecutor(new ConsoleLogCommand());


		getServer().getPluginManager().registerEvents(new PlayerHitListener(), this);

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
