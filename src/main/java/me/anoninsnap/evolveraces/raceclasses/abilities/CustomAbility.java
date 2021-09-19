package me.anoninsnap.evolveraces.raceclasses.abilities;

import me.anoninsnap.evolveraces.EvolveRaces;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import me.anoninsnap.evolveraces.development.ParticleDraw;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

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
				case SNIPE -> successfulAbilityUse = snipe(p);
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
		// Step each iteration
		Vector deltaD = facing.clone().multiply(0.4d);
		// Center for Finding Mobs
		Location ec = p.getEyeLocation();
		// Offset ec so you don't target all mobs behind you
		ec.add(deltaD);
		// World where action is being performed
		World w = p.getWorld();
		// Size for each box in search
		double boxSize;
		// Set for target searching. Can't have Duplicates
		Set<Entity> targets = new HashSet<>();

		for (int i = 1; i <= 10; i++) {
			// Move ec (epicenter)
			ec.add(deltaD);

			boxSize = 0.5 * (2.2 - (2.2 * (double) i / 15.4));

			// Find all mobs in square
			targets.addAll(w.getNearbyEntities(ec, boxSize, boxSize, boxSize));

			ParticleDraw.drawCube(w, ec, boxSize);
		}

		// Remove player using ability from targeted group
		targets.remove(p);

		// Damage mobs
		for (Entity target : targets) {
			if (target instanceof LivingEntity t) {
				t.damage(MULTIPLIER, p);
			}
		}

		// Draws a box around the area affected
//		createBox(w,
//				  ec.toVector().add(new Vector( 2,  1,  2)).toLocation(w),
//				  ec.toVector().add(new Vector( 2,  1, -2)).toLocation(w),
//				  ec.toVector().add(new Vector(-2,  1, -2)).toLocation(w),
//				  ec.toVector().add(new Vector(-2,  1,  2)).toLocation(w),
//				  ec.toVector().add(new Vector( 2, -1,  2)).toLocation(w),
//				  ec.toVector().add(new Vector( 2, -1, -2)).toLocation(w),
//				  ec.toVector().add(new Vector(-2, -1, -2)).toLocation(w),
//				  ec.toVector().add(new Vector(-2, -1,  2)).toLocation(w)
//		);

//		Collection<Entity> mobs = ec.getWorld().getNearbyEntities(ec, 2, 1, 2);
//		if (mobs.size() == 0) {
//			return false;
//		}
		// Apply dmg to targets
//		for (Entity mob : mobs) {
//			if (mob instanceof LivingEntity m && m != p) {
//				m.damage(MULTIPLIER, p);
//			}
//		}

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

	private boolean snipe(Player p) {
		World w = p.getWorld();
		Location playerEyes = p.getEyeLocation();
		Vector facing = playerEyes.getDirection();

		// Create a RayTrace looking for entities
		RayTraceResult r = w.rayTraceEntities(playerEyes.clone().add(facing), facing, 60);
		if (r == null) {
			return false;
		}

		// Get Targeted Entity
		Entity target = r.getHitEntity();
		// Check if Entity is a Living Mob
		if (!(target instanceof LivingEntity mob)) {
			return false;
		}
		// Get Distance between player and targeted mob
		double distance = playerEyes.distance(mob.getLocation());
		ConsoleLogger.debugLog("Distance between " + p.getDisplayName() + " and their Target: " + distance);

		// Calculate Damage
		double dmg = MULTIPLIER * Math.log(distance) + 5d;

		// Draw a line from the shooter to the target
		ParticleDraw.drawLine(Particle.DRIPPING_OBSIDIAN_TEAR, playerEyes, mob.getLocation());

		// Deal Damage
		mob.damage(dmg, p);
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


	// !! THIS SHOULD BE REMOVED
}
