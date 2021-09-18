package me.anoninsnap.evolveraces.raceclasses.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomEffect {
	public static int burnLightLevel;

	public static void apply(Player player, PotionEffectType effect, int duration, int level) {
		player.addPotionEffect(new PotionEffect(effect, duration, level, false, false, false));
	}

	public static void apply(Player player, CustomEffectType effect, int duration, int level) {
		if (effect == CustomEffectType.BURN){
			burn(player, duration, level);
		}
	}

	private static void burn(Player p, int t, int v){
		if (p.getLocation().getBlock().getLightLevel() > burnLightLevel && p.getLocation().getBlock().getLightFromSky() > burnLightLevel)
		p.setFireTicks(Math.max(t, 21));
	}
}
