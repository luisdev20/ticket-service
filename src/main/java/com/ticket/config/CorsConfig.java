package com.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración global de CORS para permitir peticiones desde aplicaciones
 * Flutter.
 * Esta configuración es esencial para desarrollo y producción de aplicaciones
 * móviles.
 * 
 * @author Sistema de Tickets
 * @version 1.0
 */
@Configuration
public class CorsConfig {

    /**
     * Configura el filtro CORS global para toda la aplicación.
     * Permite peticiones desde cualquier origen durante el desarrollo.
     * 
     * IMPORTANTE: En producción, reemplaza "*" con los dominios específicos de tu
     * app.
     * 
     * @return CorsFilter configurado
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Permite credenciales (cookies, headers de autorización)
        config.setAllowCredentials(true);

        // Orígenes permitidos - En producción, especifica tus dominios exactos
        // Ejemplo: config.setAllowedOrigins(Arrays.asList("https://miapp.com",
        // "http://localhost:3000"));
        config.setAllowedOriginPatterns(List.of("*")); // Permite todos los orígenes

        // Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
                "Origin",
                "Content-Type",
                "Accept",
                "Authorization",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));

        // Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"));

        // Headers expuestos al cliente
        config.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"));

        // Tiempo de cache para preflight requests (en segundos)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
