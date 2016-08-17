package capaNegocio;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Articulo {

	private int idArticulo;
	private int tomo;
	private int folio;
	private int asiento;
	private String idEtiqueta;
	private String descripcion;
	private String marca;
	private String modelo;
	private String serie;
	private String condicion;
	private int idUbicacion;
	private String adquisicion;
	private String observaciones;

	public Articulo(int idArticulo, int tomo, int folio, int asiento,
			String idEtiqueta, String descripcion, String marca, String modelo,
			String serie, String condicion, int idUbicacion,
			String adquisicion, String observaciones) {

		String idArticuloStr = "" + idArticulo;
		if (idArticulo > 0){
			int zerosNeeded = 6 - idArticuloStr.length();
			while(zerosNeeded > 0){
				idArticuloStr = "0" + idArticuloStr;
				zerosNeeded--;
			}
		}
		
		idEtiqueta = "68-3781" + " " + idArticuloStr;
		this.idArticulo = idArticulo;
		this.tomo = tomo;
		this.folio = folio;
		this.asiento = asiento;
		this.idEtiqueta = idEtiqueta;
		this.descripcion = descripcion;
		this.marca = marca;
		this.modelo = modelo;
		this.serie = serie;
		this.condicion = condicion;
		this.idUbicacion = idUbicacion;
		this.adquisicion = adquisicion;
		this.observaciones = observaciones;
	}

	public Articulo(int tomo, int folio, int asiento, String descripcion,
			String marca, String modelo, String serie, String condicion,
			int idUbicacion, String adquisicion, String observaciones) {
		this(-1, tomo, folio, asiento, null, descripcion, marca, modelo, serie,
				condicion, idUbicacion, adquisicion, observaciones);
	}

	public Articulo(JSONObject jsonObject) throws JSONException {
		this(jsonObject.getInt("idArticulo"), jsonObject.getInt("tomo"),
				jsonObject.getInt("folio"), jsonObject.getInt("asiento"),
				jsonObject.getString("idEtiqueta"), jsonObject
						.getString("descripcion"), jsonObject
						.getString("marca"), jsonObject.getString("modelo"),
				jsonObject.getString("serie"), jsonObject
						.getString("condicion"), jsonObject
						.getInt("idUbicacion"), jsonObject
						.getString("adquisicion"), jsonObject
						.getString("observaciones"));
	}

	public int getTomo() {
		return tomo;
	}

	public void setTomo(int tomo) {
		this.tomo = tomo;
	}

	public int getFolio() {
		return folio;
	}

	public void setFolio(int folio) {
		this.folio = folio;
	}

	public int getAsiento() {
		return asiento;
	}

	public void setAsiento(int asiento) {
		this.asiento = asiento;
	}

	public String getIdEtiqueta() {
		return idEtiqueta;
	}

	public void setIdEtiqueta(String idEtiqueta) {
		this.idEtiqueta = idEtiqueta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getCondicion() {
		return condicion;
	}

	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}

	public String getAdquisicion() {
		return adquisicion;
	}

	public void setAdquisicion(String adquisicion) {
		this.adquisicion = adquisicion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public int getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(int idArticulo) {
		this.idArticulo = idArticulo;
	}

	public int getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(int idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	@Override
	public String toString() {
		return "Articulo [idArticulo=" + idArticulo + ", tomo=" + tomo
				+ ", folio=" + folio + ", asiento=" + asiento + ", idEtiqueta="
				+ idEtiqueta + ", descripcion=" + descripcion + ", marca="
				+ marca + ", modelo=" + modelo + ", serie=" + serie
				+ ", condicion=" + condicion + ", idUbicacion=" + idUbicacion
				+ ", adquisicion=" + adquisicion + ", observaciones="
				+ observaciones + "]";
	}

	public JSONObject getJSON() throws JSONException {
		JSONObject respuesta = new JSONObject();
		respuesta.put("idArticulo", this.getIdArticulo());
		respuesta.put("tomo", this.getTomo());
		respuesta.put("folio", this.getFolio());
		respuesta.put("asiento", this.getAsiento());
		respuesta.put("idEtiqueta", this.getIdEtiqueta());
		respuesta.put("descripcion", this.getDescripcion());
		respuesta.put("marca", this.getMarca());
		respuesta.put("modelo", this.getModelo());
		respuesta.put("serie", this.getSerie());
		respuesta.put("condicion", this.getCondicion());
		respuesta.put("idUbicacion", this.getIdUbicacion());
		respuesta.put("adquisicion", this.getAdquisicion());
		respuesta.put("observaciones", this.getObservaciones());
		return respuesta;
	}
}
