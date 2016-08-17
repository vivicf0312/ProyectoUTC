package capaPresentacion.usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import utilidades.RequestFailureException;
import capaAPICliente.RolCliente;
import capaNegocio.Rol;
import capaNegocio.Usuario;

@SuppressWarnings("serial")
public class TablaUsuariosModelo extends AbstractTableModel {

	public static final int colObjeto = -1;
	private static final int colNombre = 0;
	private static final int colApellido1 = 1;
	private static final int colApellido2 = 2;
	private static final int colNombreUsuario = 3;
	private static final int rol = 4;
	private Usuario usuarioActual = null;
	private String[] nombreColumnas = { "NOMBRE", "PRIMER APELLIDO",
			"SEGUNDO APELLIDO", "NOMBRE DE USUARIO", "ROL" };
	private List<Usuario> usuarios;
	private List<Usuario> usuariosFiltrados;
	private String filtro = null;
	private HashMap<Integer, String> roles = new HashMap<Integer, String>();
	private String serverIP;

	public TablaUsuariosModelo() {
		this(new ArrayList<Usuario>(), null, null);
	}

	public TablaUsuariosModelo(List<Usuario> usuarios, Usuario usuario,
			String serverIP) {
		this.usuarios = usuarios;
		this.usuarioActual = usuario;
		this.serverIP = serverIP;
		setFiltro(null);
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
		if (this.filtro != null && !this.filtro.trim().equals("")) {
			this.usuariosFiltrados = new ArrayList<Usuario>();
			String lowerCaseFilter = filtro.toLowerCase();
			for (Usuario usuario : usuarios) {
				String rol = getRol(usuario.getIdRol());
				if (usuario.getNombre().toLowerCase().contains(lowerCaseFilter)
						|| usuario.getNombreUsuario().toLowerCase()
								.contains(lowerCaseFilter)
						|| rol.toLowerCase().contains(lowerCaseFilter)) {
					this.usuariosFiltrados.add(usuario);
				}
			}
		} else {
			this.usuariosFiltrados = new ArrayList<Usuario>(usuarios);
		}
	}

	private String getRol(int id) {
		String rolUsuario = "";
		if (roles.containsKey(id)) {
			rolUsuario = roles.get(id);
		} else {
			RolCliente rolCliente = new RolCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					serverIP);
			Rol rol;
			try {
				rol = rolCliente.mostrar(id);
				rolUsuario = rol.getDescripcion();
				roles.put(id, rolUsuario);
			} catch (RequestFailureException e) {
				rolUsuario = "No Disponible";
			}
		}
		return rolUsuario;
	}

	@Override
	public int getColumnCount() {
		return nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return usuariosFiltrados.size();
	}

	@Override
	public String getColumnName(int col) {
		return nombreColumnas[col];
	}

	@Override
	public Object getValueAt(int fila, int col) {

		Usuario tempUsuario = usuariosFiltrados.get(fila);

		switch (col) {
		case colNombre:
			return tempUsuario.getNombre();
		case colApellido1:
			return tempUsuario.getApellido1();
		case colApellido2:
			return tempUsuario.getApellido2();
		case colNombreUsuario:
			return tempUsuario.getNombreUsuario();
		case rol:
			return getRol(tempUsuario.getIdRol());
		case colObjeto:
			return tempUsuario;
		default:
			return tempUsuario.getApellido1();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
}
