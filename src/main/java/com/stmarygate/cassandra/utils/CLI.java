package com.stmarygate.cassandra.utils;

import com.stmarygate.cassandra.InteractiveConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CLI class extends the InteractiveConsole and provides additional functionality for handling
 * specific commands within a command-line interface. It includes commands such as 'help' for
 * displaying instructions and 'exit' for terminating the CLI.s
 */
public class CLI extends InteractiveConsole {

  private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);

  /**
   * Starts the CLI, which allows the user to enter commands. This method is called when the
   * Cassandra client is started.
   */
  public static void startCLI() {
    CLI cli = new CLI();
    cli.start();
  }

  /**
   * Processes user input within the CLI, handling specific commands such as 'help' and 'exit'.
   * Overrides the method in the parent class.
   *
   * @param userInput The user-entered command.
   */
  @Override
  public void processUserInput(String userInput) {
    if (userInput.trim().equalsIgnoreCase("help")) {
      displayHelp();
    } else if (userInput.trim().equalsIgnoreCase("exit")) {
      exitCLI();
    } else {
      LOGGER.info("Unknown command: {}", userInput);
    }
  }

  /** Displays a help message providing information about available commands. */
  private void displayHelp() {
    System.out.println("Commands:");
    System.out.println("  help - Display this help message.");
    System.out.println("  exit - Exit Cassandra.");
    System.out.println("  sendpacket <packet> <data> - Send a packet to the server.");
  }

  /** Exits the CLI, logging a message before terminating the program. */
  private void exitCLI() {
    LOGGER.info("Exiting CLI...");
    System.exit(0);
  }
}
