package capaAPICliente;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaNegocio.Ubicacion;

public class UbicacionCliente extends AbstractCliente<Ubicacion> {

	public UbicacionCliente(String nombreUsuario, String clave, String serverIP) {
		super(nombreUsuario, clave, serverIP);
	}

	Logger logger = Logging.obtenerClientLogger();

	@Override
	public List<Ubicacion> mostrarTodo() throws RequestFailureException {
		List<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();

		try {
			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/ubicacion/mostrarTodo";
			JSONObject salidaJson = get(url);
			for (int i = 0; i < salidaJson.getJSONArray("ubicaciones").length(); i++) {
				JSONObject jsonUbicacion = new JSONObject((String) salidaJson
						.getJSONArray("ubicaciones").get(i));
				Ubicacion ubicacion = new Ubicacion(jsonUbicacion);
				ubicaciones.add(ubicacion);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return ubicaciones;
	}

	@Override
	public Ubicacion agregar(Ubicacion ubicacion)
			throws RequestFailureException {
		boolean resultado = false;
		Ubicacion ubicacionCreada = null;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/ubicacion/agregar";
		try {
			JSONObject inputJsonUbicacion = ubicacion.getJSON();
			JSONObject salidaJson = post(url, inputJsonUbicacion);
			resultado = salidaJson.getBoolean("exitoso");
			if (resultado == true) {
				JSONObject jsonUbicacion = new JSONObject((String) salidaJson
						.getJSONArray("ubicaciones").get(0));
				ubicacionCreada = new Ubicacion(jsonUbicacion);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return ubicacionCreada;
	}

	@Override
	public boolean actualizar(Ubicacion ubicacion)
			throws RequestFailureException {
		boolean resultado = false;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/ubicacion/actualizar";
		try {
			JSONObject jsonUbicacion = ubicacion.getJSON();
			JSONObject salidaJson = put(url, jsonUbicacion);
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
			logger.log(Level.INFO, "idUbicacion=" + id);
			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/ubicacion/eliminar?idUbicacion="
					+ id;
			JSONObject salidaJson = delete(url);
			mensaje = salidaJson.getString("mensaje");
			logger.log(Level.INFO, "Mensaje", mensaje);
			resultado = salidaJson.getBoolean("exitoso");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return resultado;
	}

	@Override
	public Ubicacion mostrar(int id) throws RequestFailureException {
		Ubicacion ubicacion = null;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/ubicacion/mostrar/" + id;
			JSONObject salidaJson = get(url);
			JSONObject jsonUbicacion = new JSONObject((String) salidaJson
					.getJSONArray("ubicaciones").get(0));
			ubicacion = new Ubicacion(jsonUbicacion);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return ubicacion;
	}

}
