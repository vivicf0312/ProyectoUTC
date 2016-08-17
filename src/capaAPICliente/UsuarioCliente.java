package capaAPICliente;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import utilidades.Logging;
import utilidades.RequestFailureException;
import capaNegocio.Usuario;

public class UsuarioCliente extends AbstractCliente<Usuario> {

	public UsuarioCliente(String nombreUsuario, String clave, String serverIP) {
		super(nombreUsuario, clave, serverIP);
	}

	Logger logger = Logging.obtenerClientLogger();

	@Override
	public List<Usuario> mostrarTodo() throws RequestFailureException {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/usuario/mostrarTodo";
			JSONObject salidaJson = get(url);
			for (int i = 0; i < salidaJson.getJSONArray("usuarios").length(); i++) {
				JSONObject jsonUsuario = new JSONObject((String) salidaJson
						.getJSONArray("usuarios").get(i));
				Usuario usuario = new Usuario(jsonUsuario);
				usuarios.add(usuario);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return usuarios;
	}

	@Override
	public Usuario agregar(Usuario usuario) throws RequestFailureException {
		boolean resultado = false;
		Usuario usuarioCreado = null;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/usuario/agregar";
		try {
			JSONObject inputJsonUsuario = usuario.getJSON();
			JSONObject salidaJson = post(url, inputJsonUsuario);
			resultado = salidaJson.getBoolean("exitoso");
			if (resultado == true) {
				JSONObject jsonUsuario = new JSONObject((String) salidaJson
						.getJSONArray("usuarios").get(0));
				usuarioCreado = new Usuario(jsonUsuario);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return usuarioCreado;
	}

	@Override
	public boolean actualizar(Usuario usuario) throws RequestFailureException {
		boolean resultado = false;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/usuario/actualizar";
		try {
			JSONObject jsonUsuario = usuario.getJSON();
			JSONObject salidaJson = put(url, jsonUsuario);
			resultado = salidaJson.getBoolean("exitoso");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return resultado;
	}

	@Override
	public boolean eliminar(int id) throws RequestFailureException {
		String mensaje = null;
		boolean resultado = true;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/usuario/eliminar?idUsuario=" + id;
			JSONObject salidaJson = delete(url);
			mensaje = salidaJson.getString("mensaje");
			logger.log(Level.INFO, "Mensaje: ", mensaje);
			resultado = salidaJson.getBoolean("exitoso");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return resultado;
	}

	@Override
	public Usuario mostrar(int id) throws RequestFailureException {
		Usuario usuario = null;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/usuario/mostrar/" + id;
			JSONObject salidaJson = get(url);
			JSONObject jsonUsuario = new JSONObject((String) salidaJson
					.getJSONArray("usuarios").get(0));
			usuario = new Usuario(jsonUsuario);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return usuario;
	}

	public Usuario buscarUsuario() throws RequestFailureException {
		Usuario usuario = null;
		try {
			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/usuario/autenticar";
			JSONObject salidaJson = get(url);
			JSONObject jsonUsuario = new JSONObject((String) salidaJson
					.getJSONArray("usuarios").get(0));
			usuario = new Usuario(jsonUsuario);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return usuario;
	}
}
