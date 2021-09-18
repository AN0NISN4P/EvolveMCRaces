package me.anoninsnap.evolveraces.eventlisteners;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.raceclasses.EvolvedRace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR && event.getPlayer().isSneaking()) {
			Player p = event.getPlayer();
			EvolvedRace race = PlayerRaceLists.getPlayerRace(p);
			if (race == null) {
				return;
			}
			race.useAbility();
		}
	}
}
