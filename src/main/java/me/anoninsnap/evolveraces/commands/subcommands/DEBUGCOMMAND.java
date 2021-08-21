package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.commands.SubCommand;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class DEBUGCOMMAND implements SubCommand {
	@Override
	public String getName() {
		return "admin";
	}

	@Override
	public String getDescription() {
		return "admin stuff";
	}

	@Override
	public String getSyntax() {
		return "/race admin [idfk]";
	}

	@Override
	public void perform(Player p, String[] args) {

	}
}
