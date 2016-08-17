package capaPresentacion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import capaNegocio.Usuario;

@SuppressWarnings("serial")
public class VentanaReporte extends JFrame {

	private JPanel panelTitulo;
	private JPanel panelContenido;
	private JPanel panelBotones;
	private JPanel usuarioTotalPanel;
	private JPanel printPanel;
	private JButton btnImprimir;
	private JButton btnSalir;
	private JLabel lblEscuela;
	private JLabel lblReporteTitulo;
	private JLabel lblFecha;
	private JLabel lblNombreUsuario;
	private JLabel lblTotal;
	private JScrollPane tablaScrollPane;
	private JTable tablaReporte;
	Usuario usuarioActual = null;

	public static String getFechaActual() {
		Date ahora = new Date();
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
		return formateador.format(ahora);
	}

	public VentanaReporte(Usuario usuario, JTable parentTable, String titulo) {
		usuarioActual = usuario;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 500);
		panelContenido = new JPanel();
		panelContenido.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelContenido);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 622, 0 };
		gbl_contentPane.rowHeights = new int[] { 52, 0, 0, 280, 0, 0, 0, 33, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0,
				0.0, 1.0, 0.0, Double.MIN_VALUE };
		panelContenido.setLayout(gbl_contentPane);

		printPanel = new JPanel();
		GridBagConstraints gbc_printPanel = new GridBagConstraints();
		gbc_printPanel.gridheight = 7;
		gbc_printPanel.fill = GridBagConstraints.BOTH;
		gbc_printPanel.insets = new Insets(0, 0, 5, 0);
		gbc_printPanel.gridx = 0;
		gbc_printPanel.gridy = 0;
		panelContenido.add(printPanel, gbc_printPanel);
		printPanel.setLayout(new GridLayout(0, 1, 0, 0));

		panelTitulo = new JPanel();
		printPanel.add(panelTitulo);
		GridBagLayout gbl_panelTitulo = new GridBagLayout();
		gbl_panelTitulo.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0 };
		gbl_panelTitulo.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panelTitulo.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelTitulo.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panelTitulo.setLayout(gbl_panelTitulo);

		lblEscuela = new JLabel("Escuela Villa Nueva");
		lblEscuela.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblEscuela = new GridBagConstraints();
		gbc_lblEscuela.insets = new Insets(0, 0, 5, 0);
		gbc_lblEscuela.gridx = 10;
		gbc_lblEscuela.gridy = 0;
		panelTitulo.add(lblEscuela, gbc_lblEscuela);

		lblReporteTitulo = new JLabel("Reporte de " + titulo);
		lblReporteTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblReporteTitulo = new GridBagConstraints();
		gbc_lblReporteTitulo.insets = new Insets(0, 0, 5, 0);
		gbc_lblReporteTitulo.gridx = 10;
		gbc_lblReporteTitulo.gridy = 1;
		panelTitulo.add(lblReporteTitulo, gbc_lblReporteTitulo);

		lblFecha = new JLabel(getFechaActual());
		GridBagConstraints gbc_lblFecha = new GridBagConstraints();
		gbc_lblFecha.insets = new Insets(0, 0, 5, 0);
		gbc_lblFecha.gridx = 10;
		gbc_lblFecha.gridy = 2;
		panelTitulo.add(lblFecha, gbc_lblFecha);

		tablaScrollPane = new JScrollPane();
		printPanel.add(tablaScrollPane);

		tablaReporte = new AjusteColumnasTabla() {
			@Override
			public Printable getPrintable(PrintMode printMode,
					MessageFormat headerFormat, MessageFormat footerFormat) {
				return new TablaImprimible(this, printMode, headerFormat,
						footerFormat);
			}
		};

		tablaReporte.setBorder(null);
		tablaScrollPane.setViewportView(tablaReporte);
		tablaReporte.setModel(parentTable.getModel());

		usuarioTotalPanel = new JPanel();
		printPanel.add(usuarioTotalPanel);
		GridBagLayout gbl_usuarioTotalPanel = new GridBagLayout();
		gbl_usuarioTotalPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_usuarioTotalPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0,
				0 };
		gbl_usuarioTotalPanel.columnWeights = new double[] { 0.0, 1.0, 0.0,
				0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_usuarioTotalPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		usuarioTotalPanel.setLayout(gbl_usuarioTotalPanel);

		lblNombreUsuario = new JLabel("Usuario: " + usuarioActual.getNombre()
				+ " " + usuarioActual.getApellido1() + " "
				+ usuarioActual.getApellido2());
		GridBagConstraints gbc_lblNombreUsuario = new GridBagConstraints();
		gbc_lblNombreUsuario.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNombreUsuario.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreUsuario.gridx = 1;
		gbc_lblNombreUsuario.gridy = 5;
		usuarioTotalPanel.add(lblNombreUsuario, gbc_lblNombreUsuario);
		lblNombreUsuario.setHorizontalAlignment(SwingConstants.LEFT);

		lblTotal = new JLabel("Total de " + titulo + " "
				+ parentTable.getModel().getRowCount());
		lblTotal.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTotal = new GridBagConstraints();
		gbc_lblTotal.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotal.gridx = 1;
		gbc_lblTotal.gridy = 6;
		usuarioTotalPanel.add(lblTotal, gbc_lblTotal);

		panelBotones = new JPanel();
		GridBagConstraints gbc_btnPanelReporte = new GridBagConstraints();
		gbc_btnPanelReporte.anchor = GridBagConstraints.NORTH;
		gbc_btnPanelReporte.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPanelReporte.gridx = 0;
		gbc_btnPanelReporte.gridy = 7;
		panelContenido.add(panelBotones, gbc_btnPanelReporte);

		btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imprimirTabla();
			}
		});
		btnImprimir.setHorizontalAlignment(SwingConstants.RIGHT);
		panelBotones.add(btnImprimir);

		btnSalir = new JButton("Salir");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnSalir.setHorizontalAlignment(SwingConstants.LEADING);
		panelBotones.add(btnSalir);
	}

	private void imprimirTabla() {

		MessageFormat encabezado = new MessageFormat(lblEscuela.getText()
				+ "\n " + lblReporteTitulo.getText() + "\n"
				+ lblFecha.getText());

		MessageFormat piePagina = new MessageFormat(lblNombreUsuario.getText()
				+ "\n" + lblTotal.getText() + "\n" + "Página {0}");

		boolean mostrarVentanaImprimir = true;
		boolean interactivo = true;

		JTable.PrintMode modo = JTable.PrintMode.FIT_WIDTH;

		try {
			boolean completo = tablaReporte.print(modo, encabezado, piePagina,
					mostrarVentanaImprimir, null, interactivo, null);

			if (completo) {

				JOptionPane.showMessageDialog(this, "Impresión Completada",
						"Resultado Impresión", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Impresión Cancelada",
						"Resultado Impresión", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe) {
			JOptionPane.showMessageDialog(this,
					"Impresión Fallida: " + pe.getMessage(),
					"Resultado Impresión", JOptionPane.ERROR_MESSAGE);
		}
	}

}
