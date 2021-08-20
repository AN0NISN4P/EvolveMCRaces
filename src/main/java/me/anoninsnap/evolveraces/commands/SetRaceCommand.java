package me.anoninsnap.evolveraces.commands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRaceCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Return false if no argument was given
		if (args.length == 0) {
			return false;
		}

		// Set Race for a Player
		if (sender instanceof Player player) {
			String newRace;
			// Check Permission to set race AND check if a Player has been specified
			if (player.hasPermission("emcraces.setothers") && args.length > 1) {
				Player target = Bukkit.getPlayer(args[0]);
				newRace = args[1].toLowerCase();

				// Tell Console the race attempted to be attached to Player
				ConsoleLogger.debugLog(player.getDisplayName() + " will be set to " + newRace);

				// If targeted player could be found and Race could be set, informs the players of the event happening
				if (target != null && PlayerRaceLists.setRace(target, newRace)) {
					target.sendMessage(ChatColor.AQUA + "Your Race has been set to " + ChatColor.YELLOW + newRace);
					player.sendMessage(target.getDisplayName() + "'s race has been set to " + ChatColor.YELLOW + newRace);

				} else { // TODO: Differentiate between Target and Race not found
					player.sendMessage(ChatColor.RED + "Target or Race could not be found");
				}

			} else {
				// If Player does not have the permission, or didn't specify a player
				newRace = args[0].toLowerCase();

				// Tell Console the race attempted to be attached to Player
				ConsoleLogger.debugLog(player.getDisplayName() + " will be set to " + newRace);

				// If race can be set, will inform the player
				if (PlayerRaceLists.setRace(player, newRace)) {
					player.sendMessage(ChatColor.AQUA + "Your Race has been set to " + ChatColor.YELLOW + newRace);

				} else {
					player.sendMessage(ChatColor.RED + "That Race does not appear in our journals.");
				}
			}

		} else {
			ConsoleLogger.debugLog("Action can only be done In-game"); // TODO: Implement Setting of a Race from Console
		}


		return true;
	}
}
