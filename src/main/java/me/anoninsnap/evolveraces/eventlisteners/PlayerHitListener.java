package me.anoninsnap.evolveraces.eventlisteners;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PlayerHitListener implements Listener {
	EntityType[] mobs = {EntityType.ZOMBIE, EntityType.SKELETON};
	List<EntityType> bonusDmgMobs = Arrays.asList(mobs);
	Entity e1;
	Entity e2;

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		// Getting Entities in the event
		e1 = event.getDamager();
		e2 = event.getEntity();

		// Verify it is a Player hitting a Player
		if (e1 instanceof Player attacker && e2 instanceof Player defender) {

			// Check player Race
			if (PlayerRaceLists.getPlayerRace(defender).equalsIgnoreCase("vampire")) {

				// Get Weapon
				ItemStack weapon = attacker.getInventory().getItemInMainHand();

				// Check Weapon
				if (weapon.isSimilar(new ItemStack(Material.WOODEN_SWORD))) {

					// Change DMG Amount
					double dmg = event.getDamage() * 1.2; // TODO: Config DMG Modifier
					event.setDamage(dmg);
				}
			}
		} else if (e1 instanceof Player player) {

			// Check Player is hitting a designated mob
			ItemStack heldItem = player.getInventory().getItemInMainHand();
			if (bonusDmgMobs.contains(e2.getType()) && heldItem != null) {

				// Check if Player is using the correct item
				Material heltItemType = heldItem.getData().getItemType();
				if (heltItemType.equals(Material.WOODEN_SWORD) || heltItemType.equals(Material.LEGACY_WOOD_SWORD)) { // FIXME: Legacy item? // TODO: Config Item Modifier

					// Change DMG Amount
					double dmg = event.getDamage() * 10.2; // TODO: Config DMG Modifier
					event.setDamage(dmg);
				}

			}
		}
	}
}
