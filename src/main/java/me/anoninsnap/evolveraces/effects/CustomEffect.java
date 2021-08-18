package me.anoninsnap.evolveraces.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CustomEffect {
	public static void apply(Player player, PotionEffectType effect, int duration, int level) {
		player.addPotionEffect(new PotionEffect(effect, duration, level, false, false, false));
	}

	public static void apply(Player player, CustomEffectType effect, int duration, int level) {
		if (effect == CustomEffectType.BURN){
			burn(player, duration, level);
		}

	}

	private static void burn(Player p, int t, int v){
		p.setFireTicks(t);
	}
}
