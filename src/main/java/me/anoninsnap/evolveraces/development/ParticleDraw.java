package me.anoninsnap.evolveraces.development;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class ParticleDraw {
	public static boolean DEBUG_DRAW;
	public static boolean EFFECT_DRAW = true;

	public static void drawCube(World w, Location center, double vertexOffset) {
		if (!DEBUG_DRAW) {
			return;
		}
		w.spawnParticle(Particle.DRIP_LAVA, center, 1);
		createBox(w,
				center.toVector().add(new Vector(-vertexOffset, -vertexOffset, -vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(-vertexOffset, -vertexOffset, vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(vertexOffset, -vertexOffset, vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(vertexOffset, -vertexOffset, -vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(-vertexOffset, vertexOffset, -vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(-vertexOffset, vertexOffset, vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(vertexOffset, vertexOffset, vertexOffset)).toLocation(w),
				center.toVector().add(new Vector(vertexOffset, vertexOffset, -vertexOffset)).toLocation(w)
		);
	}

	public static void createBox(World w, Location A, Location B, Location C, Location D, Location E, Location F, Location G, Location H) {
		if (!DEBUG_DRAW) {
			return;
		}
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

	public static void line(World w, Location p1, Location p2) {
		if (!DEBUG_DRAW) {
			return;
		}
		Location mid = midPoint(w, p1, p2);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, midPoint(w, p1, mid), 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, mid, 1);
		w.spawnParticle(Particle.DRIPPING_OBSIDIAN_TEAR, midPoint(w, mid, p2), 1);
	}

	private static Location midPoint(World w, Location pos1, Location pos2) {
		Vector diff = pos1.toVector().subtract(pos2.toVector());
		return pos2.toVector().add(diff.multiply(0.5d)).toLocation(w);
	}


	public static void drawLine(Particle effect, Location pointA, Location pointB) {
		assert effect != null && pointA != null && pointB != null;
		World w = pointA.getWorld();
		assert w != null;

		Vector diff = pointB.toVector().subtract(pointA.toVector());
		double d = diff.length();
		int particlesPrBlock = 10;
		diff = diff.normalize().multiply(d / particlesPrBlock);
		Location walkingPoint = pointA.clone();

		for (int i = 0; i < d * particlesPrBlock; i++) {
			w.spawnParticle(effect, walkingPoint, 1);
			walkingPoint.add(diff);
		}
	}
}
