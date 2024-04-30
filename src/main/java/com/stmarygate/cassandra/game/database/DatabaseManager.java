package com.stmarygate.cassandra.game.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
    query("CREATE TABLE IF NOT EXISTS settings (language TEXT, server_url TEXT, server_port INTEGER);");

    // Set first server URL
    query("INSERT INTO settings (server_url) VALUES ('http://localhost');");
    query("INSERT INTO settings (server_port) VALUES ('8080')");
    query("INSERT INTO settings (language) VALUES ('en')");
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
