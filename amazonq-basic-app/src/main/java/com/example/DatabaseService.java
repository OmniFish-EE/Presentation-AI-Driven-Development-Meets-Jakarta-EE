package com.example;

import java.sql.*;
import java.util.function.Function;

public class DatabaseService {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/persondb";
    private static final String DB_USER = "user";
    private static final String DB_PASSWORD = "password";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    public <T> T executeQuery(String sql, Function<ResultSet, T> mapper, Object... params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                return mapper.apply(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed: " + sql, e);
        }
    }

    public int executeUpdate(String sql, Object... params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setParameters(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Database update failed: " + sql, e);
        }
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
}
