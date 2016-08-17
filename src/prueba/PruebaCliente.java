package prueba;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PruebaCliente {

	JSONObject jsonUbicacion = null;
	JSONObject jsonRol = null;
	JSONObject jsonUsuario = null;
	JSONObject jsonArticulo = null;
	JSONObject jsonRegistro = null;
	String nombreUsuario = "admin";
	String clave = "adminpw";

	public static void main(String[] args) {

		PruebaCliente pruebaCliente = new PruebaCliente();
		// // add and selectAll
		pruebaCliente.agregarRol();
		pruebaCliente.mostrarTodoRoles();

		pruebaCliente.agregarUsuario();
		pruebaCliente.mostrarTodoUsuarios();

		pruebaCliente.agregarUbicacion();
		pruebaCliente.mostrarTodoUbicaciones();

		pruebaCliente.agregarArticulo();
		pruebaCliente.mostrarTodoArticulos();

		pruebaCliente.agregarRegistro();
		pruebaCliente.mostrarTodoRegistros();

		// Buscar Usuario
		pruebaCliente.buscarUsuarios();

		// update
		pruebaCliente.actualizarRol();
		pruebaCliente.actualizarUsuario();
		pruebaCliente.actualizarUbicacion();
		pruebaCliente.actualizarArticulo();
		// select
		pruebaCliente.mostrarRol();
		pruebaCliente.mostrarUsuario();
		pruebaCliente.mostrarUbicacion();
		pruebaCliente.mostrarArticulo();
		pruebaCliente.mostrarRegistro();
		// delete
		pruebaCliente.eliminarUsuario();
		pruebaCliente.eliminarRol();
		pruebaCliente.eliminarArticulo();
		pruebaCliente.eliminarUbicacion();
		pruebaCliente.eliminarRegistro();

		pruebaCliente.todasTablasVacias();

	}

	private void agregarRol() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/rol/agregar";
			String data = obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearRol.json");
			jsonRol = new JSONObject(data);
			post(url, jsonRol);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void agregarUsuario() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/usuario/agregar";
			String data = obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearUsuario.json");
			jsonUsuario = new JSONObject(data);
			jsonUsuario.put("idRol", jsonRol.getInt("idRol"));
			post(url, jsonUsuario);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void agregarUbicacion() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/ubicacion/agregar";
			String data = obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearUbicacion.json");
			jsonUbicacion = new JSONObject(data);
			post(url, jsonUbicacion);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void agregarArticulo() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/articulo/agregar";
			String data = obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearArticulo.json");
			jsonArticulo = new JSONObject(data);
			jsonArticulo
					.put("idUbicacion", jsonUbicacion.getInt("idUbicacion"));
			post(url, jsonArticulo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void agregarRegistro() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/registro/agregar";
			String data = obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearRegistro.json");
			jsonRegistro = new JSONObject(data);
			jsonRegistro.put("idUsuario", jsonUsuario.getInt("idUsuario"));
			post(url, jsonRegistro);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void buscarUsuarios() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/usuario/autenticar";
			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			jsonUsuario = new JSONObject((String) salidaJson.getJSONArray(
					"usuarios").get(0));
			// System.out.println(jsonUbicacion.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarTodoRoles() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/rol/mostrarTodo";
			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			jsonRol = new JSONObject((String) salidaJson.getJSONArray("roles")
					.get(0));
			// System.out.println(jsonUbicacion.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarTodoUsuarios() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/usuario/mostrarTodo";
			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			jsonUsuario = new JSONObject((String) salidaJson.getJSONArray(
					"usuarios").get(0));
			// System.out.println(jsonUbicacion.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarTodoUbicaciones() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/ubicacion/mostrarTodo";
			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			jsonUbicacion = new JSONObject((String) salidaJson.getJSONArray(
					"ubicaciones").get(0));
			// System.out.println(jsonUbicacion.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarTodoArticulos() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/articulo/mostrarTodo";
			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			jsonArticulo = new JSONObject((String) salidaJson.getJSONArray(
					"articulos").get(0));
			// System.out.println(jsonUbicacion.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarTodoRegistros() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/registro/mostrarTodo";
			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			jsonRegistro = new JSONObject((String) salidaJson.getJSONArray(
					"registros").get(0));
			// System.out.println(jsonUbicacion.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarRol() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/rol/actualizar";
			// String data =
			// obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearUbicacion.json");
			jsonRol.put("descripcion", "Profesor de Ingles");
			put(url, jsonRol);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarUsuario() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/usuario/actualizar";
			// String data =
			// obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearUbicacion.json");
			jsonUsuario.put("clave", "yellow");
			put(url, jsonUsuario);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarUbicacion() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/ubicacion/actualizar";
			// String data =
			// obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearUbicacion.json");
			jsonUbicacion.put("descripcion", "clase 30");
			put(url, jsonUbicacion);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarArticulo() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/articulo/actualizar";
			// String data =
			// obtenerData("C:/eclipse/workspace/ProyectoUTC/src/prueba/crearUbicacion.json");
			jsonArticulo.put("observaciones", "se traslado a oficina");
			put(url, jsonArticulo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarRol() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/rol/mostrar/"
					+ jsonRol.getInt("idRol");
			get(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarUsuario() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/usuario/mostrar/"
					+ jsonUsuario.getInt("idUsuario");
			get(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarUbicacion() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/ubicacion/mostrar/"
					+ jsonUbicacion.getInt("idUbicacion");
			get(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarArticulo() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/articulo/mostrar/"
					+ jsonArticulo.getInt("idArticulo");
			get(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void mostrarRegistro() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/registro/mostrar/"
					+ jsonRegistro.getInt("idRegistro");
			get(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eliminarUsuario() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/usuario/eliminar?idUsuario="
					+ jsonUsuario.getInt("idUsuario");
			delete(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eliminarRol() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/rol/eliminar?idRol="
					+ jsonRol.getInt("idRol");
			delete(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eliminarArticulo() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/articulo/eliminar?idArticulo="
					+ jsonArticulo.getInt("idArticulo");
			delete(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eliminarUbicacion() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/ubicacion/eliminar?idUbicacion="
					+ jsonUbicacion.getInt("idUbicacion");
			delete(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void eliminarRegistro() {
		try {

			String url = "http://localhost:8080/ProyectoUTC/rest/registro/eliminar?idRegistro="
					+ jsonRegistro.getInt("idRegistro");
			delete(url);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void post(String url, JSONObject data) {
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource
					.type(MediaType.APPLICATION_JSON)
					.header("nombreUsuario", nombreUsuario)
					.header("clave", clave)
					.post(ClientResponse.class, data.toString());

			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void put(String url, JSONObject data) {
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource
					.type(MediaType.APPLICATION_JSON)
					.header("nombreUsuario", nombreUsuario)
					.header("clave", clave)
					.put(ClientResponse.class, data.toString());

			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String get(String url) {
		String salida = "";
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource
					.type(MediaType.APPLICATION_JSON)
					.header("nombreUsuario", nombreUsuario)
					.header("clave", clave).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			salida = response.getEntity(String.class);
			System.out.println(salida);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salida;
	}

	private String delete(String url) {
		String salida = "";
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource
					.type(MediaType.APPLICATION_JSON)
					.header("nombreUsuario", nombreUsuario)
					.header("clave", clave).delete(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			salida = response.getEntity(String.class);
			System.out.println(salida);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salida;
	}

	private String obtenerData(String fileName) {
		String data = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			while ((str = in.readLine()) != null)
				data += str;
			in.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private void todasTablasVacias() {
		tablaVacia("http://localhost:8080/ProyectoUTC/rest/rol/mostrarTodo",
				"roles");
		tablaVacia(
				"http://localhost:8080/ProyectoUTC/rest/usuario/mostrarTodo",
				"usuarios");
		tablaVacia(
				"http://localhost:8080/ProyectoUTC/rest/ubicacion/mostrarTodo",
				"ubicaciones");
		tablaVacia(
				"http://localhost:8080/ProyectoUTC/rest/articulo/mostrarTodo",
				"articulos");
		tablaVacia(
				"http://localhost:8080/ProyectoUTC/rest/registro/mostrarTodo",
				"registros");
	}

	private void tablaVacia(String url, String arrayKey) {
		try {

			String salida = get(url);
			JSONObject salidaJson = new JSONObject(salida);
			assert (salidaJson.getJSONArray(arrayKey).length() == 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
