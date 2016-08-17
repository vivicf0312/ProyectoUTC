package capaAPIRest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import utilidades.Auxiliares;
import utilidades.Enumeracion.Operaciones;
import utilidades.Logging;
import capaDatos.DAOFactory;
import capaDatos.UbicacionDAO;
import capaNegocio.Ubicacion;
import capaNegocio.Usuario;

@Path("/ubicacion")
public class UbicacionAPI {
	Logger logger = Logging.obtenerServerLogger();
	DAOFactory daoFactory = DAOFactory.obtenerInstancia();

	@POST
	@Path("/agregar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response agregar(String entrada, @Context HttpHeaders headers) {
		boolean resultado = false;
		String mensaje = "";
		Response.Status httpCode = Response.Status.CREATED;
		logger.info(entrada);
		Ubicacion ubicacion = null;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				Set<Operaciones> operacionesPermitidas = Auxiliares
						.getOperations(usuario);
				logger.info("Operaciones permitidas =" + operacionesPermitidas);
				operacionesPermitidas.retainAll(getEscribirOperaciones());
				logger.info("Operaciones permitidas =" + operacionesPermitidas);
				if (!operacionesPermitidas.isEmpty()) {
					JSONObject entradaJson = new JSONObject(entrada);
					ubicacion = new Ubicacion(entradaJson);
					UbicacionDAO ubicacionDAO = (UbicacionDAO) daoFactory
							.obtenerDAO(ubicacion.getClass().getSimpleName());
					int idUbicacion = ubicacionDAO.agregar(ubicacion);
					ubicacion.setIdUbicacion(idUbicacion);
					resultado = true;
					mensaje = "Ubicacion agregada";
					String descripcion = "ubicacion agregada: " + ubicacion;
					Auxiliares.agregarRegistro(usuario, descripcion);
				} else {
					httpCode = Response.Status.FORBIDDEN;
					mensaje = "No tiene permiso";
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Ubicacion agregada " + resultado);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Entrada JSON invalida", e);
			mensaje = "Entrada invalida";
			httpCode = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, ubicacion))
				.build();
	}

	@PUT
	@Path("/actualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizar(String entrada, @Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "";
		logger.info(entrada);
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				JSONObject entradaJson = new JSONObject(entrada);
				Ubicacion ubicacion = new Ubicacion(
						entradaJson.getInt("idUbicacion"),
						entradaJson.getString("descripcion"));
				UbicacionDAO ubicacionDAO = (UbicacionDAO) daoFactory
						.obtenerDAO(ubicacion.getClass().getSimpleName());
				resultado = ubicacionDAO.actualizar(ubicacion);
				mensaje = "Ubicacion actualizada";
				String descripcion = "ubicacion actualizada: " + ubicacion;
				Auxiliares.agregarRegistro(usuario, descripcion);
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Ubicacion actualizada " + resultado);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "entrada JSON invalida", e);
			mensaje = "Entrada invalida";
			httpCode = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje)).build();
	}

	@DELETE
	@Path("/eliminar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminar(@QueryParam("idUbicacion") List<Integer> ids,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "eliminacion fallida";
		logger.log(Level.INFO, "idUbicacion= " + ids);
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				for (int id : ids) {
					UbicacionDAO ubicacionDAO = (UbicacionDAO) daoFactory
							.obtenerDAO(Ubicacion.class.getSimpleName());
					Ubicacion ubicacionEliminada = ubicacionDAO.mostrar(id);
					resultado = ubicacionDAO.eliminar(id);
					if (resultado)
						mensaje = "Ubicacion eliminada";
					String descripcion = "ubicacion eliminada: "
							+ ubicacionEliminada;
					Auxiliares.agregarRegistro(usuario, descripcion);
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Ubicacion eliminada " + resultado);

		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = e.getMessage();
			httpCode = Response.Status.PRECONDITION_FAILED;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje)).build();
	}

	@GET
	@Path("/mostrar/{idUbicacion}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrar(@PathParam("idUbicacion") Integer idUbicacion,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		Ubicacion ubicacion = null;
		String mensaje = "seleccion fallida";
		boolean resultado = false;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				UbicacionDAO ubicacionDAO = (UbicacionDAO) daoFactory
						.obtenerDAO(Ubicacion.class.getSimpleName());
				ubicacion = ubicacionDAO.mostrar(idUbicacion);
				if (ubicacion != null) {
					mensaje = "Ubicacion seleccionada";
					resultado = true;
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Ubicacion Seleccionada " + ubicacion);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido ", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, ubicacion))
				.build();
	}

	@GET
	@Path("/mostrarTodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrarTodo(@Context HttpHeaders headers) {

		List<Ubicacion> ubicaciones = null;
		String mensaje = "seleccionar todo fallido";
		boolean resultado = false;
		Response.Status httpCode = Response.Status.ACCEPTED;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				Set<Operaciones> operacionesPermitidas = Auxiliares
						.getOperations(usuario);
				logger.info("Operaciones permitidas =" + operacionesPermitidas);
				operacionesPermitidas.retainAll(getLeerOperaciones());
				logger.info("Operaciones permitidas =" + operacionesPermitidas);
				if (!operacionesPermitidas.isEmpty()) {
					UbicacionDAO ubicacionDAO = (UbicacionDAO) daoFactory
							.obtenerDAO(Ubicacion.class.getSimpleName());
					ubicaciones = ubicacionDAO.mostrarTodo();
					if (ubicaciones != null) {
						mensaje = "Ubicacion seleccionada";
						resultado = true;
					}
				} else {
					httpCode = Response.Status.FORBIDDEN;
					mensaje = "Operacion no permitida";
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Ubicaciones Seleccionadas " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, ubicaciones))
				.build();
	}

	private String obtenerRespuestaDeUbicaciones(Ubicacion ubicacion) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta = ubicacion.getJSON();
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private String obtenerRespuesta(boolean resultado, String mensaje) {
		List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
		return obtenerRespuesta(resultado, mensaje, ubicaciones);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			Ubicacion ubicacion) {
		List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
		if (ubicacion != null) {
			ubicaciones.add(ubicacion);
		}
		return obtenerRespuesta(resultado, mensaje, ubicaciones);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			List<Ubicacion> ubicaciones) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta.put("exitoso", resultado);
			respuesta.put("mensaje", mensaje);
			JSONArray jsonUbicaciones = new JSONArray();

			if (ubicaciones != null) {
				for (Ubicacion ubicacion : ubicaciones) {
					jsonUbicaciones
							.put(obtenerRespuestaDeUbicaciones(ubicacion));
				}
			}
			respuesta.put("ubicaciones", jsonUbicaciones);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private Set<Operaciones> getLeerOperaciones() {
		HashSet<Operaciones> leerOperaciones = new HashSet<Operaciones>();
		leerOperaciones.add(Operaciones.PERMISO_TODO);
		leerOperaciones.add(Operaciones.EDITAR_UBICACION);
		leerOperaciones.add(Operaciones.MOSTRAR_UBICACION);
		return leerOperaciones;
	}

	private Set<Operaciones> getEscribirOperaciones() {
		HashSet<Operaciones> escribirOperaciones = new HashSet<Operaciones>();
		escribirOperaciones.add(Operaciones.PERMISO_TODO);
		escribirOperaciones.add(Operaciones.EDITAR_UBICACION);
		return escribirOperaciones;
	}
}
