/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.infra.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Coder
 */
public class AppConfig {
    private final Properties properties = new Properties();
    
    public AppConfig (){
            try (InputStream in = getClass().getResourceAsStream("/application.properties")){
                if (in == null) {
                    throw new IllegalStateException("Application.properties was not found");
                }
                properties.load(in);
            } catch (IOException e) {
                throw new RuntimeException ("Configuration can not be loaded", e);
            }
}
    public String get(String key) {  return properties.getProperty(key);}
}
