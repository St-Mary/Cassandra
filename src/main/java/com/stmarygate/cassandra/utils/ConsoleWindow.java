package com.stmarygate.cassandra.utils;

import com.stmarygate.cassandra.Constants;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Scanner;

/** This class contains all the methods for the console window. */
public class ConsoleWindow {

  /** Print the header. */
  public static void printHeader() {
    System.out.println(
        """
                     $$$$$$\\   $$$$$$\\   $$$$$$\\   $$$$$$\\   $$$$$$\\  $$\\   $$\\ $$$$$$$\\  $$$$$$$\\   $$$$$$\\ \s
                    $$  __$$\\ $$  __$$\\ $$  __$$\\ $$  __$$\\ $$  __$$\\ $$$\\  $$ |$$  __$$\\ $$  __$$\\ $$  __$$\\\s
                    $$ /  \\__|$$ /  $$ |$$ /  \\__|$$ /  \\__|$$ /  $$ |$$$$\\ $$ |$$ |  $$ |$$ |  $$ |$$ /  $$ |
                    $$ |      $$$$$$$$ |\\$$$$$$\\  \\$$$$$$\\  $$$$$$$$ |$$ $$\\$$ |$$ |  $$ |$$$$$$$  |$$$$$$$$ |
                    $$ |      $$  __$$ | \\____$$\\  \\____$$\\ $$  __$$ |$$ \\$$$$ |$$ |  $$ |$$  __$$< $$  __$$ |
                    $$ |  $$\\ $$ |  $$ |$$\\   $$ |$$\\   $$ |$$ |  $$ |$$ |\\$$$ |$$ |  $$ |$$ |  $$ |$$ |  $$ |
                    \\$$$$$$  |$$ |  $$ |\\$$$$$$  |\\$$$$$$  |$$ |  $$ |$$ | \\$$ |$$$$$$$  |$$ |  $$ |$$ |  $$ |
                     \\______/ \\__|  \\__| \\______/  \\______/ \\__|  \\__|\\__|  \\__|\\_______/ \\__|  \\__|\\__|  \\__|""");
    System.out.println(
        "------------------------------------------------------------------------------------------");
    System.out.println("GitHub: https://github.com/St-Mary/Cassandra");
    System.out.println("Version: " + Constants.getVersion());
    System.out.println("Licence: GNU General Public License v3.0");
    System.out.println(
        "------------------------------------------------------------------------------------------");
  }

  /**
   * Get the address to connect to.
   *
   * @return The address to connect to.
   */
  public static SocketAddress getAddress() {
    System.out.println("Enter the host and port to connect to (host:port): ");
    Scanner scanner = new Scanner(System.in);
    System.out.print("Cassandra> ");
    String hostAndPort = scanner.nextLine().trim();
    String regex = "^(.*):(\\d+)$";
    if (hostAndPort.matches(regex)) {
      String[] hostAndPortArray = hostAndPort.split(":");
      String h = hostAndPortArray[0];
      int p = Integer.parseInt(hostAndPortArray[1]);
      return new InetSocketAddress(h, p);
    } else {
      System.out.println("Invalid host and port.");
      return getAddress();
    }
  }
}
