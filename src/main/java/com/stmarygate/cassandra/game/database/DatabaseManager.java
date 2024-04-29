package com.stmarygate.cassandra.game.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
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
}
