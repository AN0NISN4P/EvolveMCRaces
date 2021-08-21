package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.commands.SubCommand;
import me.anoninsnap.evolveraces.raceclasses.EvolvedRace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetLevelCommand implements SubCommand {
	@Override
	public String getName() {
		return "setLevel";
	}

	@Override
	public String getDescription() {
		return "Set the Active Race level of a Player";
	}

	@Override
	public String getSyntax() {
		return "/race setlevel [player] <level>";
	}

	@Override
	public void perform(Player p, String[] args) {
		if (args.length < 2) {
			// No level argument
			p.sendMessage(ChatColor.RED + "Missing arguments");
			p.sendMessage(ChatColor.RED + getSyntax());
		} else if (args.length == 2) {
			// Check if argument was an integer
			try {
				int lvl = Integer.parseInt(args[1]);
				// Get current player race
				EvolvedRace playerRace = PlayerRaceLists.getPlayerRace(p);
				if (playerRace == null) {
					p.sendMessage(ChatColor.RED + "You're not in a Race yet");
				} else {
					// Set the race level
					playerRace.setLevel(lvl);
				}
			} catch (NumberFormatException e) {
				// If argument wasn't an Integer
				p.sendMessage(ChatColor.RED + "Incorrect argument");
				p.sendMessage(ChatColor.RED + getSyntax());
			}

		} else if (args.length == 3) {
			// Player specifies a target and a level

			// Get Target
			Player target = Bukkit.getPlayer(args[1]);
			// Check if Target was gotten
			if (target != null) {
				try {
					// Try to get level as integer
					int lvl = Integer.parseInt(args[2]);
					// Get Target's Race
					EvolvedRace targetRace = PlayerRaceLists.getPlayerRace(target);
					if (targetRace != null) {
						// If Race was gotten, set level
						targetRace.setLevel(lvl);
					} else {
						p.sendMessage(ChatColor.DARK_RED + target.getDisplayName() + ChatColor.RED + " is not in a Race yet");
					}
				} catch (NumberFormatException e) {
					p.sendMessage(ChatColor.RED + "Incorrect argument");
					p.sendMessage(ChatColor.RED + getSyntax());
				}
			}
		}
	}
}
