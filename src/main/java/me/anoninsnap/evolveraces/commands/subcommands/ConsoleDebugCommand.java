package me.anoninsnap.evolveraces.commands.subcommands;

import me.anoninsnap.evolveraces.commands.SubCommand;
import me.anoninsnap.evolveraces.development.ConsoleLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ConsoleDebugCommand implements SubCommand {

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getDescription() {
		return "Set whether or not, the actions should be logged to the Console";
	}

	@Override
	public String getSyntax() {
		return "/race debug <on/off>";
	}

	@Override
	public void perform(Player p, String[] args) {
		if (args.length == 1) {
			ConsoleLogger.LOG_TO_CONSOLE = !ConsoleLogger.LOG_TO_CONSOLE;
		} else if (args.length == 2) {
			String action;
			switch (args[1]) {
				case "on", "true", "yes", "enable" -> {
					ConsoleLogger.LOG_TO_CONSOLE = true;
					action = ChatColor.GRAY + "Debug Logging turned on";
				}
				case "off", "false", "no", "disable" -> {
					ConsoleLogger.LOG_TO_CONSOLE = false;
					action = ChatColor.GRAY + "Debug Logging turned off";
				}
				default -> action = ChatColor.RED + "Incorrect Command. " + ChatColor.YELLOW + getSyntax();
			}
			p.sendMessage(action);
		} else {
			p.sendMessage(ChatColor.RED + getSyntax());
		}
	}
}
