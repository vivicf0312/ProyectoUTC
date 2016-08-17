package capaPresentacion.ubicaciones;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import capaNegocio.Ubicacion;

@SuppressWarnings("serial")
public class TablaUbicacionesModelo extends AbstractTableModel {

	public static final int colObjeto = -1;
	private static final int colDescripcion = 0;

	private String[] nombreColumnas = { "DESCRIPCION" };
	private List<Ubicacion> ubicaciones;
	private List<Ubicacion> ubicacionesFiltradas;
	private String filtro = null;

	public TablaUbicacionesModelo() {
		this(new ArrayList<Ubicacion>());
	}

	public TablaUbicacionesModelo(List<Ubicacion> ubicaciones) {
		this.ubicaciones = ubicaciones;
		setFiltro(null);
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
		if (this.filtro != null && !this.filtro.trim().equals("")) {
			this.ubicacionesFiltradas = new ArrayList<Ubicacion>();
			String lowerCaseFilter = filtro.toLowerCase();
			for (Ubicacion ubicacion : ubicaciones) {
				if (ubicacion.getDescripcion().toLowerCase()
						.contains(lowerCaseFilter)) {
					this.ubicacionesFiltradas.add(ubicacion);
				}
			}
		} else {
			this.ubicacionesFiltradas = new ArrayList<Ubicacion>(ubicaciones);
		}
	}

	@Override
	public int getColumnCount() {
		return nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return ubicacionesFiltradas.size();
	}

	@Override
	public String getColumnName(int col) {
		return nombreColumnas[col];
	}

	@Override
	public Object getValueAt(int fila, int col) {

		Ubicacion tempUbicacion = ubicacionesFiltradas.get(fila);

		switch (col) {
		case colDescripcion:
			return tempUbicacion.getDescripcion();
		case colObjeto:
			return tempUbicacion;
		default:
			return tempUbicacion.getDescripcion();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public List<Ubicacion> getUbicaciones() {
		return ubicaciones;
	}
}
