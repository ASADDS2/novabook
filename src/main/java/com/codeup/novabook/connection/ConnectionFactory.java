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

/**
 *
 * @author Coder
 */
public class ConnectionFactory {
    private final AppConfig config;
    
    public ConnectionFactory(AppConfig config) { this.config = config;}
    
    public Connection open() throws DatabaseException{
        String vendor = config.get("db.vendor");
        String host = config.get("db.host");
        String port = config.get("db.port");
        String name = config.get("db.name");
        String user = config.get("db.user");
        String pass = config.get("db.password");
        
        
        String url;
        if ("postgres".equalsIgnoreCase(vendor)) {
            url = String.format("jdbc:postgresql://%s:%s/%s", host, port, name);
        } else {
            String useSSL = config.get("db.useSSL");
            url = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=UTC", host, port, name, useSSL);
        }
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }
    
}
