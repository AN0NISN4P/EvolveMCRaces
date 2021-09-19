package me.anoninsnap.evolveraces.eventlisteners;

import me.anoninsnap.evolveraces.EvolveRaces;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FallDmgListener implements Listener {
	private EvolveRaces plugin;

	public FallDmgListener(EvolveRaces plugin){
		this.plugin = plugin;
	}

	@EventHandler public void onPlayerFallDmg(EntityDamageEvent event){
		if (event.getEntity() instanceof Player p){
			if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
				if (EvolveRaces.noFallDmgList.contains(p)){
					event.setDamage(2d);
					EvolveRaces.noFallDmgList.remove(p);
				}
			}
		}
	}
}
