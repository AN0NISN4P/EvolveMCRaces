package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.commands.CommandManager;
import me.anoninsnap.evolveraces.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpCommand implements SubCommand {
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Get the Description and Syntax of a command";
	}

	@Override
	public String getSyntax() {
		return "/race help [command]";
	}

	@Override
	public void perform(Player p, String[] args) {
		if (args.length == 2) {
			SubCommand cmd = CommandManager.getCmd(args[1]);
			if (cmd != null) {
				String desc = cmd.getDescription();
				String syntax = cmd.getSyntax();

				p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + desc);
				p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + syntax);
			} else {
				p.sendMessage(ChatColor.RED + "Command not found");
				p.sendMessage(ChatColor.RED + getSyntax());
			}
		} else {
			p.sendMessage(ChatColor.GRAY + getSyntax());
		}
	}
}
