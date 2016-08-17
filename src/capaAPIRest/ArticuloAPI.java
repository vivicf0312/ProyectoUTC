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
import capaDatos.ArticuloDAO;
import capaDatos.DAOFactory;
import capaNegocio.Articulo;
import capaNegocio.Usuario;

@Path("/articulo")
public class ArticuloAPI {
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
		Articulo articulo = null;
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
					articulo = new Articulo(entradaJson);
					ArticuloDAO articuloDAO = (ArticuloDAO) daoFactory
							.obtenerDAO(articulo.getClass().getSimpleName());
					int idArticulo = articuloDAO.agregar(articulo);
					articulo.setIdArticulo(idArticulo);
					resultado = true;
					mensaje = "articulo agregado";
					String descripcion = "Articulo agregado: " + articulo;
					Auxiliares.agregarRegistro(usuario, descripcion);
				} else {
					httpCode = Response.Status.FORBIDDEN;
					mensaje = "No tiene permiso";
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("articulo agregado " + resultado);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "entrada JSON invalida ", e);
			mensaje = "Entrada invalida";
			httpCode = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido ", e);
			mensaje = "Error desconocido ";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, articulo)).build();
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
				Articulo articulo = new Articulo(
						entradaJson.getInt("idArticulo"),
						entradaJson.getInt("tomo"),
						entradaJson.getInt("folio"),
						entradaJson.getInt("asiento"),
						entradaJson.getString("idEtiqueta"),
						entradaJson.getString("descripcion"),
						entradaJson.getString("marca"),
						entradaJson.getString("modelo"),
						entradaJson.getString("serie"),
						entradaJson.getString("condicion"),
						entradaJson.getInt("idUbicacion"),
						entradaJson.getString("adquisicion"),
						entradaJson.getString("observaciones"));
				ArticuloDAO articuloDAO = (ArticuloDAO) daoFactory
						.obtenerDAO(articulo.getClass().getSimpleName());
				resultado = articuloDAO.actualizar(articulo);
				mensaje = "Articulo actualizado";
				String descripcion = "articulo actualizado: " + articulo;
				Auxiliares.agregarRegistro(usuario, descripcion);
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Articulo actualizado " + resultado);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "entrada JSON invalida ", e);
			mensaje = "Entrada invalida";
			httpCode = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido ", e);
			mensaje = "Error desconocido ";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje)).build();
	}

	@DELETE
	@Path("/eliminar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response eliminar(@QueryParam("idArticulo") List<Integer> ids,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "Eliminacion fallida";
		logger.log(Level.INFO, "ids=" + ids);
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				for (int id : ids) {
					ArticuloDAO articuloDAO = (ArticuloDAO) daoFactory
							.obtenerDAO(Articulo.class.getSimpleName());
					Articulo articuloEliminado = articuloDAO.mostrar(id);
					resultado = articuloDAO.eliminar(id);
					if (resultado)
						mensaje = "Articulo eliminado";
					String descripcion = "articulo eliminado: "
							+ articuloEliminado;
					Auxiliares.agregarRegistro(usuario, descripcion);
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Articulo eliminado " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje)).build();
	}

	@GET
	@Path("/mostrar/{idArticulo}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrar(@PathParam("idArticulo") Integer idArticulo,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		Articulo articulo = null;
		String mensaje = "seleccion fallida";
		boolean resultado = false;
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				ArticuloDAO articuloDAO = (ArticuloDAO) daoFactory
						.obtenerDAO(Articulo.class.getSimpleName());
				articulo = articuloDAO.mostrar(idArticulo);
				if (articulo != null) {
					mensaje = "Articulo seleccionado";
					resultado = true;
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Articulo Seleccionado " + articulo);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, articulo)).build();
	}

	@GET
	@Path("/mostrarTodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrarTodo(@Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.ACCEPTED;
		List<Articulo> articulos = null;
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
					ArticuloDAO articuloDAO = (ArticuloDAO) daoFactory
							.obtenerDAO(Articulo.class.getSimpleName());
					articulos = articuloDAO.mostrarTodo();
					if (articulos != null) {
						mensaje = "Articulos seleccionados";
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
			logger.info("Articulos Seleccionados " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, articulos))
				.build();
	}

	private String obtenerRespuestaDeArticulos(Articulo articulo) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta = articulo.getJSON();
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private String obtenerRespuesta(boolean resultado, String mensaje) {
		List<Articulo> articulos = new ArrayList<Articulo>();
		return obtenerRespuesta(resultado, mensaje, articulos);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			Articulo articulo) {
		List<Articulo> articulos = new ArrayList<Articulo>();
		if (articulo != null) {
			articulos.add(articulo);
		}
		return obtenerRespuesta(resultado, mensaje, articulos);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			List<Articulo> articulos) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta.put("exitoso", resultado);
			respuesta.put("mensaje", mensaje);
			JSONArray jsonArticulos = new JSONArray();

			if (articulos != null) {
				for (Articulo articulo : articulos) {
					jsonArticulos.put(obtenerRespuestaDeArticulos(articulo));
				}
			}
			respuesta.put("articulos", jsonArticulos);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private Set<Operaciones> getLeerOperaciones() {
		HashSet<Operaciones> leerOperaciones = new HashSet<Operaciones>();
		leerOperaciones.add(Operaciones.PERMISO_TODO);
		leerOperaciones.add(Operaciones.EDITAR_ARTICULO);
		leerOperaciones.add(Operaciones.MOSTRAR_ARTICULO);
		return leerOperaciones;
	}

	private Set<Operaciones> getEscribirOperaciones() {
		HashSet<Operaciones> escribirOperaciones = new HashSet<Operaciones>();
		escribirOperaciones.add(Operaciones.PERMISO_TODO);
		escribirOperaciones.add(Operaciones.EDITAR_ARTICULO);
		return escribirOperaciones;
	}
}
