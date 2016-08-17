package capaAPICliente;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaNegocio.Rol;

public class RolCliente extends AbstractCliente<Rol> {

	public RolCliente(String nombreUsuario, String clave, String serverIP) {
		super(nombreUsuario, clave, serverIP);
	}

	Logger logger = Logging.obtenerClientLogger();

	@Override
	public List<Rol> mostrarTodo() throws RequestFailureException {
		List<Rol> roles = new ArrayList<Rol>();

		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/rol/mostrarTodo";
			JSONObject salidaJson = get(url);
			for (int i = 0; i < salidaJson.getJSONArray("roles").length(); i++) {
				JSONObject jsonRol = new JSONObject((String) salidaJson
						.getJSONArray("roles").get(i));
				Rol rol = new Rol(jsonRol);
				roles.add(rol);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return roles;
	}

	@Override
	public Rol agregar(Rol rol) throws RequestFailureException {
		boolean resultado = false;
		Rol rolCreado = null;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/rol/agregar";
		try {
			JSONObject inputJsonRol = rol.getJSON();
			JSONObject salidaJson = post(url, inputJsonRol);
			resultado = salidaJson.getBoolean("exitoso");
			if (resultado == true) {
				JSONObject jsonRol = new JSONObject((String) salidaJson
						.getJSONArray("roles").get(0));
				rolCreado = new Rol(jsonRol);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return rolCreado;
	}

	@Override
	public boolean actualizar(Rol rol) throws RequestFailureException {
		boolean resultado = false;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/rol/actualizar";
		try {
			JSONObject jsonRol = rol.getJSON();
			JSONObject salidaJson = put(url, jsonRol);
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
					+ ":8080/ProyectoUTC/rest/rol/eliminar?idRol=" + id;
			JSONObject salidaJson = delete(url);
			mensaje = salidaJson.getString("mensaje");
			logger.log(Level.INFO, "Mensaje: ", mensaje);
			resultado = salidaJson.getBoolean("exitoso");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception ", e);
		}
		return resultado;
	}

	@Override
	public Rol mostrar(int id) throws RequestFailureException {
		Rol rol = null;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/rol/mostrar/" + id;
			JSONObject salidaJson = get(url);
			JSONObject jsonRol = new JSONObject((String) salidaJson
					.getJSONArray("roles").get(0));
			rol = new Rol(jsonRol);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return rol;
	}
}