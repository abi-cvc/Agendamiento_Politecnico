package com.recursos;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configurador de Jackson para el servicio REST
 * 
 * Propósito:
 * 1. Registrar módulo JSR310 para soportar java.time (LocalDate, LocalTime, LocalDateTime)
 * 2. Configurar serialización de fechas en formato ISO-8601 (no timestamps)
 * 
 * Esta clase es detectada automáticamente por Jersey gracias a la anotación @Provider
 */
@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JacksonConfig() {
        mapper = new ObjectMapper();
        
        // Registrar módulo para soportar java.time.*
        mapper.registerModule(new JavaTimeModule());
        
        // Configurar para NO escribir fechas como timestamps (usar ISO-8601)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        System.out.println("✅ Jackson configurado con soporte para java.time (LocalDate, LocalTime, LocalDateTime)");
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
