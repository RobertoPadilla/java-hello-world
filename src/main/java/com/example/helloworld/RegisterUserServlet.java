package com.example.helloworld;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/api/register")
public class RegisterUserServlet extends HttpServlet {
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    
    resp.setContentType("application/json; charset=UTF-8");
    resp.setHeader("Access-Control-Allow-Origin", "*");
    
    String nombre = req.getParameter("nombre");
    String edadStr = req.getParameter("edad");

    System.out.println("Received registration: nombre=" + nombre + ", edad=" + edadStr);
    
    if (nombre == null || nombre.trim().isEmpty() || edadStr == null) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().println("{\"error\": \"Nombre y edad son requeridos\"}");
      return;
    }
    
    try {
      int edad = Integer.parseInt(edadStr);
      
      if (edad < 0 || edad > 150) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().println("{\"error\": \"Edad debe estar entre 0 y 150\"}");
        return;
      }
      
      try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "INSERT INTO usuarios (nombre, edad) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          stmt.setString(1, nombre.trim());
          stmt.setInt(2, edad);
          stmt.executeUpdate();
        }
      }
      
      resp.setStatus(HttpServletResponse.SC_CREATED);
      resp.getWriter().println("{\"message\": \"Usuario registrado exitosamente\"}");
      
    } catch (NumberFormatException e) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().println("{\"error\": \"Edad debe ser un número válido\"}");
    } catch (SQLException e) {
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.getWriter().println("{\"error\": \"Error al guardar en la base de datos: " + e.getMessage() + "\"}");
    }
  }
  
  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setHeader("Access-Control-Allow-Origin", "*");
    resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    resp.setStatus(HttpServletResponse.SC_OK);
  }
}
