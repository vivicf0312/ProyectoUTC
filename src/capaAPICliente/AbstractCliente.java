package capaAPICliente;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import utilidades.Logging;
import utilidades.RequestFailureException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public abstract class AbstractCliente<T> {

	protected String serverIP;
	protected String nombreUsuario = "rfon";
	protected String clave = "12345";
	Logger logger = Logging.obtenerClientLogger();

	public AbstractCliente(String nombreUsuario, String clave, String serverIP) {
		this.serverIP = serverIP;
		this.nombreUsuario = nombreUsuario;
		this.clave = clave;
	}

	private JSONObject validateResponse(ClientResponse response)
			throws RequestFailureException {
		int httpCode = response.getStatus();
		String salida = response.getEntity(String.class);
		logger.log(Level.INFO, "Salida: ", salida);
		JSONObject salidaJson;
		try {
			salidaJson = new JSONObject(salida);
			if (httpCode != 200 && httpCode != 201 && httpCode != 202) {
				throw new RequestFailureException(httpCode,
						salidaJson.getString("mensaje"));
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Error de servidor: ", e);
			throw new RequestFailureException(httpCode, "Error de Servidor");
		}
		return salidaJson;
	}

	protected JSONObject post(String url, JSONObject data)
			throws RequestFailureException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.header("nombreUsuario", nombreUsuario).header("clave", clave)
				.post(ClientResponse.class, data.toString());
		return validateResponse(response);
	}

	protected JSONObject put(String url, JSONObject data)
			throws RequestFailureException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.header("nombreUsuario", nombreUsuario).header("clave", clave)
				.put(ClientResponse.class, data.toString());
		return validateResponse(response);
	}

	protected JSONObject get(String url) throws RequestFailureException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.header("nombreUsuario", nombreUsuario).header("clave", clave)
				.get(ClientResponse.class);
		return validateResponse(response);
	}

	protected JSONObject delete(String url) throws RequestFailureException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.header("nombreUsuario", nombreUsuario).header("clave", clave)
				.delete(ClientResponse.class);
		return validateResponse(response);
	}

	public abstract T agregar(T t) throws RequestFailureException;

	public abstract boolean actualizar(T t) throws RequestFailureException;

	public abstract boolean eliminar(int id) throws RequestFailureException;

	public abstract T mostrar(int id) throws RequestFailureException;

	public abstract List<T> mostrarTodo() throws RequestFailureException;
}
