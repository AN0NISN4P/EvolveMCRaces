package me.anoninsnap.evolveraces.development;


import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ConsoleLogger {
	public static boolean logToConsole;

	/**
	 * Sends any message to the Console, if set to do so
	 *
	 * @param messageToConsole String with the message. <br> <i>Supports ChatColors</i>
	 */
	public static void debugLog(String messageToConsole) {
		if (logToConsole) {
			Bukkit.getLogger().log(Level.INFO, messageToConsole);
		}
	}

	public static void warningLog(String messageToConsole) {
		if (logToConsole) {
			Bukkit.getLogger().log(Level.WARNING, messageToConsole);
		}
	}
}
