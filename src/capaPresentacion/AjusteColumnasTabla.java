package capaPresentacion;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class AjusteColumnasTabla extends JTable {

	private static final long serialVersionUID = 1L;

	public AjusteColumnasTabla() {
		super();
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Component component = super.prepareRenderer(renderer, row, column);
		int rendererWidth = component.getPreferredSize().width;
		TableColumn tableColumn = getColumnModel().getColumn(column);
		tableColumn
				.setPreferredWidth(Math.max(rendererWidth
						+ getIntercellSpacing().width,
						tableColumn.getPreferredWidth()));
		return component;
	}
}
