package capaPresentacion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

class TablaImprimible implements Printable {

	private final JTable tabla;
	private final JTableHeader encabezado;
	private final TableColumnModel colModel;
	private final int totalAnchoCol;
	private final JTable.PrintMode modoImprimir;
	private final MessageFormat formatoEncabezado;
	private final MessageFormat formatoPiePagina;
	private int ultima = -1;
	private int fila = 0;
	private int col = 0;
	private final Rectangle clip = new Rectangle(0, 0, 0, 0);
	private final Rectangle hclip = new Rectangle(0, 0, 0, 0);
	private final Rectangle tempRect = new Rectangle(0, 0, 0, 0);
	private static final int H_F_SPACE = 8;
	private static final float HEADER_FONT_SIZE = 18.0f;
	private static final float FOOTER_FONT_SIZE = 12.0f;
	private final Font fondoEncabezado;
	private final Font fondoPiePagina;

	public TablaImprimible(JTable tabla, JTable.PrintMode modoImprimir,
			MessageFormat formatoEncabezado, MessageFormat formatoPiePagina) {

		this.tabla = tabla;

		encabezado = tabla.getTableHeader();
		colModel = tabla.getColumnModel();
		totalAnchoCol = colModel.getTotalColumnWidth();

		if (encabezado != null) {
			// la altura del encabezado se puede establecer una vez ya que no
			// cambia
			hclip.height = encabezado.getHeight();
		}

		this.modoImprimir = modoImprimir;

		this.formatoEncabezado = formatoEncabezado;
		this.formatoPiePagina = formatoPiePagina;

		// obtener la fuente de encabezado y pie de pagina de la fuente de la
		// tabla
		fondoEncabezado = tabla.getFont().deriveFont(Font.BOLD,
				HEADER_FONT_SIZE);
		fondoPiePagina = tabla.getFont().deriveFont(Font.PLAIN,
				FOOTER_FONT_SIZE);
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {

		final int imgWidth = (int) pageFormat.getImageableWidth();
		final int imgHeight = (int) pageFormat.getImageableHeight();

		if (imgWidth <= 0) {
			throw new PrinterException("Width of printable area is too small.");
		}

		// pasar numero de pagina cuando se formatee el texto de encabezado y
		// pie de pagina

		Object[] pageNumber = new Object[] { Integer.valueOf(pageIndex + 1) };

		// recuperar formato de texto de encabezado, si hay
		String headerText = null;
		if (formatoEncabezado != null) {
			headerText = formatoEncabezado.format(pageNumber);
		}

		// recuperar formato de texto de pie de pagina, si hay
		String footerText = null;
		if (formatoPiePagina != null) {
			footerText = formatoPiePagina.format(pageNumber);
		}

		// almacenar los limites del texto de encabezado y pie de pagina
		Rectangle2D hRect = null;
		Rectangle2D fRect = null;

		// cantidad de espacio vertical necesario para el texto de encabezado y
		// pie de pagina
		int headerTextSpace = 0;
		int footerTextSpace = 0;

		// cantidad de espacio vertical disponible para imprimir la tabla
		int availableSpace = imgHeight;

		// si hay una texto de encabezado buscar cuando espacio es necesario
		// para el
		// y restarlo del espacio disponible
		if (headerText != null) {
			graphics.setFont(fondoEncabezado);
			int nbLines = headerText.split("\n").length;
			hRect = graphics.getFontMetrics().getStringBounds(headerText,
					graphics);

			hRect = new Rectangle2D.Double(hRect.getX(),
					Math.abs(hRect.getY()), hRect.getWidth(), hRect.getHeight()
							* nbLines);

			headerTextSpace = (int) Math.ceil(hRect.getHeight() * nbLines);
			availableSpace -= headerTextSpace + H_F_SPACE;
		}

		// si hay una texto de pie de pagina buscar cuando espacio es necesario
		// para el
		// y restarlo del espacio disponible
		if (footerText != null) {
			graphics.setFont(fondoPiePagina);
			fRect = graphics.getFontMetrics().getStringBounds(footerText,
					graphics);

			fRect = new Rectangle2D.Double(hRect.getX(),
					Math.abs(hRect.getY()), hRect.getWidth(), hRect.getHeight());

			footerTextSpace = (int) Math.ceil(fRect.getHeight());
			availableSpace -= footerTextSpace + H_F_SPACE;
		}

		if (availableSpace <= 0) {
			throw new PrinterException(
					"Altura del area a imprimir es muy pequeña.");
		}

		// dependiento del modo de impresion, se podria necesitar un factor de
		// escala que
		// encaje el ancho total de la tabla en la pagina
		double sf = 1.0D;
		if (modoImprimir == JTable.PrintMode.FIT_WIDTH
				&& totalAnchoCol > imgWidth) {
			assert imgWidth > 0;

			assert totalAnchoCol > 1;

			sf = (double) imgWidth / (double) totalAnchoCol;
		}

		assert sf > 0;

		while (ultima < pageIndex) {

			if (fila >= tabla.getRowCount() && col == 0) {
				return NO_SUCH_PAGE;
			}

			int scaledWidth = (int) (imgWidth / sf);
			int scaledHeight = (int) ((availableSpace - hclip.height) / sf);

			encontrarSiguienteClip(scaledWidth, scaledHeight);

			ultima++;
		}

		Graphics2D g2d = (Graphics2D) graphics.create();

		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

		AffineTransform oldTrans;

		if (footerText != null) {
			oldTrans = g2d.getTransform();

			g2d.translate(0, imgHeight - footerTextSpace);

			String[] lines = footerText.split("\n");
			imprimirTexto(g2d, lines, fRect, fondoPiePagina, imgWidth);

			g2d.setTransform(oldTrans);
		}

		if (headerText != null) {
			String[] lines = headerText.split("\n");
			imprimirTexto(g2d, lines, hRect, fondoEncabezado, imgWidth);

			g2d.translate(0, headerTextSpace + H_F_SPACE);
		}

		tempRect.x = 0;
		tempRect.y = 0;
		tempRect.width = imgWidth;
		tempRect.height = availableSpace;
		g2d.clip(tempRect);

		if (sf != 1.0D) {
			g2d.scale(sf, sf);

		} else {
			int diff = (imgWidth - clip.width) / 2;
			g2d.translate(diff, 0);
		}

		oldTrans = g2d.getTransform();
		Shape oldClip = g2d.getClip();

		if (encabezado != null) {
			hclip.x = clip.x;
			hclip.width = clip.width;

			g2d.translate(-hclip.x, 0);
			g2d.clip(hclip);
			encabezado.print(g2d);

			g2d.setTransform(oldTrans);
			g2d.setClip(oldClip);

			g2d.translate(0, hclip.height);
		}
		g2d.translate(-clip.x, -clip.y);
		g2d.clip(clip);
		tabla.print(g2d);

		g2d.setTransform(oldTrans);
		g2d.setClip(oldClip);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, clip.width, hclip.height + clip.height);

		g2d.dispose();

		return PAGE_EXISTS;
	}

	private void imprimirTexto(Graphics2D g2d, String[] lines,
			Rectangle2D rect, Font font, int imgWidth) {

		g2d.setColor(Color.BLACK);
		g2d.setFont(font);

		for (int i = 0; i < lines.length; i++) {
			int tx;

			if (rect.getWidth() < imgWidth) {
				tx = (int) (imgWidth / 2 - g2d.getFontMetrics()
						.getStringBounds(lines[i], g2d).getWidth() / 2);

			} else if (tabla.getComponentOrientation().isLeftToRight()) {
				tx = 0;

			} else {
				tx = -(int) (Math.ceil(rect.getWidth()) - imgWidth);
			}

			int ty = (int) Math.ceil(Math.abs(rect.getY() + i
					* rect.getHeight() / lines.length));
			g2d.drawString(lines[i], tx, ty);
		}
	}

	private void encontrarSiguienteClip(int pw, int ph) {
		final boolean ltr = tabla.getComponentOrientation().isLeftToRight();

		if (col == 0) {
			if (ltr) {
				clip.x = 0;
			} else {
				clip.x = totalAnchoCol;
			}

			clip.y += clip.height;

			clip.width = 0;
			clip.height = 0;
			int rowCount = tabla.getRowCount();
			int rowHeight = tabla.getRowHeight(fila);
			do {
				clip.height += rowHeight;

				if (++fila >= rowCount) {
					break;
				}

				rowHeight = tabla.getRowHeight(fila);
			} while (clip.height + rowHeight <= ph);
		}
		if (modoImprimir == JTable.PrintMode.FIT_WIDTH) {
			clip.x = 0;
			clip.width = totalAnchoCol;
			return;
		}

		if (ltr) {
			clip.x += clip.width;
		}
		clip.width = 0;

		int colCount = tabla.getColumnCount();
		int colWidth = colModel.getColumn(col).getWidth();
		do {
			clip.width += colWidth;
			if (!ltr) {
				clip.x -= colWidth;
			}

			if (++col >= colCount) {
				col = 0;

				break;
			}

			colWidth = colModel.getColumn(col).getWidth();
		} while (clip.width + colWidth <= pw);

	}
}