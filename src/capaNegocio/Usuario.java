package capaNegocio;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Usuario {

	private String nombre, apellido1, apellido2, nombreUsuario, clave;
	private int idUsuario, idRol;

	public Usuario(int idUsuario, String nombre, String apellido1,
			String apellido2, String nombreUsuario, String clave, int idRol) {
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.nombreUsuario = nombreUsuario;
		this.clave = clave;
		this.idRol = idRol;
	}

	public Usuario(String nombre, String apellido1, String apellido2,
			String nombreUsuario, String clave, int idRol) {
		this(-1, nombre, apellido1, apellido2, nombreUsuario, clave, idRol);

	}

	public Usuario(JSONObject jsonObject) throws JSONException {
		this(jsonObject.getInt("idUsuario"), jsonObject.getString("nombre"),
				jsonObject.getString("apellido1"), jsonObject
						.getString("apellido2"), jsonObject
						.getString("nombreUsuario"), jsonObject
						.getString("clave"), jsonObject.getInt("idRol"));
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellido1=" + apellido1
				+ ", apellido2=" + apellido2 + ", nombreUsuario="
				+ nombreUsuario + ", clave=" + clave + ", idUsuario="
				+ idUsuario + ", idRol=" + idRol + "]";
	}

	public JSONObject getJSON() throws JSONException {
		JSONObject respuesta = new JSONObject();
		respuesta.put("idUsuario", this.getIdUsuario());
		respuesta.put("nombre", this.getNombre());
		respuesta.put("apellido1", this.getApellido1());
		respuesta.put("apellido2", this.getApellido2());
		respuesta.put("nombreUsuario", this.getNombreUsuario());
		respuesta.put("clave", this.getClave());
		respuesta.put("idRol", this.getIdRol());
		return respuesta;
	}
}
