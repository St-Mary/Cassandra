package com.stmarygate.cassandra.utils;

import com.stmarygate.cassandra.Cassandra;
import com.stmarygate.coral.network.packets.Packet;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Scanner;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A command line interface for the Cassandra client. */
public class CLI {
  private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);
  @Getter @Setter private static Thread thread;

  public static void start() {
    thread = new Thread(CLI::run);
    thread.setName("CLI");
    thread.start();
  }

  /** Run the CLI */
  private static void run() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      // Utils.wait(200);
      System.out.print("> ");
      String line = scanner.nextLine().trim();

      switch (line.split(" ")[0]) {
        case "help":
          {
            System.out.println("Commands:");
            System.out.println("  help - Show this help message.");
            System.out.println("  exit - Exit the Cassandra CLI.");
            System.out.println("  sendpacket <packet> <data> - Send a packet to the server.");
            break;
          }
        case "sendpacket":
          {
            if (!checkLine(line, "^sendpacket ([a-zA-Z]+) (.*)$")) break;
            sendPacket(line);
            break;
          }
        default:
          {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
            break;
          }
      }
    }
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
   * @param length The length of the constructor
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
   * Get the parameters of the packet
   *
   * @param constructor The constructor
   * @param data The data
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
   * Check the line with a regex
   *
   * @param line The line to check
   * @param regex The regex to check with
   */
  private static boolean checkLine(String line, String regex) {
    if (!line.matches(regex)) {
      System.out.println("Invalid syntax. Type 'help' for a list of commands.");
      return false;
    }
    return true;
  }

  public static void kill() {
    if (thread != null) {
      thread.interrupt();
      thread = null;
    }
  }
}
