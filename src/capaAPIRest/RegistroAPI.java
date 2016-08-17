package capaAPIRest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import utilidades.Logging;
import capaDatos.DAOFactory;
import capaDatos.RegistroDAO;
import capaNegocio.Registro;
import capaNegocio.Usuario;

@Path("/registro")
public class RegistroAPI {
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
		Registro registro = null;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				JSONObject entradaJson = new JSONObject(entrada);
				registro = new Registro(entradaJson);
				RegistroDAO registroDAO = (RegistroDAO) daoFactory
						.obtenerDAO(registro.getClass().getSimpleName());
				int idRegistro = registroDAO.agregar(registro);
				resultado = true;
				mensaje = "Registro creado " + idRegistro;
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("registro creado " + resultado);
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
				.entity(obtenerRespuesta(resultado, mensaje, registro)).build();
	}

	@DELETE
	@Path("/eliminar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminar(@QueryParam("idRegistro") List<Integer> ids,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "Eliminacion fallida";
		logger.log(Level.INFO, "ids=" + ids);
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				for (int id : ids) {
					RegistroDAO registroDAO = (RegistroDAO) daoFactory
							.obtenerDAO(Registro.class.getSimpleName());
					resultado = registroDAO.eliminar(id);
					if (resultado)
						mensaje = "Registro eliminado";
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Registro eliminado " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido ", e);
			mensaje = "Error desconocido ";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje)).build();
	}

	@GET
	@Path("/mostrar/{idRegistro}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrar(@PathParam("idRegistro") Integer idRegistro,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.CREATED;
		Registro registro = null;
		String mensaje = "seleccion fallida";
		boolean resultado = false;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				RegistroDAO registroDAO = (RegistroDAO) daoFactory
						.obtenerDAO(Registro.class.getSimpleName());
				registro = registroDAO.mostrar(idRegistro);
				if (registro != null) {
					mensaje = "Registro seleccionado";
					resultado = true;
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Registro Seleccionado " + registro);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido ", e);
			mensaje = "Error desconocido ";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, registro)).build();
	}

	@GET
	@Path("/mostrarTodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrarTodo(@Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.CREATED;
		List<Registro> registros = null;
		String mensaje = "seleccionar todo fallido";
		boolean resultado = false;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				RegistroDAO registroDAO = (RegistroDAO) daoFactory
						.obtenerDAO(Registro.class.getSimpleName());
				registros = registroDAO.mostrarTodo();
				if (registros != null) {
					mensaje = "Registros seleccionados";
					resultado = true;
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Registros Seleccionados " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido ", e);
			mensaje = "Error desconocido ";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, registros))
				.build();
	}

	private String obtenerRespuestaDeRegistro(Registro registro) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta = registro.getJSON();

		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private String obtenerRespuesta(boolean resultado, String mensaje) {
		List<Registro> registros = new ArrayList<Registro>();
		return obtenerRespuesta(resultado, mensaje, registros);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			Registro registro) {
		List<Registro> registros = new ArrayList<Registro>();
		if (registro != null) {
			registros.add(registro);
		}
		return obtenerRespuesta(resultado, mensaje, registros);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			List<Registro> registros) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta.put("exitoso", resultado);
			respuesta.put("mensaje", mensaje);
			JSONArray jsonRegistro = new JSONArray();

			if (registros != null) {
				for (Registro registro : registros) {
					jsonRegistro.put(obtenerRespuestaDeRegistro(registro));
				}
			}
			respuesta.put("registros", jsonRegistro);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

}
