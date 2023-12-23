package com.stmarygate.cassandra.utils;

public class Utils {
  /**
   * Wait the specified amount of time.
   *
   * @param ms The amount of time to wait in milliseconds.
   */
  public static void wait(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
