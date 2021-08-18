package me.anoninsnap.evolveraces.development;


public class ConsoleLogger {
	public static boolean logToConsole;

	/**
	 * Sends any message to the Console, if set to do so
	 *
	 * @param messageToConsole String with the message. <br> <i>Supports ChatColors</i>
	 */
	public static void debugLog(String messageToConsole) {
		if (logToConsole) {
			System.out.println(messageToConsole);
		}
	}
}
