package capaAPICliente;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import utilidades.Logging;
import utilidades.RequestFailureException;
import capaNegocio.Articulo;

public class ArticuloCliente extends AbstractCliente<Articulo> {

	public ArticuloCliente(String nombreUsuario, String clave, String serverIP) {
		super(nombreUsuario, clave, serverIP);
	}

	Logger logger = Logging.obtenerClientLogger();

	@Override
	public List<Articulo> mostrarTodo() throws RequestFailureException {
		List<Articulo> articulos = new ArrayList<Articulo>();
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/articulo/mostrarTodo";
			JSONObject salidaJson = get(url);
			for (int i = 0; i < salidaJson.getJSONArray("articulos").length(); i++) {
				JSONObject jsonArticulo = new JSONObject((String) salidaJson
						.getJSONArray("articulos").get(i));
				Articulo articulo = new Articulo(jsonArticulo);
				articulos.add(articulo);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return articulos;
	}

	@Override
	public Articulo agregar(Articulo articulo) throws RequestFailureException {
		boolean resultado = false;
		Articulo articuloCreado = null;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/articulo/agregar";
		try {
			JSONObject inputJsonArticulo = articulo.getJSON();
			JSONObject salidaJson = post(url, inputJsonArticulo);
			resultado = salidaJson.getBoolean("exitoso");
			if (resultado == true) {
				JSONObject jsonArticulo = new JSONObject((String) salidaJson
						.getJSONArray("articulos").get(0));
				articuloCreado = new Articulo(jsonArticulo);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return articuloCreado;
	}

	@Override
	public boolean actualizar(Articulo articulo) throws RequestFailureException {
		boolean resultado = false;
		String url = "http://" + serverIP
				+ ":8080/ProyectoUTC/rest/articulo/actualizar";
		try {
			JSONObject jsonArticulo = articulo.getJSON();
			JSONObject salidaJson = put(url, jsonArticulo);
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
					+ ":8080/ProyectoUTC/rest/articulo/eliminar?idArticulo="
					+ id;
			JSONObject salidaJson = delete(url);
			mensaje = salidaJson.getString("mensaje");
			logger.log(Level.INFO, "mensaje: ", mensaje);
			resultado = salidaJson.getBoolean("exitoso");
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return resultado;
	}

	@Override
	public Articulo mostrar(int id) throws RequestFailureException {
		Articulo articulo = null;
		try {

			String url = "http://" + serverIP
					+ ":8080/ProyectoUTC/rest/articulo/mostrar/" + id;
			JSONObject salidaJson = get(url);
			JSONObject jsonArticulo = new JSONObject((String) salidaJson
					.getJSONArray("articulos").get(0));
			articulo = new Articulo(jsonArticulo);
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "JSON Exception", e);
		}
		return articulo;
	}

}
