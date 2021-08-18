package me.anoninsnap.evolveraces.eventlisteners;

import me.anoninsnap.evolveraces.EvolveRaces;
import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerHitListener implements Listener {
	// Entity Variables
	private Entity e1;
	private Entity e2;

	// Affected Mobs
	private static Set<EntityType> bonusDmgMobs = new HashSet<>();

	// Damage Values
	private static double dmgModifierPvP;
	private static double dmgModifierPvE;
	private static double holyStarBonus;
	private static double resistanceModifier;

	public PlayerHitListener(EvolveRaces plugin) {
		FileConfiguration config = plugin.getConfig();
		ConfigurationSection combatConfig = config.getConfigurationSection("Combat");

		// Damage Values loaded from Config
		dmgModifierPvP = combatConfig.getConfigurationSection("PvP").getDouble("DmgModifier");
		resistanceModifier = combatConfig.getConfigurationSection("PvP").getDouble("ResistanceModifier");
		dmgModifierPvE = combatConfig.getConfigurationSection("PvE").getDouble("DmgModifier");
		holyStarBonus = combatConfig.getDouble("HolyStarBonus");

		// Mob List loaded from Config
		List<?> mobList = combatConfig.getConfigurationSection("PvE").getList("MobList");
		for (Object mob : mobList) {
			bonusDmgMobs.add(EntityType.valueOf(mob.toString()));
		}

		// EventListener Registration
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		// Getting Entities in the event
		e1 = event.getDamager();
		e2 = event.getEntity();

		// Check if Player is hitting an entity
		if (e1 instanceof Player attacker) {
			if (e2 instanceof Player defender) {
				// PLAYER HITTING PLAYER

				// Check player Race
				String defenderRace = PlayerRaceLists.getPlayerRace(defender);
				if (defenderRace != null && defenderRace.equalsIgnoreCase("vampire")) {

					// Check if hit lands
					boolean successfulHit = playerHitVampire(attacker, defender, event);

					// Notify attacker, the vampire has been weakened
					if (successfulHit) {
						attacker.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "The Vampire has been Weakened."); // TODO: Special Class for telling players + Config Settings?
					}
				}
			} else if (e2 instanceof Mob mob) {
				// PLAYER HITTING MOB

				// Getting held item
				ItemStack heldItem = getHeldItems(attacker)[0];

				// Check if mob is supposed to take bonus dmg, and verifyPlayer is holding an item
				if (bonusDmgMobs.contains(mob.getType()) && heldItem != null) {


					// Check if Player is using the correct item
					Material heltItemType = heldItem.getData().getItemType();
					if (heltItemType.equals(Material.WOODEN_SWORD) || heltItemType.equals(Material.LEGACY_WOOD_SWORD)) { // FIXME: Legacy item? // TODO: Config Item Modifier

						// Modify DMG
						double dmg = event.getDamage() * dmgModifierPvE;

						// Change DMG to modified Value
						event.setDamage(dmg);
					}

				}
				ConsoleLogger.debugLog("PvE: " +
						ChatColor.RED + attacker.getDisplayName() + ChatColor.RESET + " hit " +
						ChatColor.RED + mob.getName() + ChatColor.RESET +
						".\n\tDmg Done: " + event.getFinalDamage());
			}
		}
	}

	/**
	 * Get the items currently held by the Player
	 *
	 * @param p Player which items are desired
	 * @return Array consisting of the item in the Main hand, and item the Offhand
	 */
	public ItemStack[] getHeldItems(Player p) {
		return new ItemStack[]{p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand()};
	}

	/**
	 * Logic happening when a Player hits a Vampire
	 *
	 * @param attacker Player hitting the Vampire
	 * @param vampire  Vampire being hit by Player
	 * @param e        The whole Event, includes the players involved and all dmg information
	 * @return True unless method breaks
	 */
	public boolean playerHitVampire(Player attacker, Player vampire, EntityDamageByEntityEvent e) {
		double initialDmg = e.getDamage();
		double modifiedDmg = initialDmg;
		// Get Weapon
		ItemStack[] heldItems = getHeldItems(attacker);
		ItemStack weapon = heldItems[0];
		ItemStack offhand = heldItems[1];

		// Check Weapon
		if (weapon != null && (weapon.isSimilar(new ItemStack(Material.WOODEN_SWORD)) || weapon.getData().getItemType().equals(Material.LEGACY_WOOD_SWORD))) {
			// ALL CHANGES MADE TO THE ATTACK

			// Change DMG Amount
			modifiedDmg += initialDmg * dmgModifierPvP - initialDmg;

			// Console Log Damage Event (Can be toggled in game using /consolelog on/off)
			ConsoleLogger.debugLog("PvP: " +
					ChatColor.RED + attacker.getDisplayName() + ChatColor.RESET + " hit " +
					ChatColor.RED + vampire.getDisplayName() + ChatColor.RESET +
					".\n\tDmg Modified: " + initialDmg + " -> " + modifiedDmg);

			// Potion Effects
			vampire.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
			vampire.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 1));
			vampire.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 1));
		}

		// Check Offhand
		if (offhand != null && (offhand.isSimilar(new ItemStack(Material.NETHER_STAR)))) {
			modifiedDmg += holyStarBonus;
		}

		// Check Resistances
		ItemStack[] vampireArmour = vampire.getInventory().getArmorContents();
		if (!Arrays.equals(vampireArmour, new ItemStack[]{null, null, null, null})) {
			for (ItemStack armourPiece : vampireArmour) {
				if (armourPiece.getEnchantments().containsKey(Enchantment.PROTECTION_FIRE)) {
					modifiedDmg -= modifiedDmg * resistanceModifier;
				}
			}
		}

		e.setDamage(modifiedDmg);
		return true;
	}
}
