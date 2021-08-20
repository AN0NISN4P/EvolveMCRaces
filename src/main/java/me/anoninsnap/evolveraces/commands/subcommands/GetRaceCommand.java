package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.commands.SubCommand;
import me.anoninsnap.evolveraces.raceclasses.EvolvedRace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GetRaceCommand implements SubCommand {

	@Override
	public String getName() {
		return "get";
	}

	@Override
	public String getDescription() {
		return "Get the race of a player";
	}

	@Override
	public String getSyntax() {
		return "/race get [player]";
	}

	@Override
	public void perform(Player p, String[] args) {
		if (args.length == 1) {
			p.sendMessage(ChatColor.YELLOW + "You're currently a " + PlayerRaceLists.getPlayerRace(p));
		} else if (args.length == 2) {
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) {
				p.sendMessage(ChatColor.RED + "Player not found.");
			} else {
				EvolvedRace targetRace = PlayerRaceLists.getPlayerRace(target);
				if (targetRace == null) {
					p.sendMessage(ChatColor.YELLOW + target.getDisplayName() + " does not currently belong to any race");
				} else {
					p.sendMessage(ChatColor.GOLD + target.getDisplayName() + ChatColor.YELLOW + " is currently a " + ChatColor.GOLD + targetRace.getName());
				}
			}
		} else {
			p.sendMessage(ChatColor.RED + getSyntax());
		}
	}
}
