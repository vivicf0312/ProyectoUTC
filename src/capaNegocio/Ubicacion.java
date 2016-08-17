package capaNegocio;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Ubicacion {

	private int idUbicacion;
	private String descripcion;

	public Ubicacion(int idUbicacion, String descripcion) {
		this.idUbicacion = idUbicacion;
		this.descripcion = descripcion;

	}

	public Ubicacion(String descripcion) {
		this(-1, descripcion);
	}

	public Ubicacion(JSONObject jsonObject) throws JSONException {
		this(jsonObject.getInt("idUbicacion"), jsonObject
				.getString("descripcion"));
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setIdUbicacion(int idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public int getIdUbicacion() {
		return idUbicacion;
	}

	@Override
	public String toString() {
		return "Ubicacion [idUbicacion=" + idUbicacion + ", descripcion="
				+ descripcion + "]";
	}

	public JSONObject getJSON() throws JSONException {
		JSONObject respuesta = new JSONObject();
		respuesta.put("idUbicacion", this.getIdUbicacion());
		respuesta.put("descripcion", this.getDescripcion());
		return respuesta;
	}
}
