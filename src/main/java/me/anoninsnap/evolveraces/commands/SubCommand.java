package me.anoninsnap.evolveraces.commands;

import org.bukkit.entity.Player;

public interface SubCommand {
	String getName();
	String getDescription();
	String getSyntax();
	void perform(Player p, String args[]);
}
