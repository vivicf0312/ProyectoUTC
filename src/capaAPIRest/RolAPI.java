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
import capaDatos.RolDAO;
import capaNegocio.Rol;
import capaNegocio.Usuario;

@Path("/rol")
public class RolAPI {
	Logger logger = Logging.obtenerServerLogger();
	DAOFactory daoFactory = DAOFactory.obtenerInstancia();

	@POST
	@Path("/agregar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response agregar(String entrada, @Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.CREATED;
		boolean resultado = false;
		String mensaje = "";
		logger.info(entrada);
		Rol rol = null;
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
					rol = new Rol(entradaJson);
					RolDAO rolDAO = (RolDAO) daoFactory.obtenerDAO(rol
							.getClass().getSimpleName());
					int idRol = rolDAO.agregar(rol);
					rol.setIdRol(idRol);
					resultado = true;
					mensaje = "Rol agregado";
					String descripcion = "rol agregado: " + rol;
					Auxiliares.agregarRegistro(usuario, descripcion);
				} else {
					httpCode = Response.Status.FORBIDDEN;
					mensaje = "No tiene permiso";
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("rol agregado " + resultado);
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
				.entity(obtenerRespuesta(resultado, mensaje, rol)).build();
	}

	@PUT
	@Path("/actualizar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizar(String entrada, @Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "";
		logger.info(entrada);
		Rol rol = null;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				JSONObject entradaJson = new JSONObject(entrada);
				HashSet<Operaciones> operaciones = new HashSet<Operaciones>();
				logger.log(
						Level.INFO,
						"operaciones="
								+ entradaJson.getJSONArray("operaciones"));

				JSONArray jsonOperaciones = entradaJson
						.getJSONArray("operaciones");
				for (int i = 0; i < jsonOperaciones.length(); i++) {
					operaciones.add(Operaciones.valueOf(jsonOperaciones
							.getString(i)));
				}
				rol = new Rol(entradaJson.getInt("idRol"),
						entradaJson.getString("descripcion"), operaciones);
				RolDAO rolDAO = (RolDAO) daoFactory.obtenerDAO(rol.getClass()
						.getSimpleName());
				resultado = rolDAO.actualizar(rol);
				mensaje = "Rol actualizado";
				String descripcion = "rol actualizado: " + rol;
				Auxiliares.agregarRegistro(usuario, descripcion);
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Rol actualizado " + resultado);
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
				.entity(obtenerRespuesta(resultado, mensaje, rol)).build();
	}

	@DELETE
	@Path("/eliminar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminar(@QueryParam("idRol") List<Integer> ids,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "Eliminacion fallida";
		logger.log(Level.INFO, "ids= " + ids);

		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				for (int id : ids) {
					RolDAO rolDAO = (RolDAO) daoFactory.obtenerDAO(Rol.class
							.getSimpleName());
					Rol rolEliminado = rolDAO.mostrar(id);
					resultado = rolDAO.eliminar(id);
					if (resultado)
						mensaje = "Rol eliminado";
					String descripcion = "rol eliminado: " + rolEliminado;
					Auxiliares.agregarRegistro(usuario, descripcion);
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Rol eliminado " + resultado);
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
	@Path("/mostrar/{idRol}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrar(@PathParam("idRol") Integer idRol,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		Rol rol = null;
		String mensaje = "seleccion fallida";
		boolean resultado = false;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				RolDAO rolDAO = (RolDAO) daoFactory.obtenerDAO(Rol.class
						.getSimpleName());
				rol = rolDAO.mostrar(idRol);
				if (rol != null) {
					mensaje = "Rol seleccionado";
					resultado = true;
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Rol Seleccionado " + rol);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, rol)).build();
	}

	@GET
	@Path("/mostrarTodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrarTodo(@Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.ACCEPTED;
		List<Rol> roles = null;
		String mensaje = "seleccionar todo fallido";
		boolean resultado = false;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				Set<Operaciones> operacionesPermitidas = Auxiliares
						.getOperations(usuario);
				logger.info("Operaciones permitidas =" + operacionesPermitidas);
				operacionesPermitidas.retainAll(getLeerOperaciones());
				logger.info("Operaciones permitidas =" + operacionesPermitidas);
				if (!operacionesPermitidas.isEmpty()) {
					RolDAO rolDAO = (RolDAO) daoFactory.obtenerDAO(Rol.class
							.getSimpleName());
					roles = rolDAO.mostrarTodo();
					if (roles != null) {
						mensaje = "Roles seleccionados";
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
			logger.info("Roles Seleccionados " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, roles)).build();
	}

	private String obtenerRespuestaDeRol(Rol rol) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta = rol.getJSON();
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private String obtenerRespuesta(boolean resultado, String mensaje) {
		List<Rol> roles = new ArrayList<Rol>();
		return obtenerRespuesta(resultado, mensaje, roles);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje, Rol rol) {
		List<Rol> roles = new ArrayList<Rol>();
		if (rol != null) {
			roles.add(rol);
		}
		return obtenerRespuesta(resultado, mensaje, roles);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			List<Rol> roles) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta.put("exitoso", resultado);
			respuesta.put("mensaje", mensaje);
			JSONArray jsonRoles = new JSONArray();

			if (roles != null) {
				for (Rol rol : roles) {
					jsonRoles.put(obtenerRespuestaDeRol(rol));
				}
			}
			respuesta.put("roles", jsonRoles);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private Set<Operaciones> getLeerOperaciones() {
		HashSet<Operaciones> leerOperaciones = new HashSet<Operaciones>();
		leerOperaciones.add(Operaciones.PERMISO_TODO);
		leerOperaciones.add(Operaciones.EDITAR_ROL);
		leerOperaciones.add(Operaciones.MOSTRAR_ROL);
		return leerOperaciones;
	}

	private Set<Operaciones> getEscribirOperaciones() {
		HashSet<Operaciones> escribirOperaciones = new HashSet<Operaciones>();
		escribirOperaciones.add(Operaciones.PERMISO_TODO);
		escribirOperaciones.add(Operaciones.EDITAR_ROL);
		return escribirOperaciones;
	}
}
