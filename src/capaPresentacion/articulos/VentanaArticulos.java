package capaPresentacion.articulos;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utilidades.Logging;
import utilidades.RequestFailureException;
import capaAPICliente.ArticuloCliente;
import capaAPICliente.UbicacionCliente;
import capaNegocio.Articulo;
import capaNegocio.Ubicacion;
import capaNegocio.Usuario;
import capaPresentacion.VentanaOpciones;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class VentanaArticulos extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tomoTxt;
	private JTextField folioTxt;
	private JTextField asientoTxt;
	private JTextField descripcionTxt;
	private JTextField marcaTxt;
	private JTextField modeloTxt;
	private JTextField serieTxt;
	private JTextField condicionTxt;
	private JComboBox<String> ubicacionComboBox;
	private JTextField adquisicionTxt;
	private JTextField observacionTxt;

	private VentanaOpciones ventanaOpciones;
	private boolean modoActualizar = false;
	private Articulo articuloAnterior;
	private HashMap<String, Integer> ubicacionMap = new HashMap<String, Integer>();
	private Usuario usuarioActual = null;
	Logger logger = Logging.obtenerClientLogger();
	private String serverIP;

	public VentanaArticulos(VentanaOpciones ventanaOpciones,
			Usuario usuarioActual, Articulo articuloAnterior,
			boolean modoActualizar, String serverIP) {
		this.ventanaOpciones = ventanaOpciones;
		this.usuarioActual = usuarioActual;
		this.articuloAnterior = articuloAnterior;
		this.modoActualizar = modoActualizar;
		this.serverIP = serverIP;
		init();
		if (modoActualizar) {
			setTitle("Actualizar Artículo");
			crearGui(articuloAnterior);
		} else {
			setTitle("Agregar Artículo");
		}
	}

	public VentanaArticulos(VentanaOpciones ventanaOpciones,
			Usuario usuarioActual, String serverIP) {
		this(ventanaOpciones, usuarioActual, null, false, serverIP);
	}

	private void init() {

		setBounds(200, 200, 425, 409);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout((LayoutManager) new FormLayout(
				new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC, }, new RowSpec[] {
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
						FormFactory.RELATED_GAP_ROWSPEC, }));
		{
			JLabel lblRegistrado = new JLabel("REGISTRADO EN LIBRO FISICO");
			lblRegistrado.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblRegistrado, "4, 2, center, default");
		}
		{
			JLabel lblTomo = new JLabel("Tomo");
			lblTomo.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblTomo, "2, 4, center, default");
		}
		{
			tomoTxt = new JTextField();
			contentPanel.add(tomoTxt, "2, 6, fill, default");
			tomoTxt.setColumns(10);
		}
		{
			JLabel lblTomo = new JLabel("Folio");
			lblTomo.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblTomo, "4, 4, center, default");
		}
		{
			folioTxt = new JTextField();
			contentPanel.add(folioTxt, "4, 6, fill, default");
			folioTxt.setColumns(10);
		}
		{
			JLabel lblAsiento = new JLabel("Asiento");
			lblAsiento.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblAsiento, "6, 4, center, default");
		}
		{
			asientoTxt = new JTextField();
			contentPanel.add(asientoTxt, "6, 6, fill, default");
			asientoTxt.setColumns(10);
		}

		{
			JLabel lblDescripcion = new JLabel("Descripción");
			lblDescripcion.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblDescripcion, "2, 12, right, default");
		}
		{
			descripcionTxt = new JTextField();
			contentPanel.add(descripcionTxt, "4, 12, fill, default");
			descripcionTxt.setColumns(10);
		}
		{
			JLabel lblMarca = new JLabel("Marca");
			lblMarca.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblMarca, "2, 14, right, default");
		}
		{
			marcaTxt = new JTextField();
			contentPanel.add(marcaTxt, "4, 14, fill, default");
			marcaTxt.setColumns(10);
		}
		{
			JLabel lblModelo = new JLabel("Modelo");
			lblModelo.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblModelo, "2, 16, right, default");
		}
		{
			modeloTxt = new JTextField();
			contentPanel.add(modeloTxt, "4, 16, fill, default");
			modeloTxt.setColumns(10);
		}
		{
			JLabel lblSerie = new JLabel("Serie");
			lblSerie.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblSerie, "2, 18, right, default");
		}
		{
			serieTxt = new JTextField();
			contentPanel.add(serieTxt, "4, 18, fill, default");
			serieTxt.setColumns(10);
		}
		{
			JLabel lblEstado = new JLabel("Estado");
			lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblEstado, "2, 20, right, default");
		}
		{
			condicionTxt = new JTextField();
			contentPanel.add(condicionTxt, "4, 20, fill, default");
			condicionTxt.setColumns(10);
		}
		{
			JLabel lblUbicacion = new JLabel("Ubicación");
			lblUbicacion.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblUbicacion, "2, 22, right, default");
		}
		{
			ubicacionComboBox = new JComboBox<String>();
			ubicacionComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

				}
			});
			try {
				UbicacionCliente ubicacionCliente = new UbicacionCliente(
						usuarioActual.getNombreUsuario(),
						usuarioActual.getClave(), serverIP);
				List<Ubicacion> ubicaciones = ubicacionCliente.mostrarTodo();
				for (Ubicacion ubicacion : ubicaciones) {
					ubicacionComboBox.addItem(ubicacion.getDescripcion());
					ubicacionMap.put(ubicacion.getDescripcion(),
							ubicacion.getIdUbicacion());
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"Error al mostrar lista en el comboBox", e);
			}
			contentPanel.add(ubicacionComboBox, "4, 22");
		}
		{
			JLabel lblAdquisicion = new JLabel("Modo de adquisición");
			lblAdquisicion.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblAdquisicion, "2, 24, right, default");
		}
		{
			adquisicionTxt = new JTextField();
			contentPanel.add(adquisicionTxt, "4, 24, fill, default");
			adquisicionTxt.setColumns(10);
		}
		{
			JLabel lblObservacion = new JLabel("Observación");
			lblObservacion.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblObservacion, "2, 26, right, default");
		}
		{
			observacionTxt = new JTextField();
			contentPanel.add(observacionTxt, "4, 26, fill, default");
			observacionTxt.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						guardarArticulo();

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

	private void crearGui(Articulo articulo) {

		tomoTxt.setText(Integer.toString(articulo.getTomo()));
		folioTxt.setText(Integer.toString(articulo.getFolio()));
		asientoTxt.setText(Integer.toString(articulo.getAsiento()));
		descripcionTxt.setText(articulo.getDescripcion());
		marcaTxt.setText(articulo.getMarca());
		modeloTxt.setText(articulo.getModelo());
		serieTxt.setText(articulo.getSerie());
		condicionTxt.setText(articulo.getCondicion());
		ubicacionComboBox.setSelectedItem(ubicacionComboBox);
		adquisicionTxt.setText(articulo.getAdquisicion());
		observacionTxt.setText(articulo.getObservaciones());
	}

	private void guardarArticulo() {
		int tomo;
		int folio;
		int asiento;

		try {
			tomo = Integer.parseInt(tomoTxt.getText());
			folio = Integer.parseInt(folioTxt.getText());
			asiento = Integer.parseInt(asientoTxt.getText());

		} catch (NumberFormatException e) {
			logger.log(Level.SEVERE,
					"El tomo, folio y asiento deben ser números, "
							+ "intente de nuevo.", e);

			JOptionPane.showMessageDialog(VentanaArticulos.this,
					"El tomo, folio y asiento deben ser números, "
							+ "intente de nuevo."
							+ " El campo de Observación es opcional", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;

		}
		String descripcion = descripcionTxt.getText();
		String marca = marcaTxt.getText();
		String modelo = modeloTxt.getText();
		String serie = serieTxt.getText();
		String condicion = condicionTxt.getText();
		int idUbicacion = ubicacionMap.get(ubicacionComboBox.getSelectedItem()
				.toString());
		String adquisicion = adquisicionTxt.getText();
		String observacion = observacionTxt.getText();
		Articulo articulo = null;

		if (tomoTxt.getText().isEmpty() || folioTxt.getText().isEmpty()
				|| asientoTxt.getText().isEmpty()
				|| descripcionTxt.getText().isEmpty()
				|| marcaTxt.getText().isEmpty()
				|| modeloTxt.getText().isEmpty()
				|| serieTxt.getText().isEmpty()
				|| condicionTxt.getText().isEmpty()
				|| ubicacionComboBox.getSelectedItem().toString().isEmpty()
				|| adquisicionTxt.getText().isEmpty()) {
			JOptionPane.showMessageDialog(VentanaArticulos.this,
					"Debes llenar toda la información, intenta de nuevo."
							+ " El campo de Observación es opcional", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;

		} else {
			TablaArticulosModelo tablaArticulos = ventanaOpciones
					.getTablaArticulosModelo();
			for (Articulo articuloExistente : tablaArticulos.getArticulos()) {
				if (articuloExistente.getTomo() == tomo
						&& articuloExistente.getFolio() == folio
						&& articuloExistente.getAsiento() == asiento
						&& (!modoActualizar || articuloExistente
								.getIdArticulo() != articuloAnterior
								.getIdArticulo())) {
					JOptionPane.showMessageDialog(ventanaOpciones,
							"Artículo ya existe", "Creación Fallida",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			if (modoActualizar) {
				articulo = articuloAnterior;
				articulo.setTomo(tomo);
				articulo.setFolio(folio);
				articulo.setAsiento(asiento);
				articulo.setDescripcion(descripcion);
				articulo.setMarca(marca);
				articulo.setModelo(modelo);
				articulo.setSerie(serie);
				articulo.setCondicion(condicion);
				articulo.setIdUbicacion(idUbicacion);
				articulo.setAdquisicion(adquisicion);
				articulo.setObservaciones(observacion);
			} else {
				articulo = new Articulo(tomo, folio, asiento, descripcion,
						marca, modelo, serie, condicion, idUbicacion,
						adquisicion, observacion);
			}
		}
		try {
			// guardar en la BD
			ArticuloCliente articuloCliente = new ArticuloCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					serverIP);

			if (modoActualizar) {

				// Revisar si se necesita actualizar los campos del GUI para el
				// articulo actual.
				if (ventanaOpciones.obtenerIdArticuloActual() == articulo
						.getIdArticulo()) {
					logger.log(Level.INFO, "Artículo ya existe");
				} else {
					articuloCliente.actualizar(articulo);
				}
			} else {
				articuloCliente.agregar(articulo);
			}

			// cerrar Ventana
			setVisible(false);

			// refrescar la lista del GUI
			ventanaOpciones.refrescarVistaArticulos(true);
			// mostrar mensaje de exitoso
			JOptionPane.showMessageDialog(ventanaOpciones,
					"Artículo guardado exitosamente.", "Artículo guardado",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error guardando artículo", exc);
			JOptionPane.showMessageDialog(ventanaOpciones,
					"Error al guardar artículo: " + exc.getMessage(), "Error: "
							+ exc.getHttpCode(), JOptionPane.ERROR_MESSAGE);
		}

	}
}
