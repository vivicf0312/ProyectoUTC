package capaPresentacion.registros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import utilidades.RequestFailureException;
import capaAPICliente.UsuarioCliente;
import capaNegocio.Registro;
import capaNegocio.Usuario;

@SuppressWarnings("serial")
public class TablaRegistrosModelo extends AbstractTableModel {

	public static final int colObjeto = -1;
	private static final int colDescripcion = 0;
	private static final int colUsuario = 1;
	private static final int colTime = 2;

	private String[] nombreColumnas = { "DESCRIPCION", "USUARIO", "TIEMPO" };
	private List<Registro> registros;
	private List<Registro> registrosFiltrados;
	private String filtro = null;
	private Usuario usuarioActual = null;
	private HashMap<Integer, String> usuarios = new HashMap<Integer, String>();
	private String serverIP;

	public TablaRegistrosModelo() {
		this(new ArrayList<Registro>(), null, null);
	}

	public TablaRegistrosModelo(List<Registro> registros, Usuario usuario,
			String serverIP) {
		this.registros = registros;
		this.usuarioActual = usuario;
		this.serverIP = serverIP;
		setFiltro(null);
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;

		if (this.filtro != null && !this.filtro.trim().equals("")) {
			this.registrosFiltrados = new ArrayList<Registro>();
			String lowerCaseFilter = filtro.toLowerCase();
			for (Registro registro : registros) {
				String usuario = getUsuario(registro.getIdUsuario());
				if (registro.getDescripcion().toLowerCase()
						.contains(lowerCaseFilter)
						|| usuario.toLowerCase().contains(lowerCaseFilter)) {
					this.registrosFiltrados.add(registro);
				}
			}
		} else {
			this.registrosFiltrados = new ArrayList<Registro>(registros);
		}
	}

	private String getUsuario(int id) {
		String usuarioRegistro = "";
		if (usuarios.containsKey(id)) {
			usuarioRegistro = usuarios.get(id);
		} else {
			UsuarioCliente usuarioCliente = new UsuarioCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					serverIP);
			Usuario usuario;
			try {
				usuario = usuarioCliente.mostrar(id);
				if (usuario != null) {
					usuarioRegistro = usuario.getNombre() + " "
							+ usuario.getApellido1() + " "
							+ usuario.getApellido2();
					usuarios.put(id, usuarioRegistro);
				} else {
					usuarioRegistro = "Usuario no existe";
				}
			} catch (RequestFailureException e) {
				usuarioRegistro = "No Disponible";
			}
		}
		return usuarioRegistro;
	}

	@Override
	public int getColumnCount() {
		return nombreColumnas.length;
	}

	@Override
	public int getRowCount() {
		return registrosFiltrados.size();
	}

	@Override
	public String getColumnName(int col) {
		return nombreColumnas[col];
	}

	@Override
	public Object getValueAt(int fila, int col) {

		Registro tempRegistro = registrosFiltrados.get(fila);

		switch (col) {
		case colDescripcion:
			return tempRegistro.getDescripcion();
		case colUsuario:
			return getUsuario(tempRegistro.getIdUsuario());
		case colTime:
			return tempRegistro.getTimeStamp();
		case colObjeto:
			return tempRegistro;
		default:
			return tempRegistro.getIdRegistro();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
}
