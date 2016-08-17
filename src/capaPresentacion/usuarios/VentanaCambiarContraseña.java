package capaPresentacion.usuarios;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
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

import com.sun.jersey.api.client.ClientHandlerException;

@SuppressWarnings("serial")
public class VentanaCambiarContraseña extends JFrame {

	private JPanel panelContenido;
	private JTextField nombreUsuarioTxt;
	private JPasswordField claveActualPwsd;
	private JPasswordField nuevaClavePwsd;
	private JTextField servidorTxt;
	Logger logger = Logging.obtenerClientLogger();
	private JPasswordField confirmarClavePwsd;

	public VentanaCambiarContraseña() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 312, 228);
		panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelContenido);
		GridBagLayout gbl_panelContenido = new GridBagLayout();
		gbl_panelContenido.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelContenido.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0 };
		gbl_panelContenido.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		gbl_panelContenido.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelContenido.setLayout(gbl_panelContenido);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cambiarClave();
			}
		});

		JLabel lblNombreUsuario = new JLabel("Nombre Usuario");
		GridBagConstraints gbc_lblNombreUsuario = new GridBagConstraints();
		gbc_lblNombreUsuario.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreUsuario.gridx = 0;
		gbc_lblNombreUsuario.gridy = 2;
		panelContenido.add(lblNombreUsuario, gbc_lblNombreUsuario);

		nombreUsuarioTxt = new JTextField();
		GridBagConstraints gbc_nombreUsuarioTxt = new GridBagConstraints();
		gbc_nombreUsuarioTxt.gridwidth = 4;
		gbc_nombreUsuarioTxt.insets = new Insets(0, 0, 5, 5);
		gbc_nombreUsuarioTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_nombreUsuarioTxt.gridx = 1;
		gbc_nombreUsuarioTxt.gridy = 2;
		panelContenido.add(nombreUsuarioTxt, gbc_nombreUsuarioTxt);
		nombreUsuarioTxt.setColumns(10);

		JLabel lblClaveActual = new JLabel("Clave Actual");
		GridBagConstraints gbc_lblClaveActual = new GridBagConstraints();
		gbc_lblClaveActual.anchor = GridBagConstraints.EAST;
		gbc_lblClaveActual.insets = new Insets(0, 0, 5, 5);
		gbc_lblClaveActual.gridx = 0;
		gbc_lblClaveActual.gridy = 3;
		panelContenido.add(lblClaveActual, gbc_lblClaveActual);

		claveActualPwsd = new JPasswordField();
		GridBagConstraints gbc_claveActualPwsd = new GridBagConstraints();
		gbc_claveActualPwsd.gridwidth = 4;
		gbc_claveActualPwsd.insets = new Insets(0, 0, 5, 5);
		gbc_claveActualPwsd.fill = GridBagConstraints.HORIZONTAL;
		gbc_claveActualPwsd.gridx = 1;
		gbc_claveActualPwsd.gridy = 3;
		panelContenido.add(claveActualPwsd, gbc_claveActualPwsd);

		JLabel lblNuevaClave = new JLabel("Nueva Clave");
		GridBagConstraints gbc_lblNuevaClave = new GridBagConstraints();
		gbc_lblNuevaClave.anchor = GridBagConstraints.EAST;
		gbc_lblNuevaClave.insets = new Insets(0, 0, 5, 5);
		gbc_lblNuevaClave.gridx = 0;
		gbc_lblNuevaClave.gridy = 4;
		panelContenido.add(lblNuevaClave, gbc_lblNuevaClave);

		nuevaClavePwsd = new JPasswordField();
		GridBagConstraints gbc_nuevaClavePwsd = new GridBagConstraints();
		gbc_nuevaClavePwsd.gridwidth = 4;
		gbc_nuevaClavePwsd.insets = new Insets(0, 0, 5, 5);
		gbc_nuevaClavePwsd.fill = GridBagConstraints.HORIZONTAL;
		gbc_nuevaClavePwsd.gridx = 1;
		gbc_nuevaClavePwsd.gridy = 4;
		panelContenido.add(nuevaClavePwsd, gbc_nuevaClavePwsd);

		JLabel lblConfirmarClave = new JLabel("Confirmar Clave");
		GridBagConstraints gbc_lblConfirmarClave = new GridBagConstraints();
		gbc_lblConfirmarClave.anchor = GridBagConstraints.EAST;
		gbc_lblConfirmarClave.insets = new Insets(0, 0, 5, 5);
		gbc_lblConfirmarClave.gridx = 0;
		gbc_lblConfirmarClave.gridy = 5;
		panelContenido.add(lblConfirmarClave, gbc_lblConfirmarClave);

		confirmarClavePwsd = new JPasswordField();
		GridBagConstraints gbc_confirmarClavePwsd = new GridBagConstraints();
		gbc_confirmarClavePwsd.gridwidth = 4;
		gbc_confirmarClavePwsd.insets = new Insets(0, 0, 5, 5);
		gbc_confirmarClavePwsd.fill = GridBagConstraints.HORIZONTAL;
		gbc_confirmarClavePwsd.gridx = 1;
		gbc_confirmarClavePwsd.gridy = 5;
		panelContenido.add(confirmarClavePwsd, gbc_confirmarClavePwsd);

		JLabel lblIpServidor = new JLabel("IP Servidor");
		GridBagConstraints gbc_lblIpServidor = new GridBagConstraints();
		gbc_lblIpServidor.anchor = GridBagConstraints.EAST;
		gbc_lblIpServidor.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpServidor.gridx = 0;
		gbc_lblIpServidor.gridy = 6;
		panelContenido.add(lblIpServidor, gbc_lblIpServidor);

		servidorTxt = new JTextField("127.0.0.1");
		GridBagConstraints gbc_servidorTxt = new GridBagConstraints();
		gbc_servidorTxt.gridwidth = 4;
		gbc_servidorTxt.insets = new Insets(0, 0, 5, 5);
		gbc_servidorTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_servidorTxt.gridx = 1;
		gbc_servidorTxt.gridy = 6;
		panelContenido.add(servidorTxt, gbc_servidorTxt);
		servidorTxt.setColumns(10);
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 0, 5, 5);
		gbc_btnOk.gridx = 3;
		gbc_btnOk.gridy = 7;
		panelContenido.add(btnOk, gbc_btnOk);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.insets = new Insets(0, 0, 5, 5);
		gbc_btnCancelar.gridx = 4;
		gbc_btnCancelar.gridy = 7;
		panelContenido.add(btnCancelar, gbc_btnCancelar);
	}

	private void cambiarClave() {
		try {
			String nombreUsuario = nombreUsuarioTxt.getText();
			String claveActual = new String(claveActualPwsd.getPassword());
			String claveNueva = new String(nuevaClavePwsd.getPassword());
			String confirmarClave = new String(confirmarClavePwsd.getPassword());
			String servidor = servidorTxt.getText();

			// obtener ID Usuario
			if (nombreUsuario.isEmpty()) {
				JOptionPane.showMessageDialog(VentanaCambiarContraseña.this,
						"Debes escribir tu nombre de Usuario.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (servidor.isEmpty()) {
				JOptionPane.showMessageDialog(VentanaCambiarContraseña.this,
						"Debes escribir la dirección IP del servidor.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (claveActual.isEmpty()) {
				JOptionPane.showMessageDialog(VentanaCambiarContraseña.this,
						"Debes escribir tu clave actual.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!confirmarClave.equals(claveNueva)) {
				JOptionPane.showMessageDialog(VentanaCambiarContraseña.this,
						"Claves no coinciden", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String claveEncriptadaActual = Encriptacion.encriptar(claveActual);
			UsuarioCliente usuarioCliente = new UsuarioCliente(nombreUsuario,
					claveEncriptadaActual, servidor);
			Usuario usuarioActual = usuarioCliente.buscarUsuario();
			if (usuarioActual != null) {
				String claveEncriptadaNueva = Encriptacion
						.encriptar(claveNueva);
				usuarioActual.setClave(claveEncriptadaNueva);
				boolean resultado = usuarioCliente.actualizar(usuarioActual);
				if (resultado == true) {
					JOptionPane.showMessageDialog(
							VentanaCambiarContraseña.this,
							"Clave de Acceso cambiada exitosamente",
							"Actualización Exitosa",
							JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(
							VentanaCambiarContraseña.this,
							"No se pudo actualizar la clave de acceso",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				JOptionPane.showMessageDialog(VentanaCambiarContraseña.this,
						"No se encontró información del usuario", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
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
