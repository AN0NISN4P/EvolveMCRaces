package me.anoninsnap.evolveraces.commands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GetRaceCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String race = null;
		boolean targeted = false;

		// Find the race when requested by a Player
		if (sender instanceof Player player) {
			if (player.hasPermission("emcraces.checkothers") && args.length != 0) {
				Player argPlayer = Bukkit.getPlayer(args[0]);
				if (argPlayer != null) {
					// If a player has been found
					race = PlayerRaceLists.getPlayerRace(argPlayer);
					targeted = true;
				} else {
					// If a player has not been found
					player.sendMessage(ChatColor.RED + "Player does not belong to any race");
					return true;
				}

			} else {
				// Reaching here means the Player either didn't specify a target or doesn't have the Permission to
				race = PlayerRaceLists.getPlayerRace(player);
				targeted = false;
			}


		} else
			// If command was issued from Console
			if (sender instanceof ConsoleCommandSender && args.length != 0) {
			Player argPlayer = Bukkit.getPlayer(args[0]);
			race = PlayerRaceLists.getPlayerRace(argPlayer);
			targeted = true;
		}

		// Checks if Race has been set. If command was issued by a Commandblock or an issue from the Console forgot the Argument, it will fail
		if (race != null) {
			System.out.println("Targeted: " + targeted); //SOUT: Delete Line (Ctrl + Y)
			String playerTarget;

			// Formalities - TODO: Figure out if this can be customised from a Config File
			if (targeted) {
				playerTarget = args[0] + " is";
			} else {
				playerTarget = "You're";
			}
			sender.sendMessage(ChatColor.GOLD + playerTarget + ChatColor.WHITE + " currently listed as; " + ChatColor.YELLOW + race);
		}
		return true;
	}
}
