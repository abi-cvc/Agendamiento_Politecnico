package com.recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE DE PRUEBA SIMPLE PARA DIAGNOSTICAR ERROR 404
 * 
 * Esta clase NO usa DAOs, solo devuelve datos estáticos
 * para verificar que Jersey esté funcionando correctamente.
 */
@Path("/api/prueba")
@Produces(MediaType.APPLICATION_JSON)
public class RecursoPrueba {
    
    /**
     * TEST 1: Endpoint más simple posible
     * URL: http://localhost:8080/01_MiProyecto/rest/api/prueba/hola
     */
    @GET
    @Path("/hola")
    public Response testHola() {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "¡Hola! Jersey está funcionando correctamente");
        respuesta.put("timestamp", System.currentTimeMillis());
        respuesta.put("estado", "OK");
        
        return Response.ok(respuesta).build();
    }
    
    /**
     * TEST 2: Devolver una lista simple
     * URL: http://localhost:8080/01_MiProyecto/rest/api/prueba/lista
     */
    @GET
    @Path("/lista")
    public Response testLista() {
        List<Map<String, Object>> lista = new ArrayList<>();
        
        // Emular una lista de estudiantes
        Map<String, Object> estudiante1 = new HashMap<>();
        estudiante1.put("id", 1);
        estudiante1.put("nombre", "Juan Pérez");
        estudiante1.put("email", "juan.perez@epn.edu.ec");
        lista.add(estudiante1);
        
        Map<String, Object> estudiante2 = new HashMap<>();
        estudiante2.put("id", 2);
        estudiante2.put("nombre", "María López");
        estudiante2.put("email", "maria.lopez@epn.edu.ec");
        lista.add(estudiante2);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("total", lista.size());
        respuesta.put("data", lista);
        
        return Response.ok(respuesta).build();
    }
    
    /**
     * TEST 3: Endpoint con parámetro en la URL
     * URL: http://localhost:8080/01_MiProyecto/rest/api/prueba/usuario/123
     */
    @GET
    @Path("/usuario/{id}")
    public Response testUsuario(@PathParam("id") int id) {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("id", id);
        usuario.put("nombre", "Usuario #" + id);
        usuario.put("email", "usuario" + id + "@epn.edu.ec");
        usuario.put("activo", true);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("mensaje", "Usuario obtenido correctamente");
        respuesta.put("data", usuario);
        
        return Response.ok(respuesta).build();
    }
    
    /**
     * TEST 4: Endpoint POST simple
     * URL: http://localhost:8080/01_MiProyecto/rest/api/prueba/crear
     */
    @POST
    @Path("/crear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response testCrear(Map<String, Object> datos) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("mensaje", "Datos recibidos correctamente");
        respuesta.put("datosRecibidos", datos);
        respuesta.put("timestamp", System.currentTimeMillis());
        
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }
    
    /**
     * TEST 5: Emular obtener una lista de citas (SIN DAO)
     * URL: http://localhost:8080/01_MiProyecto/rest/api/prueba/citas
     */
    @GET
    @Path("/citas")
    public Response testCitas() {
        List<Map<String, Object>> citas = new ArrayList<>();
        
        // Cita 1
        Map<String, Object> cita1 = new HashMap<>();
        cita1.put("idCita", 1);
        cita1.put("fecha", "2026-01-20");
        cita1.put("hora", "09:00:00");
        cita1.put("estado", "Agendada");
        cita1.put("paciente", "Juan Pérez");
        cita1.put("doctor", "Dr. María González");
        cita1.put("especialidad", "Medicina General");
        citas.add(cita1);
        
        // Cita 2
        Map<String, Object> cita2 = new HashMap<>();
        cita2.put("idCita", 2);
        cita2.put("fecha", "2026-01-21");
        cita2.put("hora", "10:00:00");
        cita2.put("estado", "Agendada");
        cita2.put("paciente", "María López");
        cita2.put("doctor", "Dr. Carlos Ramírez");
        cita2.put("especialidad", "Psicología");
        citas.add(cita2);
        
        // Cita 3
        Map<String, Object> cita3 = new HashMap<>();
        cita3.put("idCita", 3);
        cita3.put("fecha", "2026-01-22");
        cita3.put("hora", "14:00:00");
        cita3.put("estado", "Cancelada");
        cita3.put("paciente", "Carlos Martínez");
        cita3.put("doctor", "Dra. Ana Pérez");
        cita3.put("especialidad", "Odontología");
        citas.add(cita3);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("success", true);
        respuesta.put("total", citas.size());
        respuesta.put("data", citas);
        
        return Response.ok(respuesta).build();
    }
    
    /**
     * TEST 6: Endpoint raíz para verificar que el recurso está cargado
     * URL: http://localhost:8080/01_MiProyecto/rest/api/prueba
     */
    @GET
    public Response testRoot() {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "API de Prueba activa");
        respuesta.put("version", "1.0");
        respuesta.put("endpoints", new String[]{
            "GET /api/prueba/hola",
            "GET /api/prueba/lista",
            "GET /api/prueba/usuario/{id}",
            "POST /api/prueba/crear",
            "GET /api/prueba/citas",
            "GET /api/prueba"
        });
        
        return Response.ok(respuesta).build();
    }
}
