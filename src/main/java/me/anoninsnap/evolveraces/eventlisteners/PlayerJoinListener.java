package me.anoninsnap.evolveraces.eventlisteners;

import me.anoninsnap.evolveraces.EvolveRaces;
import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	private EvolveRaces plugin;

	public PlayerJoinListener(EvolveRaces plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player joinedPlayer = e.getPlayer();

		joinedPlayer.setInvulnerable(false);

		boolean hasRace = PlayerRaceLists.hasAnyRace(joinedPlayer);
		if (!hasRace) {
			PlayerRaceLists.defaultRaces(joinedPlayer);
			joinedPlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Creating the Default Races for you");
		}
		ConsoleLogger.debugLog(joinedPlayer.getName() + " has Race: " + hasRace);
		joinedPlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You can pick a race with " + ChatColor.AQUA + "/race set <Race>");
	}
}
