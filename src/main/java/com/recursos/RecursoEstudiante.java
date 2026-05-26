package com.recursos;

import java.util.List;

import model.dao.DAOFactory;
import model.entity.Estudiante;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Recurso REST para gestión de estudiantes
 * Expone directamente los métodos del DAO utilizados en GestionarEstudiantesController
 * 
 * Flujo según diagrama de robustez CU07:
 * - 1.1: obtener(): estudiantes[] -> GET /estudiantes
 * - 1.6: crearNuevoEstudiante() -> POST /estudiantes/add
 * - 2.3: cambiarEstadoEstudiante() -> PUT /estudiantes/update
 * - 3.1: obtenerEstudiante() -> GET /estudiantes/{id} o /estudiantes/buscar/{cedula}
 * - 3.4: guardarEstudiante() -> PUT /estudiantes/update
 */
@Path("/estudiantes")
public class RecursoEstudiante {

	/**
	 * 1.1: obtener(): estudiantes[]
	 * GET /estudiantes
	 * Equivale a: gestionarEstudiantes() -> obtener()
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Estudiante> getEstudiantes() {
		return DAOFactory.getFactory().getEstudianteDAO().getAll();
	}

	/**
	 * 3.1: obtenerEstudiante(id)
	 * GET /estudiantes/{id}
	 * Equivale a: editarEstudiante() -> obtenerEstudiante()
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Estudiante getEstudiantePorId(@PathParam("id") int id) {
		return DAOFactory.getFactory().getEstudianteDAO().getById(id);
	}

	/**
	 * 3.1 y 2: obtenerEstudiante(cedula) y desactivarEstudiante()
	 * GET /estudiantes/buscar/{cedula}
	 * Equivale a: buscarEstudiante() -> buscarPorIdPaciente()
	 */
	@GET
	@Path("/buscar/{cedula}")
	@Produces(MediaType.APPLICATION_JSON)
	public Estudiante getEstudiantePorCedula(@PathParam("cedula") String cedula) {
		return DAOFactory.getFactory().getEstudianteDAO().buscarPorIdPaciente(cedula);
	}

	/**
	 * 1.5-1.6: creaNuevoEstudiante() -> crearNuevoEstudiante()
	 * POST /estudiantes/add
	 * Equivale a: solicitarNuevoEstudiante() -> crearNuevoEstudiante()
	 */
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public void guardarEstudiante(Estudiante estudiante) {
		DAOFactory.getFactory().getEstudianteDAO().create(estudiante);
	}

	/**
	 * 3.3-3.4: actualizarDatos() -> guardarEstudiante()
	 * Incluye también 2.3: cambiarEstadoEstudiante()
	 * PUT /estudiantes/update
	 * Equivale a: actualizarEstudiante() -> update()
	 */
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void actualizarEstudiante(Estudiante estudiante) {
		DAOFactory.getFactory().getEstudianteDAO().update(estudiante);
	}

	/**
	 * Eliminar estudiante por ID
	 * DELETE /estudiantes/delete/{id}
	 * Método adicional para completar CRUD
	 */
	@DELETE
	@Path("/delete/{id}")
	public void eliminarEstudiante(@PathParam("id") int id) {
		DAOFactory.getFactory().getEstudianteDAO().delete(id);
	}
}
