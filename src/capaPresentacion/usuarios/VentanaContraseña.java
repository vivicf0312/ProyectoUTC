package capaPresentacion.usuarios;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utilidades.Encriptacion;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaAPICliente.UsuarioCliente;
import capaNegocio.Usuario;
import capaPresentacion.VentanaOpciones;

import com.sun.jersey.api.client.ClientHandlerException;

@SuppressWarnings("serial")
public class VentanaContraseña extends JFrame {

	private final JPanel panelContenido = new JPanel();
	private JPasswordField claveTxt;
	private JTextField nombreUsuarioTxt;
	private JTextField servidorTxt;

	Logger logger = Logging.obtenerClientLogger();

	public VentanaContraseña() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setTitle("Ventana de Inicio de Sesión");

		setBounds(100, 100, 450, 194);
		getContentPane().setLayout(new BorderLayout());
		panelContenido.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panelContenido, BorderLayout.CENTER);
		GridBagLayout gbl_panelContenido = new GridBagLayout();
		gbl_panelContenido.columnWidths = new int[] { 91, 321, 0 };
		gbl_panelContenido.rowHeights = new int[] { 20, 20, 20, 0 };
		gbl_panelContenido.columnWeights = new double[] { 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelContenido.rowWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panelContenido.setLayout(gbl_panelContenido);
		{
			JLabel lblUsuario = new JLabel("Nombre de Usuario");
			GridBagConstraints gbc_lblUsuario = new GridBagConstraints();
			gbc_lblUsuario.anchor = GridBagConstraints.EAST;
			gbc_lblUsuario.insets = new Insets(0, 0, 5, 5);
			gbc_lblUsuario.gridx = 0;
			gbc_lblUsuario.gridy = 0;
			panelContenido.add(lblUsuario, gbc_lblUsuario);
		}
		{
			nombreUsuarioTxt = new JTextField();
			GridBagConstraints gbc_nombreUsuarioTxt = new GridBagConstraints();
			gbc_nombreUsuarioTxt.anchor = GridBagConstraints.NORTH;
			gbc_nombreUsuarioTxt.fill = GridBagConstraints.HORIZONTAL;
			gbc_nombreUsuarioTxt.insets = new Insets(0, 0, 5, 0);
			gbc_nombreUsuarioTxt.gridx = 1;
			gbc_nombreUsuarioTxt.gridy = 0;
			panelContenido.add(nombreUsuarioTxt, gbc_nombreUsuarioTxt);
		}
		{
			JLabel lblClave = new JLabel("Clave");
			GridBagConstraints gbc_lblClave = new GridBagConstraints();
			gbc_lblClave.anchor = GridBagConstraints.EAST;
			gbc_lblClave.insets = new Insets(0, 0, 5, 5);
			gbc_lblClave.gridx = 0;
			gbc_lblClave.gridy = 1;
			panelContenido.add(lblClave, gbc_lblClave);
		}
		{
			claveTxt = new JPasswordField();
			GridBagConstraints gbc_claveTxt = new GridBagConstraints();
			gbc_claveTxt.anchor = GridBagConstraints.NORTH;
			gbc_claveTxt.fill = GridBagConstraints.HORIZONTAL;
			gbc_claveTxt.insets = new Insets(0, 0, 5, 0);
			gbc_claveTxt.gridx = 1;
			gbc_claveTxt.gridy = 1;
			panelContenido.add(claveTxt, gbc_claveTxt);
		}
		{
			JLabel lblServidor = new JLabel("IP del Servidor");
			GridBagConstraints gbc_lblServidor = new GridBagConstraints();
			gbc_lblServidor.anchor = GridBagConstraints.EAST;
			gbc_lblServidor.insets = new Insets(0, 0, 0, 5);
			gbc_lblServidor.gridx = 0;
			gbc_lblServidor.gridy = 2;
			panelContenido.add(lblServidor, gbc_lblServidor);
		}
		{
			servidorTxt = new JTextField("127.0.0.1");
			GridBagConstraints gbc_servidorTxt = new GridBagConstraints();
			gbc_servidorTxt.anchor = GridBagConstraints.NORTH;
			gbc_servidorTxt.fill = GridBagConstraints.HORIZONTAL;
			gbc_servidorTxt.gridx = 1;
			gbc_servidorTxt.gridy = 2;
			panelContenido.add(servidorTxt, gbc_servidorTxt);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Ingresar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						iniciarSesion();
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

	private void iniciarSesion() {

		try {
			String nombreUsuario = nombreUsuarioTxt.getText();
			String clave = new String(claveTxt.getPassword());
			String servidor = new String(servidorTxt.getText());
			// obtener ID Usuario
			if (nombreUsuario.isEmpty()) {
				JOptionPane.showMessageDialog(VentanaContraseña.this,
						"Debes escribir tu nombre de Usuario.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (servidor.isEmpty()) {
				JOptionPane.showMessageDialog(VentanaContraseña.this,
						"Debes escribir la dirección IP del servidor.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (clave.isEmpty()) {
				JOptionPane.showMessageDialog(VentanaContraseña.this,
						"Debes escribir tu clave.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String claveEncriptada = Encriptacion.encriptar(clave);
			UsuarioCliente usuarioCliente = new UsuarioCliente(nombreUsuario,
					claveEncriptada, servidor);
			Usuario usuarioActual = usuarioCliente.buscarUsuario();
			if (usuarioActual != null) {
				setVisible(false);
				VentanaOpciones ventanaOpciones = new VentanaOpciones(
						usuarioActual, servidor);
				ventanaOpciones
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaOpciones.setVisible(true);
			}

		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error durante el inicio de sesión", exc);
			JOptionPane.showMessageDialog(this,
					"Nombre de Usuario o clave inválidos, Intenta de Nuevo",
					"Error", JOptionPane.ERROR_MESSAGE);
		} catch (ClientHandlerException cex) {
			logger.log(Level.SEVERE, "Error durante el inicio de sesión", cex);
			JOptionPane.showMessageDialog(this,
					"No se puede conectar al servidor", "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error durante el inicio de sesión", ex);
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
