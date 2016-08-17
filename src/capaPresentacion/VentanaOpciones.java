package capaPresentacion;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utilidades.ImportarDatosExcel;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaAPICliente.ArticuloCliente;
import capaAPICliente.RegistroCliente;
import capaAPICliente.RolCliente;
import capaAPICliente.UbicacionCliente;
import capaAPICliente.UsuarioCliente;
import capaNegocio.Articulo;
import capaNegocio.Registro;
import capaNegocio.Rol;
import capaNegocio.Ubicacion;
import capaNegocio.Usuario;
import capaPresentacion.articulos.TablaArticulosModelo;
import capaPresentacion.articulos.VentanaArticulos;
import capaPresentacion.registros.TablaRegistrosModelo;
import capaPresentacion.roles.TablaRolesModelo;
import capaPresentacion.roles.VentanaRol;
import capaPresentacion.ubicaciones.TablaUbicacionesModelo;
import capaPresentacion.ubicaciones.VentanaUbicacion;
import capaPresentacion.usuarios.TablaUsuariosModelo;
import capaPresentacion.usuarios.VentanaUsuario;

public class VentanaOpciones extends JFrame {

	Logger logger = Logging.obtenerClientLogger();

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField buscarArticuloTxt;
	private JTextField buscarUsuarioTxt;
	private JTextField buscarRolTxt;
	private JTextField buscarUbicacionTxt;
	private JTextField buscarRegistroTxt;

	private int idArticulo;
	private int idUsuario;
	private int idRol;
	private int idUbicacion;
	private int idRegistro;
	private JPanel topPanel;
	private JTable tablaUsuarios;

	private JPanel panelArticulo;
	private JPanel btnPanelArticulo;
	private JPanel panelBuscarArticulo;
	private JScrollPane articuloScrollPanel;
	private JTable tablaArticulos;

	private JButton btnBuscarArticulo;
	private JButton btnBuscarUsuario;
	private JButton btnBuscarRol;
	private JButton btnBuscarUbicacion;
	private JButton btnBuscarRegistro;
	private JButton btnAgregarArticulo;
	private JButton btnActualizarArticulo;
	private JButton btnEliminarArticulo;

	private JPanel panelBuscarUsuario;
	private JLabel lblLoggedIn;
	private JLabel usuarioLogueadoLbl;
	private JTabbedPane tabbedPane;

	private JPanel panelUsuario;
	private JPanel btnPanelUsuario;
	private JScrollPane userScrollPane;
	private JButton btnAgregarUsuario;
	private JButton btnActualizarUsuario;
	private JButton btnEliminarUsuario;

	private JPanel panelBuscarUbicacion;
	private JPanel panelUbicaciones;
	private JPanel btnPanelUbicacion;
	private JScrollPane ubicacionesScrollPane;
	private JTable tablaUbicaciones;
	private JButton btnAgregarUbicacion;
	private JButton btnActualizarUbicacion;
	private JButton btnEliminarUbicacion;
	private JButton btnMostrarUbicaciones;

	private JPanel panelBuscarRol;
	private JPanel panelRoles;
	private JPanel btnPanelRol;
	private JScrollPane rolScrollPane;
	private JTable tablaRoles;
	private JButton btnAgregarRol;
	private JButton btnActualizarRol;
	private JButton btnEliminarRol;
	private JButton btnMostrarRoles;

	private JPanel panelBuscarRegistro;
	private JPanel panelRegistros;
	private JPanel btnPanelRegistro;
	private JScrollPane registroScrollPane;
	private JButton btnMostrarRegistros;
	private JTable tablaRegistros;

	Usuario usuarioActual = null;
	String serverIP;
	private JButton btnCrearReporteArticulo;
	private JButton btnMostrarArticulos;
	private JButton btnMostrarUsuarios;
	private JButton btnCrearReporteUsuario;
	private JButton btnCrearReporteReporte;
	private JButton btnCrearReporteUbicacion;
	private JButton btnCrearReporteRegistro;
	private JButton btnImportarDatosDe;

	public VentanaOpciones(Usuario usuario, String serverIP) {

		usuarioActual = usuario;
		this.serverIP = serverIP;
		setTitle("Inventario Bienes Institucionales - Escuela Villa Nueva");

		setBounds(100, 100, 1000, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		// Articulos
		panelArticulo = new JPanel();
		tabbedPane.addTab("Articulos", null, panelArticulo, null);
		panelArticulo.setLayout(new BorderLayout(0, 0));

		articuloScrollPanel = new JScrollPane();
		panelArticulo.add(articuloScrollPanel, BorderLayout.CENTER);

		tablaArticulos = new AjusteColumnasTabla();
		tablaArticulos.setToolTipText("");

		articuloScrollPanel.setViewportView(tablaArticulos);

		panelBuscarArticulo = new JPanel();
		panelArticulo.add(panelBuscarArticulo, BorderLayout.NORTH);
		panelBuscarArticulo.setBorder(null);
		FlowLayout fl_panelBuscarArticulo = (FlowLayout) panelBuscarArticulo
				.getLayout();
		fl_panelBuscarArticulo.setAlignment(FlowLayout.LEFT);

		JLabel lblIngresarArticulo = new JLabel(
				"Ingrese alguna palabra o número clave para buscar artículo");
		panelBuscarArticulo.add(lblIngresarArticulo);

		buscarArticuloTxt = new JTextField();
		panelBuscarArticulo.add(buscarArticuloTxt);
		buscarArticuloTxt.setColumns(10);

		btnBuscarArticulo = new JButton("Buscar Artículo");
		panelBuscarArticulo.add(btnBuscarArticulo);
		btnBuscarArticulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filtro = buscarArticuloTxt.getText();
				((TablaArticulosModelo) tablaArticulos.getModel())
						.setFiltro(filtro);
				tablaArticulos.repaint();
			}
		});

		btnPanelArticulo = new JPanel();
		panelArticulo.add(btnPanelArticulo, BorderLayout.SOUTH);

		btnAgregarArticulo = new JButton("Agregar Artículo");
		btnAgregarArticulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaArticulos ventanaArticulos = new VentanaArticulos(
						VentanaOpciones.this, usuarioActual, serverIP);
				ventanaArticulos.setVisible(true);
			}
		});
		btnPanelArticulo.add(btnAgregarArticulo);

		btnActualizarArticulo = new JButton("Actualizar Artículo");
		btnActualizarArticulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = tablaArticulos.getSelectedRow();
				// asegurarse de que se seleccione una fila
				if (row < 0) {
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Debes seleccionar un Artículo", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// obtener articulo actual
				Articulo tempArticulo = (Articulo) tablaArticulos.getValueAt(
						row, TablaArticulosModelo.colObjeto);
				// crear ventana
				VentanaArticulos ventanaArticulos = new VentanaArticulos(
						VentanaOpciones.this, usuarioActual, tempArticulo,
						true, serverIP);
				// mostar ventana
				ventanaArticulos.setVisible(true);
			}
		});
		btnPanelArticulo.add(btnActualizarArticulo);

		btnEliminarArticulo = new JButton("Eliminar Artículo");
		btnEliminarArticulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// obtener fila seleccionada
					int row = tablaArticulos.getSelectedRow();

					// asegurarse de que se seleccione una fila
					if (row < 0) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Debes seleccionar un articulo", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					Articulo articulo = (Articulo) tablaArticulos.getValueAt(
							row, TablaArticulosModelo.colObjeto);
					// Ventana confirmacion a usuario
					int respuesta = JOptionPane.showConfirmDialog(
							VentanaOpciones.this,
							"Está seguro que desea eliminar este artículo?",
							"Confirmar", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (respuesta != JOptionPane.YES_OPTION) {
						return;
					}
					ArticuloCliente articuloCliente = new ArticuloCliente(
							usuarioActual.getNombreUsuario(), usuarioActual
									.getClave(), VentanaOpciones.this
									.getServidor());
					boolean resultado = articuloCliente.eliminar(articulo
							.getIdArticulo());
					if (resultado) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Exitoso", "Resultado de la Eliminación",
								JOptionPane.INFORMATION_MESSAGE);
						refrescarVistaArticulos(true);
					} else {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Fallo", "Resultado de la Eliminación",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception exc) {
					logger.log(Level.SEVERE, "Error eliminando artículo", e);
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Error eliminando artículo: " + exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPanelArticulo.add(btnEliminarArticulo);

		btnMostrarArticulos = new JButton("Mostrar Artículos");
		btnMostrarArticulos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refrescarVistaArticulos(true);
			}
		});
		btnPanelArticulo.add(btnMostrarArticulos);

		btnCrearReporteArticulo = new JButton("Crear Reporte");
		btnCrearReporteArticulo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte ventanaReporte = new VentanaReporte(
						usuarioActual, tablaArticulos, "Artículos");
				ventanaReporte
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaReporte.setVisible(true);
			}
		});
		btnPanelArticulo.add(btnCrearReporteArticulo);

		// IMPORTAR DE EXCEL
		btnImportarDatosDe = new JButton("Importar Datos de Excel");
		btnImportarDatosDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					VentanaOpciones.this.setCursor(new Cursor(
							Cursor.WAIT_CURSOR));
					ImportarDatosExcel importar = new ImportarDatosExcel(
							usuarioActual, serverIP, VentanaOpciones.this);
					importar.importar();
					refrescarVistaArticulos(false);
					refrescarVistaUbicaciones(false);
				} finally {
					VentanaOpciones.this.setCursor(new Cursor(
							Cursor.DEFAULT_CURSOR));
				}
			}
		});
		btnPanelArticulo.add(btnImportarDatosDe);

		// Usuarios
		panelUsuario = new JPanel();
		tabbedPane.addTab("Usuarios", null, panelUsuario, null);
		panelUsuario.setLayout(new BorderLayout(0, 0));

		userScrollPane = new JScrollPane();
		panelUsuario.add(userScrollPane, BorderLayout.CENTER);

		tablaUsuarios = new AjusteColumnasTabla();
		userScrollPane.setViewportView(tablaUsuarios);

		btnPanelUsuario = new JPanel();
		panelUsuario.add(btnPanelUsuario, BorderLayout.SOUTH);

		panelBuscarUsuario = new JPanel();
		panelUsuario.add(panelBuscarUsuario, BorderLayout.NORTH);
		panelBuscarUsuario.setBorder(null);
		FlowLayout fl_panelBuscarUsuario = (FlowLayout) panelBuscarUsuario
				.getLayout();
		fl_panelBuscarUsuario.setAlignment(FlowLayout.LEFT);

		JLabel lblIngresarUsuario = new JLabel(
				"Ingrese alguna palabra clave para buscar usuario");
		panelBuscarUsuario.add(lblIngresarUsuario);

		buscarUsuarioTxt = new JTextField();
		panelBuscarUsuario.add(buscarUsuarioTxt);
		buscarUsuarioTxt.setColumns(10);

		btnBuscarUsuario = new JButton("Buscar Usuario");
		panelBuscarUsuario.add(btnBuscarUsuario);
		btnBuscarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filtro = buscarUsuarioTxt.getText();
				((TablaUsuariosModelo) tablaUsuarios.getModel())
						.setFiltro(filtro);
				tablaUsuarios.repaint();
			}
		});
		btnAgregarUsuario = new JButton("Agregar Usuario");
		btnAgregarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaUsuario ventanaUsuarios = new VentanaUsuario(
						VentanaOpciones.this, usuarioActual, serverIP);
				ventanaUsuarios.setVisible(true);
			}
		});
		btnPanelUsuario.add(btnAgregarUsuario);

		btnActualizarUsuario = new JButton("Actualizar Usuario");
		btnActualizarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tablaUsuarios.getSelectedRow();
				// asegurarse de que se seleccione una fila
				if (row < 0) {
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Debes seleccionar un usuario", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// obtener usuario actual
				Usuario tempUsuario = (Usuario) tablaUsuarios.getValueAt(row,
						TablaUsuariosModelo.colObjeto);
				// crear ventana
				VentanaUsuario ventanaUsuario = new VentanaUsuario(
						VentanaOpciones.this, usuarioActual, tempUsuario, true,
						serverIP);
				// mostrar ventana
				ventanaUsuario.setVisible(true);
			}
		});
		btnPanelUsuario.add(btnActualizarUsuario);

		btnEliminarUsuario = new JButton("Eliminar Usuario");
		btnEliminarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// obtener fila seleccionada
					int row = tablaUsuarios.getSelectedRow();

					// asegurarse de que se seleccione una fila
					if (row < 0) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Debes seleccionar un usuario", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Usuario usuario = (Usuario) tablaUsuarios.getValueAt(row,
							TablaUsuariosModelo.colObjeto);
					if (usuario.getNombreUsuario().equals(
							usuarioActual.getNombreUsuario())) {
						JOptionPane
								.showMessageDialog(
										VentanaOpciones.this,
										"No puedes eliminar el usuario actualmente en Sesión",
										"Fallo", JOptionPane.ERROR_MESSAGE);
						return;
					}
					// Ventana confirmacion a usuario
					int respuesta = JOptionPane.showConfirmDialog(
							VentanaOpciones.this,
							"Está seguro que desea eliminar este usuario?",
							"Confirmar", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (respuesta != JOptionPane.YES_OPTION) {
						return;
					}

					UsuarioCliente usuarioCliente = new UsuarioCliente(
							usuarioActual.getNombreUsuario(), usuarioActual
									.getClave(), VentanaOpciones.this
									.getServidor());
					boolean resultado = usuarioCliente.eliminar(usuario
							.getIdUsuario());
					if (resultado) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Exitoso", "Resultado de la Eliminacion",
								JOptionPane.INFORMATION_MESSAGE);
						refrescarVistaUsuario(true);
					} else {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Fallo", "Resultado de la Eliminación",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception exc) {
					logger.log(Level.SEVERE, "Error eliminando usuario", e);
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Error eliminando usuario: " + exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPanelUsuario.add(btnEliminarUsuario);

		btnMostrarUsuarios = new JButton("Mostrar Usuarios");
		btnMostrarUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refrescarVistaUsuario(true);
			}
		});
		btnPanelUsuario.add(btnMostrarUsuarios);

		btnCrearReporteUsuario = new JButton("Crear Reporte");
		btnCrearReporteUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte ventanaReporte = new VentanaReporte(
						usuarioActual, tablaUsuarios, "Usuarios");
				ventanaReporte
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaReporte.setVisible(true);
			}
		});
		btnPanelUsuario.add(btnCrearReporteUsuario);

		// Roles
		panelRoles = new JPanel();
		tabbedPane.addTab("Roles", null, panelRoles, null);
		panelRoles.setLayout(new BorderLayout(0, 0));

		rolScrollPane = new JScrollPane();
		panelRoles.add(rolScrollPane, BorderLayout.CENTER);

		tablaRoles = new AjusteColumnasTabla();
		rolScrollPane.setViewportView(tablaRoles);

		btnPanelRol = new JPanel();
		panelRoles.add(btnPanelRol, BorderLayout.SOUTH);

		panelBuscarRol = new JPanel();
		panelRoles.add(panelBuscarRol, BorderLayout.NORTH);
		panelBuscarRol.setBorder(null);
		FlowLayout fl_panelBuscarRol = (FlowLayout) panelBuscarRol.getLayout();
		fl_panelBuscarRol.setAlignment(FlowLayout.LEFT);

		JLabel lblIngresarRol = new JLabel(
				"Ingrese alguna palabra clave para buscar roles");
		panelBuscarRol.add(lblIngresarRol);

		buscarRolTxt = new JTextField();
		panelBuscarRol.add(buscarRolTxt);
		buscarRolTxt.setColumns(10);

		btnBuscarRol = new JButton("Buscar Rol");
		panelBuscarRol.add(btnBuscarRol);
		btnBuscarRol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filtro = buscarRolTxt.getText();
				((TablaRolesModelo) tablaRoles.getModel()).setFiltro(filtro);
				tablaRoles.repaint();
			}
		});

		btnAgregarRol = new JButton("Agregar Rol");
		btnAgregarRol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaRol ventanaRol = new VentanaRol(VentanaOpciones.this,
						usuarioActual, serverIP);
				ventanaRol.setVisible(true);
			}
		});
		btnPanelRol.add(btnAgregarRol);

		btnActualizarRol = new JButton("Actualizar Rol");
		btnActualizarRol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = tablaRoles.getSelectedRow();
				// asegurarse de que una fila sea seleccionada
				if (row < 0) {
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Debes seleccionar un rol", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// obtener rol actual
				Rol tempRol = (Rol) tablaRoles.getValueAt(row,
						TablaRolesModelo.colObjeto);
				// crear ventana
				VentanaRol ventanaRol = new VentanaRol(VentanaOpciones.this,
						usuarioActual, tempRol, true, serverIP);
				// mostrar ventana
				ventanaRol.setVisible(true);
			}
		});
		btnPanelRol.add(btnActualizarRol);

		btnEliminarRol = new JButton("Eliminar Rol");
		btnEliminarRol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// obtener fila seleccionada
					int row = tablaRoles.getSelectedRow();

					// asegurarse de que una fila sea seleccionada
					if (row < 0) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Debes seleccionar un rol", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Rol rol = (Rol) tablaRoles.getValueAt(row,
							TablaRolesModelo.colObjeto);

					// Ventana confirmacion a usuario
					int respuesta = JOptionPane.showConfirmDialog(
							VentanaOpciones.this,
							"Está seguro que desea eliminar este rol?",
							"Confirmar", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (respuesta != JOptionPane.YES_OPTION) {
						return;
					}
					RolCliente rolCliente = new RolCliente(usuarioActual
							.getNombreUsuario(), usuarioActual.getClave(),
							VentanaOpciones.this.getServidor());
					boolean resultado = rolCliente.eliminar(rol.getIdRol());
					if (resultado) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Exitoso", "Resultado de la Eliminación",
								JOptionPane.INFORMATION_MESSAGE);
						refrescarVistaRoles(true);
					} else {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Fallo", "Resultado de la Eliminación",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception exc) {
					logger.log(Level.SEVERE, "Error eliminando rol", exc);
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Error eliminando rol: " + exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPanelRol.add(btnEliminarRol);

		btnMostrarRoles = new JButton("Mostrar Roles");
		btnMostrarRoles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refrescarVistaRoles(true);
			}
		});
		btnPanelRol.add(btnMostrarRoles);

		btnCrearReporteReporte = new JButton("Crear Reporte");
		btnCrearReporteReporte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte ventanaReporte = new VentanaReporte(
						usuarioActual, tablaRoles, "Roles");
				ventanaReporte
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaReporte.setVisible(true);
			}
		});
		btnPanelRol.add(btnCrearReporteReporte);

		topPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) topPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(topPanel, BorderLayout.NORTH);

		lblLoggedIn = new JLabel("Sesión iniciada por: ");
		topPanel.add(lblLoggedIn);

		usuarioLogueadoLbl = new JLabel(usuarioActual.getNombre() + " "
				+ usuarioActual.getApellido1() + " "
				+ usuarioActual.getApellido2());
		topPanel.add(usuarioLogueadoLbl);

		// Ubicaciones
		panelUbicaciones = new JPanel();
		tabbedPane.addTab("Ubicaciones", null, panelUbicaciones, null);
		panelUbicaciones.setLayout(new BorderLayout(0, 0));

		ubicacionesScrollPane = new JScrollPane();
		panelUbicaciones.add(ubicacionesScrollPane, BorderLayout.CENTER);

		tablaUbicaciones = new AjusteColumnasTabla();
		ubicacionesScrollPane.setViewportView(tablaUbicaciones);

		btnPanelUbicacion = new JPanel();
		panelUbicaciones.add(btnPanelUbicacion, BorderLayout.SOUTH);

		panelBuscarUbicacion = new JPanel();
		panelUbicaciones.add(panelBuscarUbicacion, BorderLayout.NORTH);
		panelBuscarUbicacion.setBorder(null);
		FlowLayout fl_panelBuscarUbicacion = (FlowLayout) panelBuscarUbicacion
				.getLayout();
		fl_panelBuscarUbicacion.setAlignment(FlowLayout.LEFT);

		JLabel lblIngresarUbicacion = new JLabel(
				"Ingrese alguna palabra clave para buscar ubicación");
		panelBuscarUbicacion.add(lblIngresarUbicacion);

		buscarUbicacionTxt = new JTextField();
		panelBuscarUbicacion.add(buscarUbicacionTxt);
		buscarUbicacionTxt.setColumns(10);

		btnBuscarUbicacion = new JButton("Buscar Ubicacion");
		panelBuscarUbicacion.add(btnBuscarUbicacion);
		btnBuscarUbicacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filtro = buscarUbicacionTxt.getText();
				((TablaUbicacionesModelo) tablaUbicaciones.getModel())
						.setFiltro(filtro);
				tablaUbicaciones.repaint();
			}
		});

		btnAgregarUbicacion = new JButton("Agregar Ubicación");
		btnAgregarUbicacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaUbicacion ventanaUbicacion = new VentanaUbicacion(
						VentanaOpciones.this, usuarioActual, serverIP);
				ventanaUbicacion.setVisible(true);
			}
		});
		btnPanelUbicacion.add(btnAgregarUbicacion);

		btnActualizarUbicacion = new JButton("Actualizar Ubicación");
		btnActualizarUbicacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int row = tablaUbicaciones.getSelectedRow();
				// asegurarse de que una fila sea seleccionada
				if (row < 0) {
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Debes seleccionar una ubicación", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// obtener ubicacion actual
				Ubicacion tempUbicacion = (Ubicacion) tablaUbicaciones
						.getValueAt(row, TablaUbicacionesModelo.colObjeto);
				// crear ventana
				VentanaUbicacion ventanaUbicacion = new VentanaUbicacion(
						VentanaOpciones.this, usuarioActual, tempUbicacion,
						true, serverIP);
				// mostrar ventana
				ventanaUbicacion.setVisible(true);
			}
		});
		btnPanelUbicacion.add(btnActualizarUbicacion);

		btnEliminarUbicacion = new JButton("Eliminar Ubicación");
		btnEliminarUbicacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// obtener fila seleccionada
					int row = tablaUbicaciones.getSelectedRow();

					// asegurarse de que una fila sea seleccionada
					if (row < 0) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Debes seleccionar una ubicación", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					Ubicacion ubicacion = (Ubicacion) tablaUbicaciones
							.getValueAt(row, TablaUbicacionesModelo.colObjeto);

					// ventana confirmacion a usuario
					int respuesta = JOptionPane.showConfirmDialog(
							VentanaOpciones.this,
							"Está seguro que desea eliminar esta ubicación?",
							"Confirmar", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

					if (respuesta != JOptionPane.YES_OPTION) {
						return;
					}
					UbicacionCliente ubicacionCliente = new UbicacionCliente(
							usuarioActual.getNombreUsuario(), usuarioActual
									.getClave(), VentanaOpciones.this
									.getServidor());
					logger.log(Level.INFO,
							"idUbicacion=" + ubicacion.getIdUbicacion());
					boolean resultado = ubicacionCliente.eliminar(ubicacion
							.getIdUbicacion());
					if (resultado) {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Exitoso", "Resultado de la Eliminación",
								JOptionPane.INFORMATION_MESSAGE);
						refrescarVistaUbicaciones(true);
					} else {
						JOptionPane.showMessageDialog(VentanaOpciones.this,
								"Fallo", "Resultado de la Eliminación",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception exc) {
					logger.log(Level.SEVERE, "Error eliminando ubicación", exc);
					JOptionPane.showMessageDialog(VentanaOpciones.this,
							"Error eliminando ubicacion: " + exc.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPanelUbicacion.add(btnEliminarUbicacion);

		btnMostrarUbicaciones = new JButton("Mostrar Ubicaciones");
		btnMostrarUbicaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refrescarVistaUbicaciones(true);
			}
		});
		btnPanelUbicacion.add(btnMostrarUbicaciones);

		btnCrearReporteUbicacion = new JButton("Crear Reporte");
		btnCrearReporteUbicacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte ventanaReporte = new VentanaReporte(
						usuarioActual, tablaUbicaciones, "Ubicaciones");
				ventanaReporte
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaReporte.setVisible(true);
			}
		});
		btnPanelUbicacion.add(btnCrearReporteUbicacion);

		// Registros
		panelRegistros = new JPanel();
		tabbedPane.addTab("Registros", null, panelRegistros, null);
		panelRegistros.setLayout(new BorderLayout(0, 0));

		registroScrollPane = new JScrollPane();
		panelRegistros.add(registroScrollPane, BorderLayout.CENTER);

		tablaRegistros = new AjusteColumnasTabla();
		registroScrollPane.setViewportView(tablaRegistros);

		btnPanelRegistro = new JPanel();
		panelRegistros.add(btnPanelRegistro, BorderLayout.SOUTH);

		panelBuscarRegistro = new JPanel();
		panelRegistros.add(panelBuscarRegistro, BorderLayout.NORTH);
		panelBuscarRegistro.setBorder(null);
		FlowLayout fl_panelBuscarRegistro = (FlowLayout) panelBuscarRegistro
				.getLayout();
		fl_panelBuscarRegistro.setAlignment(FlowLayout.LEFT);

		JLabel lblIngresarRegistro = new JLabel(
				"Ingrese alguna palabra clave para buscar registros");
		panelBuscarRegistro.add(lblIngresarRegistro);

		buscarRegistroTxt = new JTextField();
		panelBuscarRegistro.add(buscarRegistroTxt);
		buscarRegistroTxt.setColumns(10);

		btnBuscarRegistro = new JButton("Buscar Registro");
		panelBuscarRegistro.add(btnBuscarRegistro);
		btnBuscarRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filtro = buscarRegistroTxt.getText();
				((TablaRegistrosModelo) tablaRegistros.getModel())
						.setFiltro(filtro);
				tablaRegistros.repaint();
			}
		});

		btnMostrarRegistros = new JButton("Mostrar Registros");
		btnMostrarRegistros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refrescarVistaRegistros(true);
			}
		});
		btnPanelRegistro.add(btnMostrarRegistros);

		btnCrearReporteRegistro = new JButton("Crear Reporte ");
		btnCrearReporteRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaReporte ventanaReporte = new VentanaReporte(
						usuarioActual, tablaRegistros, "Registros");
				ventanaReporte
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaReporte.setVisible(true);
			}
		});
		btnPanelRegistro.add(btnCrearReporteRegistro);
		refrescarVistaUbicaciones(false);
		refrescarVistaArticulos(false);
		refrescarVistaUsuario(false);
		refrescarVistaRoles(false);
		refrescarVistaRegistros(false);
		cerrar();

	}

	public void refrescarVistaUsuario(boolean mostrarError) {
		TablaUsuariosModelo modelo;
		try {
			UsuarioCliente usuarioCliente = new UsuarioCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					VentanaOpciones.this.getServidor());
			List<Usuario> usuarios = usuarioCliente.mostrarTodo();
			if (mostrarError && usuarioCliente.mostrarTodo().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"La Tabla de Usuarios esta vacía");
			}
			// crear modelo y actualizar tabla
			modelo = new TablaUsuariosModelo(usuarios, usuarioActual, serverIP);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error refrescando vista Usuario", exc);
			if (mostrarError) {
				JOptionPane.showMessageDialog(this,
						"Error: " + exc.getMessage(),
						"Error: " + exc.getHttpCode(),
						JOptionPane.ERROR_MESSAGE);
			}
			modelo = new TablaUsuariosModelo();
		}
		tablaUsuarios.setModel(modelo);
	}

	public TablaUsuariosModelo getTablaUsuariosModelo() {
		return (TablaUsuariosModelo) tablaUsuarios.getModel();
	}

	public void refrescarVistaRoles(boolean mostrarError) {
		TablaRolesModelo modelo;
		try {
			RolCliente rolCliente = new RolCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					VentanaOpciones.this.getServidor());
			List<Rol> roles = rolCliente.mostrarTodo();
			if (mostrarError && rolCliente.mostrarTodo().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"La Tabla de Roles esta vacía");
			}
			// crear modelo y actualizar tabla
			modelo = new TablaRolesModelo(roles);

		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error refrescando vista Roles", exc);
			if (mostrarError) {
				JOptionPane.showMessageDialog(this,
						"Error: " + exc.getMessage(),
						"Error: " + exc.getHttpCode(),
						JOptionPane.ERROR_MESSAGE);
			}
			modelo = new TablaRolesModelo();
		}
		tablaRoles.setModel(modelo);
	}

	public TablaRolesModelo getTablaRolesModelo() {
		return (TablaRolesModelo) tablaRoles.getModel();
	}

	public void refrescarVistaArticulos(boolean mostrarError) {
		TablaArticulosModelo modelo;
		try {

			ArticuloCliente articuloCliente = new ArticuloCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					VentanaOpciones.this.getServidor());
			List<Articulo> articulos = articuloCliente.mostrarTodo();
			if (mostrarError && articuloCliente.mostrarTodo().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"La Tabla de Artículos esta vacía");
			}
			// crear modelo y actualizar tabla
			modelo = new TablaArticulosModelo(articulos, usuarioActual,
					serverIP);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error refrescando vista artículos", exc);
			if (mostrarError) {
				JOptionPane.showMessageDialog(this,
						"Error: " + exc.getMessage(),
						"Error: " + exc.getHttpCode(),
						JOptionPane.ERROR_MESSAGE);
			}
			modelo = new TablaArticulosModelo();
		}
		tablaArticulos.setModel(modelo);
	}

	public TablaArticulosModelo getTablaArticulosModelo() {
		return (TablaArticulosModelo) tablaArticulos.getModel();
	}

	public void refrescarVistaUbicaciones(boolean mostrarError) {
		TablaUbicacionesModelo modelo;
		try {
			UbicacionCliente ubicacionCliente = new UbicacionCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					VentanaOpciones.this.getServidor());
			List<Ubicacion> ubicaciones = ubicacionCliente.mostrarTodo();
			if (mostrarError && ubicacionCliente.mostrarTodo().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"La Tabla de Ubicaciones esta vacía");
			}
			// crear modelo y actualizar tabla
			modelo = new TablaUbicacionesModelo(ubicaciones);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error refrescando vista ubicaciones", exc);
			if (mostrarError) {
				JOptionPane.showMessageDialog(this,
						"Error: " + exc.getMessage(),
						"Error: " + exc.getHttpCode(),
						JOptionPane.ERROR_MESSAGE);
			}
			modelo = new TablaUbicacionesModelo();
		}
		tablaUbicaciones.setModel(modelo);
	}

	public TablaUbicacionesModelo getTablaUbicacionesModelo() {
		return (TablaUbicacionesModelo) tablaUbicaciones.getModel();
	}

	public void refrescarVistaRegistros(boolean mostrarError) {
		TablaRegistrosModelo modelo;
		try {
			RegistroCliente registroCliente = new RegistroCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					VentanaOpciones.this.getServidor());
			List<Registro> registros = registroCliente.mostrarTodo();
			if (mostrarError && registroCliente.mostrarTodo().isEmpty()) {
				JOptionPane.showMessageDialog(this,
						"La Tabla de Registros esta vacía");
			}
			// crear modelo y actualizar tabla
			modelo = new TablaRegistrosModelo(registros, usuarioActual,
					serverIP);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error refrescando vista registro", exc);
			if (mostrarError) {
				JOptionPane.showMessageDialog(this,
						"Error: " + exc.getMessage(),
						"Error: " + exc.getHttpCode(),
						JOptionPane.ERROR_MESSAGE);
			}
			modelo = new TablaRegistrosModelo();
		}
		tablaRegistros.setModel(modelo);
	}

	public int obtenerIdUsuarioActual() {
		return idUsuario;
	}

	public int obtenerIdArticuloActual() {
		return idArticulo;
	}

	public int obtenerIdRolActual() {
		return idRol;
	}

	public int obtenerIdUbicacionActual() {
		return idUbicacion;
	}

	public int obtenerIdRegistroActual() {
		return idRegistro;
	}

	public String getServidor() {
		return serverIP;
	}

	public void cerrar() {
		try {
			this.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(WindowEvent e) {
					// No es necesario implementar
				}

				@Override
				public void windowClosing(WindowEvent e) {
					RegistroCliente registroCliente = new RegistroCliente(
							usuarioActual.getNombreUsuario(), usuarioActual
									.getClave(), getServidor());
					Registro registro = new Registro("Cerró Sesión",
							usuarioActual.getIdUsuario(), "");
					try {
						registroCliente.agregar(registro);
					} catch (RequestFailureException e1) {
						logger.log(Level.SEVERE, "Error agregando registro", e1);
					}

				}

				@Override
				public void windowClosed(WindowEvent e) {
					// No es necesario implementar

				}

				@Override
				public void windowIconified(WindowEvent e) {
					// No es necesario implementar

				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					// No es necesario implementar

				}

				@Override
				public void windowActivated(WindowEvent e) {
					// No es necesario implementar

				}

				@Override
				public void windowDeactivated(WindowEvent e) {
					// No es necesario implementar

				}
			});
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void confirmarSalida() {
		int valor = JOptionPane.showConfirmDialog(this,
				"Está seguro que desea salir de la Aplicación?", "Advertencia",
				JOptionPane.YES_NO_OPTION);
		if (valor == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}
