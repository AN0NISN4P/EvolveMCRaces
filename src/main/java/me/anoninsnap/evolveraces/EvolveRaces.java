package me.anoninsnap.evolveraces;

import me.anoninsnap.evolveraces.commands.GetRaceCommand;
import me.anoninsnap.evolveraces.commands.SetRaceCommand;
import me.anoninsnap.evolveraces.eventlisteners.PlayerHitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class EvolveRaces extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic

		PlayerRaceLists.init();
		getCommand("setrace").setExecutor(new SetRaceCommand());
		getCommand("getrace").setExecutor(new GetRaceCommand());


		getServer().getPluginManager().registerEvents(new PlayerHitListener(), this);

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
