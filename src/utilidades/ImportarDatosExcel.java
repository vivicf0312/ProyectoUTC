package utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import capaAPICliente.ArticuloCliente;
import capaAPICliente.UbicacionCliente;
import capaNegocio.Articulo;
import capaNegocio.Ubicacion;
import capaNegocio.Usuario;

public class ImportarDatosExcel {
	public static int COLUMN_TOMO = 0;
	public static int COLUMN_FOLIO = 1;
	public static int COLUMN_ASIENTO = 2;
	public static int COLUMN_DESCRIPCION = 5;
	public static int COLUMN_MARCA = 6;
	public static int COLUMN_MODELO = 7;
	public static int COLUMN_SERIE = 8;
	public static int COLUMN_CONDICION = 9;
	public static int COLUMN_UBICACION = 10;
	public static int COLUMN_ADQUISICION = 11;
	public static int COLUMN_OBSERVACION = 12;

	Logger logger = Logging.obtenerClientLogger();
	private HashMap<String, Ubicacion> ubicacionMap = new HashMap<String, Ubicacion>();
	private HashMap<String, Articulo> articuloMap = new HashMap<String, Articulo>();
	private UbicacionCliente ubicacionCliente;
	ArticuloCliente articuloCliente;
	private JFrame parentWindow;
	int numNuevasUbicaciones = 0;
	int numNuevosArticulos = 0;

	public ImportarDatosExcel(Usuario usuarioActual, String serverIP,
			JFrame parentWindow) {
		this.parentWindow = parentWindow;
		ubicacionCliente = new UbicacionCliente(
				usuarioActual.getNombreUsuario(), usuarioActual.getClave(),
				serverIP);
		articuloCliente = new ArticuloCliente(usuarioActual.getNombreUsuario(),
				usuarioActual.getClave(), serverIP);
		cargarUbicaciones();
		cargarArticulos();
	}

	public void importar() {

		JFileChooser examinar = new JFileChooser();
		examinar.setFileFilter(new FileNameExtensionFilter("Archivos Excel",
				"xls", "xlsx", "xlsm"));
		int opcion = examinar.showOpenDialog(parentWindow);
		File archivoExcel = null;
		if (opcion == JFileChooser.APPROVE_OPTION) {
			archivoExcel = examinar.getSelectedFile().getAbsoluteFile();
			JOptionPane.showMessageDialog(null,
					"Este es el getAbsoluteFile del Excel seleccionado, la ruta\n"
							+ archivoExcel);
			try {

				XSSFWorkbook leerExcel = new XSSFWorkbook(new FileInputStream(
						archivoExcel));
				XSSFSheet hojaExcel = leerExcel
						.getSheet("INVENTARIO DE BIENES INS");

				logger.info("Total number of rows in xls: "
						+ hojaExcel.getPhysicalNumberOfRows());
				logger.info("Last row number in xls: "
						+ hojaExcel.getLastRowNum());
				for (int rowIndex = 2; rowIndex < hojaExcel.getLastRowNum(); rowIndex++) {
					XSSFRow fila = hojaExcel.getRow(rowIndex);

					try {
						String ubicacionDescripcion = fila
								.getCell(COLUMN_UBICACION).getStringCellValue()
								.trim();
						Ubicacion ubicacion = obtenerUbicacion(ubicacionDescripcion);
						if (ubicacion != null) {
							Articulo articulo = new Articulo(
									(int) fila.getCell(COLUMN_TOMO)
											.getNumericCellValue(),
									(int) fila.getCell(COLUMN_FOLIO)
											.getNumericCellValue(),
									(int) fila.getCell(COLUMN_ASIENTO)
											.getNumericCellValue(),
									obtenerCeldaStringValue(fila,
											COLUMN_DESCRIPCION),
									obtenerCeldaStringValue(fila, COLUMN_MARCA),
									obtenerCeldaStringValue(fila, COLUMN_MODELO),
									obtenerCeldaStringValue(fila, COLUMN_SERIE),
									obtenerCeldaStringValue(fila,
											COLUMN_CONDICION), ubicacion
											.getIdUbicacion(),
									obtenerCeldaStringValue(fila,
											COLUMN_ADQUISICION),
									obtenerCeldaStringValue(fila,
											COLUMN_OBSERVACION));
							String keyArticulo = obtenerKeyArticulo(articulo);
							Articulo articuloExistente = obtenerArticuloExistente(keyArticulo);
							if (articuloExistente == null) {
								try {
									articulo = articuloCliente
											.agregar(articulo);
									articuloMap.put(keyArticulo, articulo);
									numNuevosArticulos++;
								} catch (RequestFailureException e) {
									logger.log(
											Level.SEVERE,
											"Error creando ubicacion "
													+ (fila.getRowNum() + 1), e);
								}
							} else {
								logger.info("Articulo ya existe y no se agregará de nuevo "
										+ (fila.getRowNum() + 1));
							}
						} else {
							logger.warning("No se encontró ubicación "
									+ ubicacionDescripcion);
						}
					} catch (Exception e) {
						logger.log(Level.SEVERE, "No se pudo importar fila "
								+ (fila.getRowNum() + 1), e);
					}
				}
				String mensaje = numNuevosArticulos + " articulos y "
						+ numNuevasUbicaciones
						+ " ubicaciones fueron importados ";
				JOptionPane.showMessageDialog(parentWindow, mensaje,
						"Datos Importados ", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception exp) {
				logger.log(Level.SEVERE, "Error importando de Excel", exp);
			}
		}
	}

	private String obtenerCeldaStringValue(XSSFRow fila, int numColumna) {
		String valorCelda = "";
		try {
			valorCelda = fila.getCell(numColumna).getStringCellValue();
		} catch (Exception e) {
			logger.info("En la fila número " + (fila.getRowNum() + 1)
					+ " la columna " + numColumna + " está vacía");
		}
		return valorCelda;
	}

	private Ubicacion obtenerUbicacion(String ubicacionDescripcion) {
		Ubicacion ubicacion = null;
		if (ubicacionMap.containsKey(ubicacionDescripcion)) {
			ubicacion = ubicacionMap.get(ubicacionDescripcion);
		} else {
			ubicacion = new Ubicacion(ubicacionDescripcion);
			try {
				ubicacion = ubicacionCliente.agregar(ubicacion);
				ubicacionMap.put(ubicacionDescripcion, ubicacion);
				numNuevasUbicaciones++;
			} catch (RequestFailureException e) {
				logger.log(Level.SEVERE, "Error creando ubicacion "
						+ ubicacionDescripcion, e);
			}
		}
		return ubicacion;
	}

	private Articulo obtenerArticuloExistente(String articuloKey) {
		Articulo articulo = null;
		if (articuloMap.containsKey(articuloKey)) {
			articulo = articuloMap.get(articuloKey);
		}
		return articulo;
	}

	private void cargarUbicaciones() {
		List<Ubicacion> ubicaciones = null;
		try {
			ubicaciones = ubicacionCliente.mostrarTodo();
		} catch (RequestFailureException e) {
			logger.log(Level.SEVERE, "Error al obtener ubicaciones", e);
		}
		for (Ubicacion ubicacion : ubicaciones) {
			ubicacionMap.put(ubicacion.getDescripcion(), ubicacion);
		}
		logger.info("Mapa Ubicaciones: " + ubicacionMap);
	}

	private void cargarArticulos() {
		List<Articulo> articulos = null;
		// key combination: tomo,folio,asiento
		try {
			articulos = articuloCliente.mostrarTodo();
		} catch (RequestFailureException e) {
			logger.log(Level.SEVERE, "Error al obtener articulos", e);
		}
		for (Articulo articulo : articulos) {
			articuloMap.put(obtenerKeyArticulo(articulo), articulo);
		}
		logger.info("Mapa Articulos: " + articuloMap);
	}

	private String obtenerKeyArticulo(Articulo articulo) {
		return articulo.getTomo() + ":" + articulo.getFolio() + ":"
				+ articulo.getAsiento();
	}
}
