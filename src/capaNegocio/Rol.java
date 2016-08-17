package capaNegocio;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import utilidades.Enumeracion.Operaciones;

public class Rol {

	private int idRol;
	private String descripcion;
	private Set<Operaciones> operaciones;

	public Rol(int idRol, String descripcion, Set<Operaciones> operaciones) {
		this.idRol = idRol;
		this.descripcion = descripcion;
		this.operaciones = operaciones;
	}

	public Rol(String descripcion, Set<Operaciones> operaciones) {
		this(-1, descripcion, operaciones);
	}

	public Rol(JSONObject jsonObject) throws JSONException {
		this(jsonObject.getInt("idRol"), jsonObject.getString("descripcion"),
				null);
		HashSet<Operaciones> operaciones = new HashSet<Operaciones>();
		JSONArray jsonOperaciones = jsonObject.getJSONArray("operaciones");
		for (int i = 0; i < jsonOperaciones.length(); i++) {
			operaciones.add(Operaciones.valueOf(jsonOperaciones.getString(i)));
		}
		this.operaciones = operaciones;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public Set<Operaciones> getOperaciones() {
		return operaciones;
	}

	public void setOperaciones(Set<Operaciones> operaciones) {
		this.operaciones = operaciones;
	}

	public boolean agregarOperacion(Operaciones operacion) {
		return operaciones.add(operacion);
	}

	public boolean eliminarOperacion(Operaciones operacion) {
		return operaciones.remove(operacion);
	}

	@Override
	public String toString() {
		return "Rol [idRol=" + idRol + ", descripcion=" + descripcion
				+ ", operaciones=" + operaciones + "]";
	}

	public JSONObject getJSON() throws JSONException {
		JSONObject respuesta = new JSONObject();
		respuesta.put("idRol", this.getIdRol());
		respuesta.put("descripcion", this.getDescripcion());
		JSONArray jsonOperaciones = new JSONArray();
		for (Operaciones operacion : this.getOperaciones()) {
			jsonOperaciones.put(operacion.name());
		}
		respuesta.put("operaciones", jsonOperaciones);
		return respuesta;
	}
}
