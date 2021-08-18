package me.anoninsnap.evolveraces.commands;

import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConsoleLogCommand implements CommandExecutor {
	Set<String> enable = new HashSet<>();
	Set<String> disable = new HashSet<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (enable.size() == 0 || disable.size() == 0) {
			Collections.addAll(enable, "yes", "true", "on", "enable");
			Collections.addAll(disable, "no", "false", "off", "disable");
		}

		if (sender.isOp() && args.length != 0) {
			String arg = args[0];
			if (enable.contains(arg)) {
				ConsoleLogger.logToConsole = true;
			} else if (disable.contains(arg)) {
				ConsoleLogger.logToConsole = false;
			} else {
				sender.sendMessage(ChatColor.RED + "/consoleLog <true/false>");
			}
		}

		return true;
	}
}
