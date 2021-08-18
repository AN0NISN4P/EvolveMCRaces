package me.anoninsnap.evolveraces.eventlisteners;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws InterruptedException {
		Player joinedPlayer = e.getPlayer();
		boolean hasRaces = PlayerRaceLists.checkForRaces(joinedPlayer);
		if (!hasRaces) {
			PlayerRaceLists.defaultRaces(joinedPlayer);
			Thread.sleep(1000);
			joinedPlayer.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You can select a race with /race");
		}
	}
}
