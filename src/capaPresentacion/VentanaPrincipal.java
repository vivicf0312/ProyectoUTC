package capaPresentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import utilidades.Logging;
import capaPresentacion.usuarios.VentanaCambiarContraseña;
import capaPresentacion.usuarios.VentanaContraseña;

@SuppressWarnings("serial")
public class VentanaPrincipal extends JFrame {
	Logger logger = Logging.obtenerClientLogger();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
					ventanaPrincipal
							.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					ventanaPrincipal.setVisible(true);

				} catch (Exception e) {
					Logger logger = Logging.obtenerClientLogger();
					logger.log(Level.SEVERE,
							"Error ingresando a Ventana Principal", e);
				}
			}
		});
	}

	public VentanaPrincipal() {

		setBounds(100, 100, 664, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(SystemColor.inactiveCaption);
		getContentPane().setSize(new Dimension(400, 400));

		JLabel lblImage = new JLabel("");
		lblImage.setBackground(SystemColor.inactiveCaption);
		lblImage.setIconTextGap(0);
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setIcon(new ImageIcon(VentanaPrincipal.class
				.getResource("/imagenes/logo.png")));
		getContentPane().add(lblImage, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		getContentPane().add(panel, BorderLayout.SOUTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel.setLayout(gbl_panel);

		JLabel lblVillaNueva = new JLabel("Villa Nueva");
		lblVillaNueva.setFont(new Font("Calibri", Font.PLAIN, 15));
		GridBagConstraints gbc_lblVillaNueva = new GridBagConstraints();
		gbc_lblVillaNueva.insets = new Insets(0, 0, 5, 0);
		gbc_lblVillaNueva.gridx = 0;
		gbc_lblVillaNueva.gridy = 1;
		panel.add(lblVillaNueva, gbc_lblVillaNueva);

		JLabel lblInstitucin = new JLabel("INSTITUCI\u00D3N");
		lblInstitucin.setFont(new Font("Calibri", Font.BOLD, 15));
		GridBagConstraints gbc_lblInstitucin = new GridBagConstraints();
		gbc_lblInstitucin.insets = new Insets(0, 0, 5, 0);
		gbc_lblInstitucin.gridx = 0;
		gbc_lblInstitucin.gridy = 2;
		panel.add(lblInstitucin, gbc_lblInstitucin);

		JLabel lblNewLabel = new JLabel("68-3781");
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 3;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblCdigo = new JLabel("C\u00D3DIGO");
		lblCdigo.setFont(new Font("Calibri", Font.BOLD, 15));
		GridBagConstraints gbc_lblCdigo = new GridBagConstraints();
		gbc_lblCdigo.insets = new Insets(0, 0, 5, 0);
		gbc_lblCdigo.gridx = 0;
		gbc_lblCdigo.gridy = 4;
		panel.add(lblCdigo, gbc_lblCdigo);

		JLabel lblMscRafaelngel = new JLabel(
				"MSC. Rafael \u00C1ngel Fonseca Le\u00F3n");
		lblMscRafaelngel.setFont(new Font("Calibri", Font.PLAIN, 15));
		GridBagConstraints gbc_lblMscRafaelngel = new GridBagConstraints();
		gbc_lblMscRafaelngel.insets = new Insets(0, 0, 5, 0);
		gbc_lblMscRafaelngel.gridx = 0;
		gbc_lblMscRafaelngel.gridy = 5;
		panel.add(lblMscRafaelngel, gbc_lblMscRafaelngel);

		JLabel lblNewLabel_1 = new JLabel("DIRECTOR(A)");
		lblNewLabel_1.setFont(new Font("Calibri", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 6;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(102, 153, 204));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 7;
		panel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0 };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		panel_1.setLayout(gbl_panel_1);

		JLabel lblNewLabel_2 = new JLabel("Rafael \u00C1ngel Fonseca Le\u00F3n");
		lblNewLabel_2.setFont(new Font("Calibri", Font.PLAIN, 15));
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 0;
		panel_1.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("ENCARGADO DE LLENADO");
		lblNewLabel_3.setFont(new Font("Calibri", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		panel_1.add(lblNewLabel_3, gbc_lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Fernando Z\u00FA\u00F1iga Azofeifa");
		lblNewLabel_4.setFont(new Font("Calibri", Font.PLAIN, 15));
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 2;
		panel_1.add(lblNewLabel_4, gbc_lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel(
				"VICEPRESIDENTE JUNTA DE EDUCACI\u00D3N");
		lblNewLabel_5.setFont(new Font("Calibri", Font.BOLD, 15));
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 3;
		panel_1.add(lblNewLabel_5, gbc_lblNewLabel_5);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnInicio = new JMenu("Inicio");
		menuBar.add(mnInicio);

		JMenuItem mntmIniciarSesion = new JMenuItem("Iniciar Sesi\u00F3n");
		mntmIniciarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaContraseña ventanaContraseña = new VentanaContraseña();
				ventanaContraseña
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaContraseña.setVisible(true);
			}
		});
		mnInicio.add(mntmIniciarSesion);

		JMenuItem mntmCambiarClaveDe = new JMenuItem("Cambiar Clave de Acceso");
		mntmCambiarClaveDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaCambiarContraseña ventanaCambiarContraseña = new VentanaCambiarContraseña();
				ventanaCambiarContraseña
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaCambiarContraseña.setVisible(true);
			}
		});
		mnInicio.add(mntmCambiarClaveDe);

		JMenuItem mntmSalir_1 = new JMenuItem("Salir");
		mntmSalir_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				System.exit(0);
			}
		});
		mnInicio.add(mntmSalir_1);

		JMenu mnAcercaDeLa = new JMenu("Acerca de la Aplicaci\u00F3n");
		mnAcercaDeLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		menuBar.add(mnAcercaDeLa);

		JMenuItem mntmVer = new JMenuItem("Ver");
		mntmVer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaAcercaDe ventanaAcercaDe = new VentanaAcercaDe();
				ventanaAcercaDe
						.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				ventanaAcercaDe.setVisible(true);
			}
		});
		mnAcercaDeLa.add(mntmVer);

		JMenu mnAyuda = new JMenu("Ayuda");
		menuBar.add(mnAyuda);

		JMenuItem mntmMostrarPdf = new JMenuItem("Mostrar PDF");
		mntmMostrarPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						File myFile = new File(
								"Manual de Usuario para el Sistema de Inventario de Bienes.pdf");
						myFile.deleteOnExit();
						if (!myFile.exists()) {
							// In JAR
							InputStream inputStream = getClass()
									.getResourceAsStream(
											"Manual de Usuario.pdf");
							// Copy file
							OutputStream outputStream = new FileOutputStream(
									myFile);
							byte[] buffer = new byte[1024];
							int length;
							while ((length = inputStream.read(buffer)) > 0) {
								outputStream.write(buffer, 0, length);
							}
							outputStream.close();
							inputStream.close();
						}
						Desktop.getDesktop().open(myFile);
					} catch (IOException ex) {
						JOptionPane
								.showMessageDialog(
										null,
										"No se puede abrir el archivo de ayuda, probablemente fue borrado",
										"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mnAyuda.add(mntmMostrarPdf);
	}
}
