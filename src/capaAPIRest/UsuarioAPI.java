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
import capaDatos.UsuarioDAO;
import capaNegocio.Usuario;

@Path("/usuario")
public class UsuarioAPI {
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
		Usuario usuarioNuevo = null;
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
					usuarioNuevo = new Usuario(entradaJson);
					UsuarioDAO usuarioDAO = (UsuarioDAO) daoFactory
							.obtenerDAO(usuarioNuevo.getClass().getSimpleName());
					int idUsuario = usuarioDAO.agregar(usuarioNuevo);
					usuarioNuevo.setIdUsuario(idUsuario);
					resultado = true;
					mensaje = "usuario agregado";
					String descripcion = "usuario agregado: " + usuarioNuevo;
					Auxiliares.agregarRegistro(usuario, descripcion);
				} else {
					httpCode = Response.Status.FORBIDDEN;
					mensaje = "No tiene permiso";
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("usuario agregado " + resultado);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "entrada de JSON invalida", e);
			mensaje = "Entrada invalida";
			httpCode = Response.Status.BAD_REQUEST;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}

		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, usuarioNuevo))
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
				Usuario usuarioExistente = new Usuario(entradaJson);
				UsuarioDAO usuarioDAO = (UsuarioDAO) daoFactory
						.obtenerDAO(usuarioExistente.getClass().getSimpleName());
				resultado = usuarioDAO.actualizar(usuarioExistente);
				mensaje = "Usuario actualizado";
				String descripcion = "usuario actualizado: " + usuarioExistente;
				Auxiliares.agregarRegistro(usuario, descripcion);
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Usuario actualizado " + resultado);
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
	public Response eliminar(@QueryParam("idUsuario") List<Integer> ids,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		boolean resultado = false;
		String mensaje = "Eliminacion fallida";
		logger.log(Level.INFO, "ids= " + ids);
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {
				for (int id : ids) {
					UsuarioDAO usuarioDAO = (UsuarioDAO) daoFactory
							.obtenerDAO(Usuario.class.getSimpleName());
					Usuario usuarioEliminado = usuarioDAO.mostrar(id);
					resultado = usuarioDAO.eliminar(id);
					if (resultado)
						mensaje = "Usuario eliminado";
					String descripcion = "usuario eliminado: "
							+ usuarioEliminado;
					Auxiliares.agregarRegistro(usuario, descripcion);
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Usuario eliminado " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje)).build();
	}

	@GET
	@Path("/autenticar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response autenticar(@Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.ACCEPTED;
		String mensaje = "Busqueda fallida";
		boolean resultado = false;
		Usuario usuario = null;

		try {
			usuario = Auxiliares.autenticar(headers);

			if (usuario != null) {
				mensaje = "Usuario encontrado";
				resultado = true;
				Auxiliares.agregarRegistro(usuario, "Inició Sesión");
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Usuario no encontrado";
			}
			logger.info("Usuario encontrado " + usuario);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, usuario)).build();
	}

	@GET
	@Path("/mostrar/{idUsuario}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrar(@PathParam("idUsuario") Integer idUsuario,
			@Context HttpHeaders headers) throws SQLException {
		Response.Status httpCode = Response.Status.ACCEPTED;
		Usuario usuarioSeleccionado = null;
		String mensaje = "seleccion fallida";
		boolean resultado = false;
		logger.log(Level.INFO, "idUsuario= " + idUsuario);
		try {
			Usuario usuario = Auxiliares.autenticar(headers);
			if (usuario != null) {

				UsuarioDAO usuarioDAO = (UsuarioDAO) daoFactory
						.obtenerDAO(Usuario.class.getSimpleName());
				usuarioSeleccionado = usuarioDAO.mostrar(idUsuario);
				if (usuarioSeleccionado != null) {
					mensaje = "Usuario seleccionado";
					resultado = true;
				}
			} else {
				httpCode = Response.Status.UNAUTHORIZED;
				mensaje = "Autenticacion fallida";
			}
			logger.info("Usuario Seleccionado " + usuarioSeleccionado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response
				.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje,
						usuarioSeleccionado)).build();
	}

	@GET
	@Path("/mostrarTodo")
	@Produces(MediaType.APPLICATION_JSON)
	public Response mostrarTodo(@Context HttpHeaders headers) {
		Response.Status httpCode = Response.Status.ACCEPTED;
		List<Usuario> usuarios = null;
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
					UsuarioDAO usuarioDAO = (UsuarioDAO) daoFactory
							.obtenerDAO(Usuario.class.getSimpleName());
					usuarios = usuarioDAO.mostrarTodo();
					if (usuarios != null) {
						mensaje = "Usuarios seleccionados";
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
			logger.info("Usuarios Seleccionados " + resultado);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error desconocido", e);
			mensaje = "Error desconocido";
			httpCode = Response.Status.INTERNAL_SERVER_ERROR;
		}
		return Response.status(httpCode)
				.entity(obtenerRespuesta(resultado, mensaje, usuarios)).build();
	}

	private String obtenerRespuestaDeUsuario(Usuario usuario) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta = usuario.getJSON();

		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private String obtenerRespuesta(boolean resultado, String mensaje) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		return obtenerRespuesta(resultado, mensaje, usuarios);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			Usuario usuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		if (usuario != null) {
			usuarios.add(usuario);
		}
		return obtenerRespuesta(resultado, mensaje, usuarios);
	}

	private String obtenerRespuesta(boolean resultado, String mensaje,
			List<Usuario> usuarios) {
		JSONObject respuesta = new JSONObject();
		try {
			respuesta.put("exitoso", resultado);
			respuesta.put("mensaje", mensaje);
			JSONArray jsonUsuarios = new JSONArray();

			if (usuarios != null) {
				for (Usuario usuario : usuarios) {
					jsonUsuarios.put(obtenerRespuestaDeUsuario(usuario));
				}
			}
			respuesta.put("usuarios", jsonUsuarios);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error JSON ", e);
		}
		return respuesta.toString();
	}

	private Set<Operaciones> getLeerOperaciones() {
		HashSet<Operaciones> leerOperaciones = new HashSet<Operaciones>();
		leerOperaciones.add(Operaciones.PERMISO_TODO);
		leerOperaciones.add(Operaciones.EDITAR_USUARIO);
		leerOperaciones.add(Operaciones.MOSTRAR_USUARIO);
		return leerOperaciones;
	}

	private Set<Operaciones> getEscribirOperaciones() {
		HashSet<Operaciones> escribirOperaciones = new HashSet<Operaciones>();
		escribirOperaciones.add(Operaciones.PERMISO_TODO);
		escribirOperaciones.add(Operaciones.EDITAR_USUARIO);
		return escribirOperaciones;
	}
}
