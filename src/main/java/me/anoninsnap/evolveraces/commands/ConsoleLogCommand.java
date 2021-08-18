package me.anoninsnap.evolveraces.commands;

import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Set;

public class ConsoleLogCommand implements CommandExecutor {
	Set<String> enable = Collections.emptySet();
	Set<String> disable = Collections.emptySet();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (enable.isEmpty() || disable.isEmpty()){
			Collections.addAll(enable, "yes", "true", "on", "enable");
			Collections.addAll(disable, "no", "false", "off", "disable");
		}

		if (sender.isOp() && args.length != 0) {
			String arg = args[0];
			if (arg.equalsIgnoreCase("true")) {
				ConsoleLogger.logToConsole = true;
			} else if (arg.equalsIgnoreCase("false")) {
				ConsoleLogger.logToConsole = false;
			} else {
				sender.sendMessage(ChatColor.RED + "/consoleLog <true/false>");
			}
		}

		return true;
	}
}
