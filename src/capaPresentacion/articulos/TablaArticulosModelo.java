package capaPresentacion.articulos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import utilidades.RequestFailureException;
import capaAPICliente.UbicacionCliente;
import capaNegocio.Articulo;
import capaNegocio.Ubicacion;
import capaNegocio.Usuario;

@SuppressWarnings("serial")
public class TablaArticulosModelo extends AbstractTableModel {

	public static final int colObjeto = -1;
	private static final int colTomo = 0;
	private static final int colFolio = 1;
	private static final int colAsiento = 2;
	private static final int colIdEtiqueta = 3;
	private static final int colDescripcion = 4;
	private static final int colMarca = 5;
	private static final int colModelo = 6;
	private static final int colSerie = 7;
	private static final int colEstado = 8;
	private static final int colIdUbicacion = 9;
	private static final int colAdquisicion = 10;
	private static final int colObservacion = 11;
	private Usuario usuarioActual = null;

	private String[] nombreColumnas = { "TOMO", "FOLIO", "ASIENTO",
			"# DE IDENTIFICACION", "DESCRIPCION", "MARCA", "MODELO", "SERIE",
			"ESTADO", "UBICACION", "MODO DE ADQUISICION", "OBSERVACION" };
	private List<Articulo> articulos;
	private List<Articulo> articulosFiltrados;
	private String filtro = null;
	private String serverIP;
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();

	public TablaArticulosModelo() {
		this(new ArrayList<Articulo>(), null, null);
	}

	public TablaArticulosModelo(List<Articulo> articulos, Usuario usuario,
			String serverIP) {
		this.articulos = articulos;
		this.usuarioActual = usuario;
		this.serverIP = serverIP;
		setFiltro(null);
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
		if (this.filtro != null && !this.filtro.trim().equals("")) {
			this.articulosFiltrados = new ArrayList<Articulo>();
			String lowerCaseFilter = filtro.toLowerCase();
			for (Articulo articulo : articulos) {// for each articulo in the
													// list of articulos do...
				String location = getLocation(articulo.getIdUbicacion());
				if (articulo.getDescripcion().toLowerCase()
						.contains(lowerCaseFilter)
						|| articulo.getIdEtiqueta().toLowerCase()
								.contains(lowerCaseFilter)
						|| articulo.getCondicion().toLowerCase()
								.contains(lowerCaseFilter)
						|| location.toLowerCase()
								.contains(filtro.toLowerCase())) {
					this.articulosFiltrados.add(articulo);
				}
			}
		} else {
			this.articulosFiltrados = new ArrayList<Articulo>(articulos);
		}
	}

	private String getLocation(int id) {
		String location = "";
		if (locations.containsKey(id)) {
			location = locations.get(id);
		} else {
			UbicacionCliente ubicacionCliente = new UbicacionCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					serverIP);
			Ubicacion ubicacion;
			try {
				ubicacion = ubicacionCliente.mostrar(id);
				location = ubicacion.getDescripcion();
				locations.put(id, location);
			} catch (RequestFailureException e) {
				location = "No Disponible";
			}
		}
		return location;
	}

	@Override
	public int getColumnCount() {
		return nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return articulosFiltrados.size();
	}

	@Override
	public String getColumnName(int col) {
		return nombreColumnas[col];
	}

	@Override
	public Object getValueAt(int fila, int col) {

		Articulo tempArticulo = articulosFiltrados.get(fila);

		switch (col) {
		case colTomo:
			return tempArticulo.getTomo();
		case colFolio:
			return tempArticulo.getFolio();
		case colAsiento:
			return tempArticulo.getAsiento();
		case colIdEtiqueta:
			return tempArticulo.getIdEtiqueta();
		case colDescripcion:
			return tempArticulo.getDescripcion();
		case colMarca:
			return tempArticulo.getMarca();
		case colModelo:
			return tempArticulo.getModelo();
		case colSerie:
			return tempArticulo.getSerie();
		case colEstado:
			return tempArticulo.getCondicion();
		case colIdUbicacion:
			return getLocation(tempArticulo.getIdUbicacion());
		case colAdquisicion:
			return tempArticulo.getAdquisicion();
		case colObservacion:
			return tempArticulo.getObservaciones();
		case colObjeto:
			return tempArticulo;
		default:
			return tempArticulo.getTomo();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}
}
