package me.anoninsnap.evolveraces.commands;

import me.anoninsnap.evolveraces.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {
	private static List<SubCommand> subCommands;

	public CommandManager() {
		subCommands = new ArrayList<>();
		subCommands.add(new AllRacesCommand());
		subCommands.add(new ConsoleDebugCommand());
		subCommands.add(new GetRaceCommand());
		subCommands.add(new HelpCommand());
		subCommands.add(new SetRaceCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player p) {
			if (args.length == 0) {
				HelpCommand help = new HelpCommand();
				help.perform(p, args);
			} else {
				for (SubCommand cmd : subCommands) {
					if (cmd.getName().equalsIgnoreCase(args[0])) {
						cmd.perform(p, args);
						return true;
					}
				}
			}
		}
		return true;
	}

	public static SubCommand getCmd(String cmd){
		for (SubCommand subCommand : subCommands) {
			if (subCommand.getName().equalsIgnoreCase(cmd)){
				return subCommand;
			}
		}
		return null;
	}
}
