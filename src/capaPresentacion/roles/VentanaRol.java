package capaPresentacion.roles;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utilidades.Enumeracion.Operaciones;
import utilidades.Logging;
import utilidades.RequestFailureException;
import capaAPICliente.RolCliente;
import capaNegocio.Rol;
import capaNegocio.Usuario;
import capaPresentacion.VentanaOpciones;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("serial")
public class VentanaRol extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField descripcionTxt;

	private VentanaOpciones ventanaOpciones;
	private boolean modoActualizar = false;
	private Rol rolAnterior;
	private Usuario usuarioActual = null;
	Logger logger = Logging.obtenerClientLogger();
	JList<Operaciones> listaOperaciones = new JList<>();
	private String serverIP;

	public VentanaRol(VentanaOpciones ventanaOpciones, Usuario usuarioActual,
			Rol rolAnterior, boolean modoActualizar, String serverIP) {
		setResizable(false);
		this.ventanaOpciones = ventanaOpciones;
		this.usuarioActual = usuarioActual;
		this.rolAnterior = rolAnterior;
		this.modoActualizar = modoActualizar;
		this.serverIP = serverIP;
		init();

		if (modoActualizar) {
			setTitle("Actualizar Rol");
			crearGui(rolAnterior);
		} else {
			setTitle("Agregar Rol");
		}
	}

	public VentanaRol(VentanaOpciones ventanaOpciones, Usuario usuarioActual,
			String serverIP) {
		this(ventanaOpciones, usuarioActual, null, false, serverIP);
	}

	private void init() {

		setBounds(100, 100, 378, 262);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout((LayoutManager) new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		{
			JLabel lblNewLabel = new JLabel("Puesto");
			lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel, "2, 2, right, default");
		}
		{
			descripcionTxt = new JTextField();
			contentPanel.add(descripcionTxt, "4, 2, fill, default");
			descripcionTxt.setColumns(10);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Operaciones permitidas");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblNewLabel_1, "2, 4, center, default");
		}

		{
			ScrollPane scrollPane = new ScrollPane();
			contentPanel.add(scrollPane, "4, 4, fill, fill");
			listaOperaciones = new JList<Operaciones>(Operaciones.values());
			listaOperaciones.setValueIsAdjusting(true);
			listaOperaciones.setLayoutOrientation(JList.VERTICAL);
			listaOperaciones
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			scrollPane.add(listaOperaciones, "4, 4, fill, fill");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						guardarRol();
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

	private void crearGui(Rol rol) {
		descripcionTxt.setText(rol.getDescripcion());
	}

	private void guardarRol() {
		String descripcion = descripcionTxt.getText();
		Rol rol = null;

		if (descripcionTxt.getText().isEmpty()) {
			JOptionPane.showMessageDialog(VentanaRol.this,
					"Debes escribir el nombre del puesto", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else if (listaOperaciones.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(VentanaRol.this,
					"Debes seleccionar una o varias operaciones permitidas "
							+ "para el usuario. Intenta de nuevo", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			TablaRolesModelo tablaRoles = ventanaOpciones.getTablaRolesModelo();
			for (Rol rolExistente : tablaRoles.getRoles()) {
				if (rolExistente.getDescripcion().equals(descripcion)
						&& (!modoActualizar || rolExistente.getIdRol() != rolAnterior
								.getIdRol())) {
					JOptionPane.showMessageDialog(ventanaOpciones,
							"Rol ya existe", "Creación Fallida",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			HashSet<Operaciones> operacionesSeleccionadas = new HashSet<Operaciones>(
					listaOperaciones.getSelectedValuesList());
			if (modoActualizar) {
				rol = rolAnterior;
				rol.setDescripcion(descripcion);
				rol.setOperaciones(operacionesSeleccionadas);
			} else {
				rol = new Rol(descripcion, operacionesSeleccionadas);
			}
		}

		try {
			RolCliente rolCliente = new RolCliente(
					usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
					serverIP);
			// guardar en la BD
			if (modoActualizar) {
				// revisar si es necesario actualizar los campos del GUI para el
				// rol actual
				if (ventanaOpciones.obtenerIdRolActual() == rol.getIdRol()) {
					logger.log(Level.INFO, "Rol ya existe");
				} else {
					rolCliente.actualizar(rol);
				}
			} else {
				rolCliente.agregar(rol);
			}

			// cerrar ventana
			setVisible(false);

			// refrescar lista del GUI
			ventanaOpciones.refrescarVistaRoles(true);

			// mostrar mensaje exitoso
			JOptionPane.showMessageDialog(ventanaOpciones,
					"Rol guardado exitosamente.", "Rol guardado",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (RequestFailureException exc) {
			logger.log(Level.SEVERE, "Error guardando rol", exc);
			JOptionPane.showMessageDialog(ventanaOpciones,
					"Error al guardar rol: " + exc.getMessage(), "Error: "
							+ exc.getHttpCode(), JOptionPane.ERROR_MESSAGE);
		}

	}
}
