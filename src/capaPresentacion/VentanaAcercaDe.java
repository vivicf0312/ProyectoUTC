package capaPresentacion;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class VentanaAcercaDe extends JDialog {

	public VentanaAcercaDe() {
		getContentPane().setBackground(new Color(176, 196, 222));
		getContentPane().setForeground(new Color(0, 0, 0));
		setTitle("Acerca de la Aplicaci\u00F3n");
		setBounds(100, 100, 507, 455);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 434, 0 };
		gridBagLayout.rowHeights = new int[] { 228, 33, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		{
			JTextPane txtpnNombreDeLa = new JTextPane();
			txtpnNombreDeLa.setFont(new Font("Traditional Arabic", Font.BOLD,
					14));
			txtpnNombreDeLa.setBackground(new Color(176, 196, 222));
			txtpnNombreDeLa.setEditable(false);
			txtpnNombreDeLa
					.setText("Nombre de la Aplicaci\u00F3n: Sistema de Inventario de Bienes Institucionales.\r\n\r\n\r\nEste Software ha sido desarrollado gratuitamente ya que es un requisito de Graduaci\u00F3n para la Carrera de Ingenier\u00EDa en Sistemas Computacionales. \r\n\r\n\r\n\r\nCreado por: Viviana Camacho Fonseca\r\n\r\n\r\n\r\nVersi\u00F3n: 1.0        \r\n                                                                                                                                                                 A\u00F1o: 2016\r\n   ");
			GridBagConstraints gbc_txtpnNombreDeLa = new GridBagConstraints();
			gbc_txtpnNombreDeLa.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtpnNombreDeLa.insets = new Insets(0, 0, 5, 0);
			gbc_txtpnNombreDeLa.gridx = 0;
			gbc_txtpnNombreDeLa.gridy = 0;
			getContentPane().add(txtpnNombreDeLa, gbc_txtpnNombreDeLa);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(176, 196, 222));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 1;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				JButton okButton = new JButton("Cerrar Ventana");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
