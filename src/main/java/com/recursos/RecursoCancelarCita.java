package com.recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.dao.DAOFactory;
import model.entity.Cita;
import model.entity.Disponibilidad;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * API REST para Cancelar Citas - VERSIÓN SIMPLIFICADA
 * Basado estrictamente en el diagrama de robustez
 * 
 * ENDPOINTS ESENCIALES:
 * 1. GET  /api/citas/{idCita}/cancelar/confirmacion - Obtener confirmación (Paso 1-2)
 * 2. POST /api/citas/{idCita}/cancelar              - Ejecutar cancelación (Paso 3-6)
 */
@Path("/api/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecursoCancelarCita {
    
    private DAOFactory factory;
    private Gson gson;
    
    public RecursoCancelarCita() {
        this.factory = DAOFactory.getFactory();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> 
                    context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, typeOfSrc, context) -> 
                    context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .create();
    }
    
    /**
     * PASO 1-2: mostrarConfirmacion
     * Obtiene los datos de la cita para mostrar confirmación antes de cancelar
     * GET /api/citas/{idCita}/cancelar/confirmacion
     */
    @GET
    @Path("/{idCita}/cancelar/confirmacion")
    public Response obtenerConfirmacionCancelacion(@PathParam("idCita") int idCita) {
        try {
            Cita cita = factory.getCitaDAO().getById(idCita);
            
            if (cita == null) {
                return error(404, "Cita no encontrada");
            }
            
            if ("Cancelada".equals(cita.getEstadoCita())) {
                return error(400, "La cita ya está cancelada");
            }
            
            // Datos mínimos para confirmación
            Map<String, Object> datos = new HashMap<>();
            datos.put("idCita", cita.getIdCita());
            datos.put("fecha", cita.getFechaCita());
            datos.put("hora", cita.getHoraCita());
            datos.put("doctor", cita.getDoctor() != null ? cita.getDoctor().getNombreCompleto() : null);
            datos.put("especialidad", cita.getEspecialidad() != null ? cita.getEspecialidad().getNombre() : null);
            
            return exito(datos);
            
        } catch (Exception e) {
            return error(500, "Error: " + e.getMessage());
        }
    }
    
    /**
     * PASO 3-6: confirmarCancelacion, actualizarEstadoCita, liberarHorario, mostrarExitoCancelar
     * Ejecuta la cancelación completa de la cita
     * POST /api/citas/{idCita}/cancelar
     */
    @POST
    @Path("/{idCita}/cancelar")
    public Response confirmarCancelacion(@PathParam("idCita") int idCita) {
        try {
            // Obtener la cita
            Cita cita = factory.getCitaDAO().getById(idCita);
            
            if (cita == null) {
                return error(404, "Cita no encontrada");
            }
            
            if ("Cancelada".equals(cita.getEstadoCita())) {
                return error(400, "La cita ya está cancelada");
            }
            
            // PASO 4: actualizarEstadoCita(idCita)
            cita.setEstadoCita("Cancelada");
            factory.getCitaDAO().update(cita);
            
            // PASO 5: liberarHorario(idHorario)
            boolean horarioLiberado = false;
            if (cita.getDoctor() != null) {
                Disponibilidad disponibilidad = factory.getDisponibilidadDAO()
                    .obtenerPorDoctorYFechaYHora(
                        cita.getDoctor().getIdDoctor(),
                        cita.getFechaCita(),
                        cita.getHoraCita()
                    );
                
                if (disponibilidad != null) {
                    disponibilidad.setDisponible(true);
                    factory.getDisponibilidadDAO().update(disponibilidad);
                    horarioLiberado = true;
                }
            }
            
            // PASO 6: mostrarExitoCancelar
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("idCita", cita.getIdCita());
            resultado.put("estado", "Cancelada");
            resultado.put("horarioLiberado", horarioLiberado);
            resultado.put("mensaje", "Cita cancelada exitosamente");
            
            return exito(resultado);
            
        } catch (Exception e) {
            return error(500, "Error al cancelar: " + e.getMessage());
        }
    }
    
    // ===== MÉTODOS AUXILIARES =====
    
    private Response exito(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        return Response.ok(gson.toJson(response)).build();
    }
    
    private Response error(int status, String mensaje) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", mensaje);
        return Response.status(status).entity(gson.toJson(response)).build();
    }
}
