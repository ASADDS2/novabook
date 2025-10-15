/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.connection;

import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.infra.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Provides JDBC connections based on application.properties without modifying it.
 */
public class ConnectionFactory {
    private final AppConfig config;
    
    public ConnectionFactory(AppConfig config) { this.config = config;}
    
    public Connection open() throws DatabaseException{
        String vendor = trim(config.get("db.vendor"));
        String host = trim(config.get("db.host"));
        String port = trim(config.get("db.port"));
        String name = trim(config.get("db.name"));
        String user = trim(config.get("db.user"));
        String pass = trim(config.get("db.password"));
        String explicitUrl = trim(config.get("db.url"));
        String useSSL = trim(config.get("db.useSSL"));

        String url;
        try {
            if (vendor != null && vendor.equalsIgnoreCase("postgres")) {
                // Ensure driver is loaded for older environments
                try { Class.forName("org.postgresql.Driver"); } catch (ClassNotFoundException ignore) {}
            } else {
                try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignore) {}
            }

            if (explicitUrl != null && !explicitUrl.isBlank()) {
                url = explicitUrl;
            } else if (vendor != null && vendor.equalsIgnoreCase("postgres")) {
                url = String.format("jdbc:postgresql://%s:%s/%s", host, port, name);
            } else {
                // Default to MySQL-compatible URL honoring useSSL flag
                String ssl = (useSSL == null || useSSL.isBlank()) ? "false" : useSSL;
                url = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=UTC", host, port, name, ssl);
            }

            // Use Properties object to safely pass user/pass and optional flags without altering application.properties
            Properties props = new Properties();
            if (user != null) props.setProperty("user", user);
            if (pass != null) props.setProperty("password", pass);

            // For MySQL 8 with caching_sha2_password, allowPublicKeyRetrieval may be required when useSSL=false
            if (url.startsWith("jdbc:mysql:") && (useSSL == null || useSSL.equalsIgnoreCase("false"))) {
                // Only set property if not already present in URL query
                if (!url.contains("allowPublicKeyRetrieval=")) {
                    props.setProperty("allowPublicKeyRetrieval", "true");
                }
            }

            return DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            String safeUrl = (explicitUrl != null && !explicitUrl.isBlank()) ? explicitUrl : (vendor != null && vendor.equalsIgnoreCase("postgres")
                    ? String.format("jdbc:postgresql://%s:%s/%s", host, port, name)
                    : String.format("jdbc:mysql://%s:%s/%s", host, port, name));
            String detail = e.getMessage();
            throw new DatabaseException("Failed to connect to database (vendor=" + vendor + ", url=" + safeUrl + ") - " + detail, e);
        }
    }

    private String trim(String s) { return s == null ? null : s.trim(); }
}
