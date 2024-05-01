package com.stmarygate.cassandra.application.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class DatabaseManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
  private static Connection c = null;

  /**
   * Retrieve or build the Hibernate session.
   */
  public static void initialize() {
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:database.db");
      System.out.println("Opened database successfully");
      initializeTables();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
  }

  /**
   * Initialize all tables.
   */
  public static void initializeTables() {
    query("CREATE TABLE IF NOT EXISTS settings (language TEXT, server_url TEXT, server_port " +
            "INTEGER, username TEXT, password TEXT);");

    // Check if the settings table is empty.
    if (Objects.equals(queryResult("SELECT COUNT(*) FROM settings"), "0")) {
      query("INSERT INTO settings (language, server_url, server_port, username, password) " +
              "VALUES ('en', 'localhost', 8080, 'admin', 'admin');");
    }
  }

  /**
   * Execute a query.
   * @param query The query to execute.
   */
  public static void query(String query) {
    try {
      Statement stmt = c.createStatement();
      stmt.executeUpdate(query);
      stmt.close();
    } catch (Exception e) {
      LOGGER.error("Failed to execute query: " + query, e);
    }
  }

  /**
   * Execute a query and return the result.
   *
   * @param query The query to execute.
   */
  public static String queryResult(String query) {
    try {
      Statement stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      String result = rs.getString(1);
      rs.close();
      stmt.close();
      return result;
    } catch (Exception e) {
      LOGGER.error("Failed to execute query: " + query, e);
      return null;
    }
  }
}
