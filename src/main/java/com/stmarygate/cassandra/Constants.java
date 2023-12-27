package com.stmarygate.cassandra;

public class Constants {
  public static final int VERSION_MAJOR = 0;
  public static final int VERSION_MINOR = 0;
  public static final int VERSION_PATCH = 1;
  public static final String VERSION_BUILD = "SNAPSHOT";

  public static String getVersion() {
    return VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_PATCH + " (" + VERSION_BUILD + ")";
  }
}