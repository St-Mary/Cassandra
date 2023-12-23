package com.stmarygate.cassandra.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class ConsoleWindow {
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
  }

  public static SocketAddress getAddress() {
    System.out.println("Enter the host and port to connect to (host:port): ");
    String hostAndPort = readLine();
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

  public static String readLine() {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.print("> ");
      return reader.readLine();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
