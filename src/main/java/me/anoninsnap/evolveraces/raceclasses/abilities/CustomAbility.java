package me.anoninsnap.evolveraces.raceclasses.abilities;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CustomAbility {
	private CustomAbilityType abilityType;
	private boolean onCooldown;
	private int cooldownTimer;

	private final double MULTIPLIER = 1.3; // TODO: Config file! (Sep 19, 2021)

	/**
	 * Create a Custom Ability
	 *
	 * @param ability       Ability which should be used
	 * @param cooldownTimer Cooldown in Ticks
	 */
	public CustomAbility(CustomAbilityType ability, int cooldownTimer) {
		abilityType = ability;
		this.cooldownTimer = cooldownTimer * 50;
		onCooldown = false;
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
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GRAY + "" + ChatColor.ITALIC + "Ability on Cooldown"));
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

	private boolean claw(Player p) {


		return true;
	}

	private boolean leap(Player p) {

		return true;
	}

	private boolean blink(Player p) {


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
}
