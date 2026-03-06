package com.example.helloworld;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final String URL = System.getenv().getOrDefault(
      "SPRING_DATASOURCE_URL", null
  );
  private static final String USER = System.getenv().getOrDefault(
      "SPRING_DATASOURCE_USERNAME", null
  );
  private static final String PASSWORD = System.getenv().getOrDefault(
      "SPRING_DATASOURCE_PASSWORD", null
  );

  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("PostgreSQL Driver not found", e);
    }
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USER, PASSWORD);
  }
}
