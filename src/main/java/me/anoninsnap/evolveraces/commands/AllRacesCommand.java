package me.anoninsnap.evolveraces.commands;

import me.anoninsnap.evolveraces.PlayerRaceLists;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllRacesCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player player && args.length != 0) {
			player.sendMessage("Your stored races are " + ChatColor.AQUA + PlayerRaceLists.getStoredRaces(player));
			ConsoleLogger.debugLog(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " stored races: " + ChatColor.AQUA + PlayerRaceLists.getStoredRaces(player));
		}

		ConsoleLogger.debugLog(ChatColor.YELLOW + "All available Races: " + ChatColor.AQUA + PlayerRaceLists.allRaces());
		ConsoleLogger.debugLog(ChatColor.YELLOW + "All stored Races:    " + ChatColor.AQUA + PlayerRaceLists.getStoredRaces());

		return true;
	}
}
