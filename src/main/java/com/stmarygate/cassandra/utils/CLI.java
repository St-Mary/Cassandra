package com.stmarygate.cassandra.utils;

import com.stmarygate.cassandra.Cassandra;
import com.stmarygate.cassandra.InteractiveConsole;
import com.stmarygate.coral.network.packets.Packet;
import java.lang.reflect.Constructor;
import java.util.Arrays;
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
   * Send a packet to the server
   *
   * @param line The line to parse
   */
  public static void sendPacket(String line) {
    String[] split = line.split(" ");
    String packet = split[1];
    String[] data = line.substring(split[0].length() + split[1].length() + 2).split(" ");

    // Get class from packet name
    Class<? extends Packet> clazz = getPacketClass(packet);
    if (clazz == null) {
      return;
    }

    try {
      Constructor<? extends Packet> constructor = getConstructor(clazz, data.length);
      Object[] parameters = getParameters(constructor, data);
      Packet p = constructor.newInstance(parameters);
      LOGGER.info("Sending packet {}", p);
      Cassandra.getBaseChannel().getSession().write(p);
      LOGGER.info("Packet sent");
    } catch (Exception e) {
      LOGGER.error(
          "Failed to send packet {}\n{}",
          packet,
          e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
    }
  }

  /**
   * Get the parameters for the constructor
   *
   * @param constructor The constructor
   * @param data The data
   * @return The parameters
   */
  private static Object[] getParameters(Constructor<? extends Packet> constructor, String[] data) {
    Object[] parameters = new Object[data.length];
    for (int i = 0; i < data.length; i++) {
      Class<?> parameterType = constructor.getParameterTypes()[i];
      if (parameterType == String.class) {
        parameters[i] = data[i];
      } else if (parameterType == int.class) {
        parameters[i] = Integer.parseInt(data[i]);
      } else if (parameterType == float.class) {
        parameters[i] = Float.parseFloat(data[i]);
      } else if (parameterType == double.class) {
        parameters[i] = Double.parseDouble(data[i]);
      } else if (parameterType == boolean.class) {
        parameters[i] = Boolean.parseBoolean(data[i]);
      } else {
        System.out.println("Invalid data type.");
        return null;
      }
    }
    return parameters;
  }

  /**
   * Check the packet name
   *
   * @param packetName The packet name
   */
  @SuppressWarnings("unchecked")
  private static Class<? extends Packet> getPacketClass(String packetName) {
    packetName = packetName.substring(0, 1).toUpperCase() + packetName.substring(1);
    try {
      return (Class<? extends Packet>)
          Class.forName("com.stmarygate.coral.network.packets.client.Packet" + packetName);
    } catch (ClassNotFoundException e) {
      System.out.println("Invalid packet name.");
    }
    return null;
  }

  /**
   * Get the right constructor for the packet
   *
   * @param clazz The packet class
   * @param length The length of the constructor Starts the CLI, which allows the user to enter
   *     commands. This method is called when the Cassandra client is started.
   */
  @SuppressWarnings("unchecked")
  private static Constructor<? extends Packet> getConstructor(
      Class<? extends Packet> clazz, int length) {
    Constructor<? extends Packet> constructor = null;
    // Get all constructors
    Constructor<?>[] constructors = clazz.getConstructors();

    for (Constructor<?> c : constructors) {
      Class<?>[] parameterTypes = c.getParameterTypes();
      if (parameterTypes.length == length) {
        constructor = (Constructor<? extends Packet>) c;
        break;
      }
    }

    if (constructor == null) {
      System.out.println("Invalid amount of data.");
    }

    return constructor;
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
    } else if (userInput.split(" ")[0].equalsIgnoreCase("sendpacket")) {
      sendPacket(userInput);
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
