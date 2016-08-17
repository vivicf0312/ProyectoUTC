package capaAPICliente;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaNegocio.Registro;

public class RegistroCliente extends AbstractCliente<Registro> {

	public RegistroCliente(String nombreUsuario, String clave, String serverIP) {
		super(nombreUsuario, clave, serverIP);
	}

	Logger logger = Logging.obtenerClientLogger();

	@Override
	public List<Registro> mostrarTodo() throws RequestFailureException {
		List<Registro> registros = new ArrayList<Registro>();

		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/registro/mostrarTodo";
			JSONObject salidaJson = get(url);
			for (int i = 0; i < salidaJson.getJSONArray("registros").length(); i++) {
				JSONObject jsonRegistro = new JSONObject((String) salidaJson
						.getJSONArray("registros").get(i));
				Registro registro = new Registro(jsonRegistro);
				registros.add(registro);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Error", e);
		}
		return registros;
	}

	@Override
	public Registro agregar(Registro registro) throws RequestFailureException {
		boolean resultado = false;
		Registro registroCreado = null;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/registro/agregar";
		try {
			JSONObject inputJsonRegistro = registro.getJSON();
			JSONObject salidaJson = post(url, inputJsonRegistro);
			resultado = salidaJson.getBoolean("exitoso");
			if (resultado == true) {
				JSONObject jsonRegistro = new JSONObject((String) salidaJson
						.getJSONArray("registros").get(0));
				registroCreado = new Registro(jsonRegistro);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return registroCreado;
	}

	@Override
	public boolean actualizar(Registro registro) throws RequestFailureException {
		// Registro no se actualiza
		return false;
	}

	@Override
	public boolean eliminar(int id) throws RequestFailureException {
		String mensaje = null;
		boolean resultado = true;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/registro/eliminar?idRegistro="
					+ id;
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
	public Registro mostrar(int id) throws RequestFailureException {
		Registro registro = null;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/registro/mostrar/" + id;
			JSONObject salidaJson = get(url);
			JSONObject jsonRegistro = new JSONObject((String) salidaJson
					.getJSONArray("registros").get(0));
			registro = new Registro(jsonRegistro);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return registro;
	}

}
