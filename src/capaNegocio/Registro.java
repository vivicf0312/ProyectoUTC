package capaNegocio;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Registro {

	private int idRegistro;
	private String descripcion;
	private int idUsuario;
	private String timeStamp;

	public Registro(int idRegistro, String descripcion, int idUsuario,
			String timeStamp) {
		this.idRegistro = idRegistro;
		this.descripcion = descripcion;
		this.idUsuario = idUsuario;
		this.timeStamp = timeStamp;
	}

	public Registro(String descripcion, int idUsuario, String timeStamp) {
		this(-1, descripcion, idUsuario, timeStamp);
	}

	public Registro(JSONObject jsonObject) throws JSONException {
		this(jsonObject.getInt("idRegistro"), jsonObject
				.getString("descripcion"), jsonObject.getInt("idUsuario"),
				jsonObject.getString("timeStamp"));
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getIdRegistro() {
		return idRegistro;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String toString() {
		return "Registro [idRegistro =" + idRegistro + ", descripcion="
				+ descripcion + ", idUsuario=" + idUsuario + ", timeStamp="
				+ timeStamp + "]";
	}

	public JSONObject getJSON() throws JSONException {
		JSONObject respuesta = new JSONObject();
		respuesta.put("idRegistro", this.getIdRegistro());
		respuesta.put("descripcion", this.getDescripcion());
		respuesta.put("idUsuario", this.getIdUsuario());
		respuesta.put("timeStamp", this.getTimeStamp());
		return respuesta;
	}
}
