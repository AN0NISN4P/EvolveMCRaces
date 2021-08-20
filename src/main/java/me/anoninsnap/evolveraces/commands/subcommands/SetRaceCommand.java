package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetRaceCommand implements SubCommand {


	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String getDescription() {
		return "Set a player's race";
	}

	@Override
	public String getSyntax() {
		return "/race set <race>";
	}

	@Override
	public void perform(Player p, String[] args) {
		String newRace;
		// Check Permission to set race AND check if a Player has been specified
		if (p.hasPermission("emcraces.setothers") && args.length == 3) {
			Player target = Bukkit.getPlayer(args[1]);
			newRace = args[2].toLowerCase();

			if (target == null) {
				// If Player is offline
				p.sendMessage(ChatColor.RED + "Player could not be found");
				p.sendMessage(ChatColor.RED + getSyntax());
			} else if (!PlayerRaceLists.setRace(target, newRace)) {
				// If the Race isn't a valid Race
				p.sendMessage(ChatColor.RED + "Race does not exist");
				p.sendMessage(ChatColor.RED + getSyntax());
			} else {
				// If Player is online, and Race could be set
				target.sendMessage(ChatColor.AQUA + "Your Race has been set to " + ChatColor.YELLOW + newRace);
				p.sendMessage(target.getDisplayName() + "'s race has been set to " + ChatColor.YELLOW + newRace);
			}

		} else if (args.length == 2) {
			// If Player does not have the permission, or didn't specify a player
			newRace = args[1].toLowerCase();

			// Inform the player, whether or not the specified Race cold be set
			if (PlayerRaceLists.setRace(p, newRace)) {
				p.sendMessage(ChatColor.AQUA + "Your Race has been set to " + ChatColor.YELLOW + newRace);
			} else {
				p.sendMessage(ChatColor.RED + "That Race does not appear in our journals.");
			}
		} else {
			p.sendMessage(ChatColor.RED + "/race set [player] <race>");
		}
	}

}
