package com.stmarygate.cassandra;

import io.github.cdimascio.dotenv.Dotenv;

/** This class contains all the constants for the Cassandra project. */
public class Constants {

  public static final int VERSION_MAJOR = 0;

  public static final int VERSION_MINOR = 0;

  public static final int VERSION_PATCH = 1;

  public static final String VERSION_BUILD = "SNAPSHOT";

  public static final Dotenv dotenv = Dotenv.load();
  public static Long getMaxTimeOutConnection = 10000L;

  /**
   * Get the formatted version.
   *
   * @return The version.
   */
  public static String getVersion() {
    return VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH + " (" + VERSION_BUILD + ")";
  }

  /**
   * Get the database host.
   *
   * @return The database host.
   */
  public static String getStorePass() {
    return dotenv.get("STOREPASS");
  }
}
