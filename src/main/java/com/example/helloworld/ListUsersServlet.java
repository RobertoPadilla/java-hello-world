package com.example.helloworld;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/api/users")
public class ListUsersServlet extends HttpServlet {
  
  private static final int PAGE_SIZE = 10;
  private final Gson gson = new Gson();
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    
    resp.setContentType("application/json; charset=UTF-8");
    resp.setHeader("Access-Control-Allow-Origin", "*");
    
    String pageParam = req.getParameter("page");
    int page = 1;
    
    if (pageParam != null) {
      try {
        page = Integer.parseInt(pageParam);
        if (page < 1) page = 1;
      } catch (NumberFormatException e) {
        page = 1;
      }
    }
    
    int offset = (page - 1) * PAGE_SIZE;
    
    try (Connection conn = DatabaseConnection.getConnection()) {
      // Contar total de usuarios
      int totalUsers = 0;
      String countSql = "SELECT COUNT(*) FROM usuarios";
      try (PreparedStatement stmt = conn.prepareStatement(countSql);
           ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          totalUsers = rs.getInt(1);
        }
      }
      
      // Obtener usuarios de la página actual
      List<Map<String, Object>> users = new ArrayList<>();
      String sql = "SELECT nombre, edad FROM usuarios ORDER BY nombre LIMIT ? OFFSET ?";
      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, PAGE_SIZE);
        stmt.setInt(2, offset);
        
        try (ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            Map<String, Object> user = new HashMap<>();
            user.put("nombre", rs.getString("nombre"));
            user.put("edad", rs.getInt("edad"));
            users.add(user);
          }
        }
      }
      
      // Preparar respuesta
      Map<String, Object> response = new HashMap<>();
      response.put("users", users);
      response.put("currentPage", page);
      response.put("totalPages", (int) Math.ceil((double) totalUsers / PAGE_SIZE));
      response.put("totalUsers", totalUsers);
      response.put("pageSize", PAGE_SIZE);
      
      resp.getWriter().println(gson.toJson(response));
      
    } catch (SQLException e) {
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      Map<String, String> error = new HashMap<>();
      error.put("error", "Error al consultar la base de datos: " + e.getMessage());
      resp.getWriter().println(gson.toJson(error));
    }
  }
}
