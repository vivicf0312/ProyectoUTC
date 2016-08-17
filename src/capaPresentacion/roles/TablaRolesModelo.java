package capaPresentacion.roles;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import capaNegocio.Rol;

@SuppressWarnings("serial")
public class TablaRolesModelo extends AbstractTableModel {

	public static final int colObjeto = -1;
	private static final int colDescripcion = 0;
	private static final int colOperaciones = 1;

	private String[] nombreColumnas = { "PUESTO", "OPERACIONES PERMITIDAS" };
	private List<Rol> roles;
	private List<Rol> rolesFiltrados;
	private String filtro = null;

	public TablaRolesModelo() {
		this(new ArrayList<Rol>());
	}

	public TablaRolesModelo(List<Rol> roles) {
		this.roles = roles;
		setFiltro(null);
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
		if (this.filtro != null && !this.filtro.trim().equals("")) {
			this.rolesFiltrados = new ArrayList<Rol>();
			String lowerCaseFilter = filtro.toLowerCase();
			for (Rol rol : roles) {
				if (rol.getDescripcion().toLowerCase()
						.contains(lowerCaseFilter)
						|| rol.getOperaciones().toString().toLowerCase()
								.contains(lowerCaseFilter)) {
					this.rolesFiltrados.add(rol);
				}
			}
		} else {
			this.rolesFiltrados = new ArrayList<Rol>(roles);
		}
	}

	@Override
	public int getColumnCount() {
		return nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return rolesFiltrados.size();
	}

	@Override
	public String getColumnName(int col) {
		return nombreColumnas[col];
	}

	@Override
	public Object getValueAt(int fila, int col) {

		Rol tempRol = rolesFiltrados.get(fila);

		switch (col) {
		case colDescripcion:
			return tempRol.getDescripcion();
		case colOperaciones:
			return tempRol.getOperaciones();
		case colObjeto:
			return tempRol;
		default:
			return tempRol.getDescripcion();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public List<Rol> getRoles() {
		return roles;
	}
}
