package me.anoninsnap.evolveraces.raceclasses.abilities;

import me.anoninsnap.evolveraces.EvolveRaces;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;

//TODO:
// Config File
// Particles

public class CustomAbility {
	private CustomAbilityType abilityType;
	private boolean onCooldown;
	private int cooldownTimer;

	private final double MULTIPLIER; // TODO: Config file! (Sep 19, 2021)

	/**
	 * Create a Custom Ability
	 *
	 * @param ability           Ability which should be used
	 * @param cooldownTimer     Cooldown in Ticks
	 * @param abilityMultiplier Multiplier for strength of ability <br>
	 *                          Examples:<br>
	 *                          - Force for Dash <br>
	 *                          - Dmg for Claw <br>
	 *                          - Distance for Blink <br>
	 */
	public CustomAbility(CustomAbilityType ability, int cooldownTimer, double abilityMultiplier) {
		abilityType = ability;
		this.cooldownTimer = cooldownTimer * 50;
		onCooldown = false;
		MULTIPLIER = abilityMultiplier;
	}

	/**
	 * Use Custom Ability
	 *
	 * @param p Player using the ability
	 */
	public void use(Player p) {
		boolean successfulAbilityUse = false;
		if (!onCooldown) {
			switch (abilityType) {
				case DASH -> successfulAbilityUse = dash(p);
				case CLAW -> successfulAbilityUse = claw(p);
				case LEAP -> successfulAbilityUse = leap(p);
				case BLINK -> successfulAbilityUse = blink(p);
			}
		}
		if (!successfulAbilityUse) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "" + ChatColor.ITALIC + "Ability cancelled"));
		}
	}

	private boolean dash(Player p) {
		// Get direction Player is facing
		Vector facing = p.getEyeLocation().getDirection();

		// Create a new Velocity Vector, using the Player's facing direction as inputs
		Vector newVelocity = p.getVelocity().add(new Vector(MULTIPLIER * facing.getX(), 0, MULTIPLIER * facing.getZ()));
		newVelocity.setY(0.2d);

		// Set the Player's velocity to the new Vector
		p.setVelocity(newVelocity);

		// Start the ability's cooldown
		startCooldown();
		return true;
	}

	private boolean claw(Player p) { // TODO: Implement Claw (Sep 19, 2021)
		// Get player facing direction
		Vector facing = p.getEyeLocation().getDirection();
		Location epicenter = p.getEyeLocation().add(facing.multiply(3));
		World w = epicenter.getWorld();

		// Check for entity within a close distance
		assert w != null;

		// Draws a box around the area affected
//		createBox(epicenter.toVector().add(new Vector(1, 1, 1)).toLocation(w),
//				epicenter.toVector().add(new Vector(1, 1, -1)).toLocation(w),
//				epicenter.toVector().add(new Vector(-1, 1, -1)).toLocation(w),
//				epicenter.toVector().add(new Vector(-1, 1, 1)).toLocation(w),
//				epicenter.toVector().add(new Vector(1, -1, 1)).toLocation(w),
//				epicenter.toVector().add(new Vector(1, -1, -1)).toLocation(w),
//				epicenter.toVector().add(new Vector(-1, -1, -1)).toLocation(w),
//				epicenter.toVector().add(new Vector(-1, -1, 1)).toLocation(w)
//		);

		Collection<Entity> mobs = epicenter.getWorld().getNearbyEntities(epicenter, 1, 1, 1);
		if (mobs.size() == 0) {
			return false;
		}
		// Apply dmg to targets
		for (Entity mob : mobs) {
			if (mob instanceof LivingEntity m && m != p) {
				m.damage(MULTIPLIER, p);
			}
		}

		startCooldown();
		return true;
	}

	private boolean leap(Player p) {
		// Get direction Player is facing
		Vector facing = p.getEyeLocation().getDirection();

		// Create a new Velocity Vector, using the Player's facing direction as inputs
		Vector newVelocity = p.getVelocity().add(new Vector(MULTIPLIER * facing.getX(), 0, MULTIPLIER * facing.getZ()));
		newVelocity.setY(0.7 * MULTIPLIER + 0.7 * facing.getY());

		// Set the Player's velocity to the new Vector
		p.setVelocity(newVelocity);

		// Add player to list of people not to be affected by fall dmg
		EvolveRaces.noFallDmgList.add(p);

		// Start the ability's cooldown
		startCooldown();
		return true;
	}

	private boolean blink(Player p) {
		// Get player facing direction
		Vector facing = p.getEyeLocation().getDirection();

		// Check x blocks forwards (should be affected by Multiplier)
		Location newLoc = p.getLocation().add(facing.multiply(MULTIPLIER));

		// Randomise location a little
		newLoc = newLoc.add(Math.random() - 0.5d, 0.1d, Math.random() - 0.5d);

		// Check location so we don't kill the player
		if (newLoc.getBlock().getType() != Material.AIR) { // TODO: Handle Water
			// IF BLOCK IS NOT AIR
			// Check if the block above is Air
			if (newLoc.add(0, 1, 0).getBlock().getType() == Material.AIR) {
				// IF BLOCK ABOVE IS AIR
				// Move newLoc to that location
				newLoc = newLoc.add(0, 1, 0);
			} else {
				// IF BLOCK ABOVE IS NOT AIR
				return false;
			}
		}

		// Teleport to the new location
		p.teleport(newLoc);

		startCooldown();
		return true;
	}

	/**
	 * Start a cooldown using the field as a timer
	 */
	private void startCooldown() {
		onCooldown = true;
		Thread cooldown = new Thread(() -> {
			try {
				Thread.sleep(cooldownTimer);
				onCooldown = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		cooldown.setDaemon(true);
		cooldown.start();
	}

	// !! THIS SHOULD BE REMOVED

	private void createBox(Location A, Location B, Location C, Location D, Location E, Location F, Location G, Location H) {
		World w = A.getWorld();
		assert w != null;
		// Top Rectangle
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, A, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, B, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, C, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, D, 1);

		// Bottom Rectangle
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, E, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, F, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, G, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, H, 1);

		// Top Rectangle Midpoints
		line(w, A, B);
		line(w, B, C);
		line(w, C, D);
		line(w, D, A);

		// Bottom Rectangle Midpoints
		line(w, E, F);
		line(w, F, G);
		line(w, G, H);
		line(w, H, E);

		// Connecting Struts
		line(w, A, E);
		line(w, B, F);
		line(w, C, G);
		line(w, D, H);
	}

	private void line(World w, Location p1, Location p2) {
		Location mid = midPoint(w, p1, p2);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, midPoint(w, p1, mid), 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, mid, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, midPoint(w, mid, p2), 1);
	}

	private Location midPoint(World w, Location pos1, Location pos2) {
		Vector AminusB = pos1.toVector().subtract(pos2.toVector());
		return pos2.toVector().add(AminusB.multiply(0.5d)).toLocation(w);
	}

	// !! THIS SHOULD BE REMOVED
}
