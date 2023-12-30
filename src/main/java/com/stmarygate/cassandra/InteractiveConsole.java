package com.stmarygate.cassandra;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The InteractiveConsole class represents an interactive console for user input with concurrent
 * logging using SLF4J. It provides a simple prompt for users to enter commands while logging events
 * simultaneously.
 */
public class InteractiveConsole {
  private static final Logger LOGGER = LoggerFactory.getLogger(InteractiveConsole.class);

  /**
   * Starts the interactive console, allowing users to input commands and logging events
   * concurrently. The console utilizes SLF4J for logging.
   */
  public void start() {
    try {
      LineReader lineReader =
          LineReaderBuilder.builder()
              .terminal(TerminalBuilder.builder().dumb(true).build())
              .build();

      while (true) {
        String userInput = lineReader.readLine("Cassandra> ");
        if (userInput.equals("exit")) break;
        processUserInput(userInput);
      }
    } catch (Exception e) {
      LOGGER.error("Error while starting the interactive console", e);
    }
  }

  /**
   * Process the user input (called when the user enters a command).
   *
   * @param userInput The user input to process.
   */
  protected void processUserInput(String userInput) {}
}
