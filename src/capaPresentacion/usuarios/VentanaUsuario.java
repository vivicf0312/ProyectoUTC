package capaPresentacion.usuarios;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utilidades.Encriptacion;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaAPICliente.RolCliente;
import capaAPICliente.UsuarioCliente;
import capaNegocio.Rol;
import capaNegocio.Usuario;
import capaPresentacion.VentanaOpciones;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class VentanaUsuario extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nombreTxt;
	private JTextField apellido1Txt;
	private JTextField apellido2Txt;
	private JTextField nombreUsuarioTxt;
	private JComboBox<String> rolComboBox;

	private VentanaOpciones ventanaOpciones;
	private boolean modoActualizar = false;
	private Usuario usuarioAnterior;
	private JPasswordField campoClaveTxt;
	private JPasswordField campoConfirmarClaveTxt;
	private JLabel lblClave;
	private JLabel lblClaveConfirmada;
	private HashMap<String, Integer> rolMap = new HashMap<String, Integer>();
	private Usuario usuarioActual = null;
	private String serverIP;
	Logger logger = Logging.obtenerClientLogger();

	public VentanaUsuario(VentanaOpciones ventanaOpciones,
			Usuario usuarioActual, Usuario usuarioAnterior,
			boolean modoActualizar, String serverIP) {
		this.ventanaOpciones = ventanaOpciones;
		this.usuarioActual = usuarioActual;
		this.usuarioAnterior = usuarioAnterior;
		this.modoActualizar = modoActualizar;
		this.serverIP = serverIP;
		init();

		if (modoActualizar) {
			setTitle("Actualizar Usuario");
			crearGui(usuarioAnterior);
		} else {
			setTitle("Agregar Usuario");
		}
	}

	public VentanaUsuario(VentanaOpciones ventanaOpciones,
			Usuario usuarioActual, String serverIP) {
		this(ventanaOpciones, usuarioActual, null, false, serverIP);
	}

	private void init() {

		setBounds(100, 100, 450, 278);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel
				.setLayout((LayoutManager) new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"), }, new RowSpec[] {
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, }));
		{
			JLabel lblNombre = new JLabel("Nombre");
			lblNombre.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNombre, "2, 2, right, default");
		}
		{
			nombreTxt = new JTextField();
			contentPanel.add(nombreTxt, "4, 2, fill, default");
			nombreTxt.setColumns(10);
		}
		{
			JLabel lblApellido1 = new JLabel("Primer Apellido");
			lblApellido1.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblApellido1, "2, 4, right, default");
		}
		{
			apellido1Txt = new JTextField();
			contentPanel.add(apellido1Txt, "4, 4, fill, default");
			apellido1Txt.setColumns(10);
		}
		{
			JLabel lblApellido2 = new JLabel("Segundo Apellido");
			lblApellido2.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblApellido2, "2, 6, right, default");
		}
		{
			apellido2Txt = new JTextField();
			contentPanel.add(apellido2Txt, "4, 6, fill, default");
			apellido2Txt.setColumns(10);
		}
		{
			JLabel lblNombreUsuario = new JLabel("Nombre Usuario");
			lblNombreUsuario.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNombreUsuario, "2, 8, right, default");
		}
		{
			nombreUsuarioTxt = new JTextField();
			contentPanel.add(nombreUsuarioTxt, "4, 8, fill, default");
			nombreUsuarioTxt.setColumns(10);
		}
		{
			JLabel lblRol = new JLabel("Rol");
			lblRol.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblRol, "2, 10");
		}
		{
			rolComboBox = new JComboBox<String>();
			rolComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

				}
			});
			try {
				RolCliente rolCliente = new RolCliente(
						usuarioActual.getNombreUsuario(),
						usuarioActual.getClave(), serverIP);
				List<Rol> roles = rolCliente.mostrarTodo();
				for (Rol rol : roles) {
					rolComboBox.addItem(rol.getDescripcion());
					rolMap.put(rol.getDescripcion(), rol.getIdRol());
				}
			} catch (Exception exc) {
				logger.log(Level.SEVERE, "Error mostrando roles del comboBox",
						exc);
			}
			contentPanel.add(rolComboBox, "4, 10");
		}

		{
			lblClave = new JLabel("Clave");
			lblClave.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblClave, "2, 12, right, default");
		}
		{
			campoClaveTxt = new JPasswordField();
			contentPanel.add(campoClaveTxt, "4, 12, fill, default");
		}
		{
			lblClaveConfirmada = new JLabel("Confirmar Clave");
			lblClaveConfirmada.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblClaveConfirmada, "2, 14, right, default");
		}
		{
			campoConfirmarClaveTxt = new JPasswordField();
			contentPanel.add(campoConfirmarClaveTxt, "4, 14, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						guardarUsuario();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancelar");
				buttonPane.add(cancelButton);
			}
		}
	}

	private void crearGui(Usuario usuario) {

		nombreTxt.setText(usuario.getNombre());
		apellido1Txt.setText(usuario.getApellido1());
		apellido2Txt.setText(usuario.getApellido2());
		nombreUsuarioTxt.setText(usuario.getNombreUsuario());
		rolComboBox.setSelectedItem(rolComboBox);
		campoClaveTxt.setText(usuario.getClave());
		campoConfirmarClaveTxt.setText(usuario.getClave());
	}

	private void guardarUsuario() {
		String nombre = nombreTxt.getText();
		String apellido1 = apellido1Txt.getText();
		String apellido2 = apellido2Txt.getText();
		String nombreUsuario = nombreUsuarioTxt.getText();
		int idRol = rolMap.get(rolComboBox.getSelectedItem().toString());
		String clave = new String(campoClaveTxt.getPassword());
		String confirmarClave = new String(campoConfirmarClaveTxt.getPassword());
		Usuario usuario = null;
		if (nombre.isEmpty() || apellido1.isEmpty() || apellido2.isEmpty()
				|| nombreUsuario.isEmpty() || clave.isEmpty()
				|| confirmarClave.isEmpty()) {
			JOptionPane.showMessageDialog(VentanaUsuario.this,
					"Debes llenar toda la información, intenta de nuevo.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;

		} else {
			String campoConfirmarClave = new String(
					campoConfirmarClaveTxt.getPassword());
			logger.log(Level.INFO, "clave=" + clave + ",confirmarClave= "
					+ campoConfirmarClave);
			if (!campoConfirmarClave.equals(clave)) {
				JOptionPane.showMessageDialog(ventanaOpciones,
						"Contraseñas no coinciden", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			TablaUsuariosModelo tablaUsuarios = ventanaOpciones
					.getTablaUsuariosModelo();
			for (Usuario usuarioExistente : tablaUsuarios.getUsuarios()) {
				if (usuarioExistente.getNombreUsuario().equals(nombreUsuario)
						&& (!modoActualizar || usuarioExistente.getIdUsuario() != usuarioAnterior
								.getIdUsuario())) {
					JOptionPane.showMessageDialog(ventanaOpciones,
							"Usuario ya existe", "Creación Fallida",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			if (modoActualizar) {
				usuario = usuarioAnterior;
				usuario.setNombre(nombre);
				usuario.setApellido1(apellido1);
				usuario.setApellido2(apellido2);
				usuario.setNombreUsuario(nombreUsuario);
				usuario.setIdRol(idRol);
				if (!usuario.getClave().equals(clave)) {
					String claveEncriptada = Encriptacion.encriptar(clave);
					usuario.setClave(claveEncriptada);
				}
			} else {
				String claveEncriptada = Encriptacion.encriptar(clave);
				usuario = new Usuario(nombre, apellido1, apellido2,
						nombreUsuario, claveEncriptada, idRol);
			}
		}
		try {

			// guardar en BD
			UsuarioCliente usuarioCliente = new UsuarioCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					serverIP);
			if (modoActualizar) {
				// revisar si es necesario actualizar los campos del GUI para el
				// usuario actual
				if (ventanaOpciones.obtenerIdUsuarioActual() == usuario
						.getIdUsuario()) {
					logger.log(Level.INFO, "Usuario ya existe");
				} else {
					usuarioCliente.actualizar(usuario);
				}
			} else {
				usuarioCliente.agregar(usuario);
			}

			// cerrar ventana
			setVisible(false);

			// refrescar lista de GUI
			ventanaOpciones.refrescarVistaUsuario(true);

			// mostrar mensaje exitoso
			JOptionPane.showMessageDialog(ventanaOpciones,
					"Usuario guardado exitosamente.", "Usuario guardado",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error guardando usuario", exc);
			JOptionPane.showMessageDialog(ventanaOpciones,
					"Error guardando usuario: " + exc.getMessage(), "Error: "
							+ exc.getHttpCode(), JOptionPane.ERROR_MESSAGE);
		}

	}
}
