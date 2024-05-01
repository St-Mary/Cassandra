package com.stmarygate.cassandra;

public class Utils {

  public static void wait(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
