package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.commands.SubCommand;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class AllRacesCommand implements SubCommand {

	@Override
	public String getName() {
		return "all";
	}

	@Override
	public String getDescription() {
		return "Show all races";
	}

	@Override
	public String getSyntax() {
		return "/race all";
	}

	@Override
	public void perform(Player p, String[] args) {
		Set<String> races = PlayerRaceLists.getAllRaces();
		p.sendMessage("Available Races");
		for (String race : races) {
			p.sendMessage("- " + ChatColor.AQUA + race);
		}
	}
}
