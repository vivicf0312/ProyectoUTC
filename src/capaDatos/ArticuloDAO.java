package capaDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilidades.Logging;
import utilidades.MetodoCerrar;
import capaNegocio.Articulo;

import com.mysql.jdbc.Connection;

public class ArticuloDAO extends AbstractBaseDAO<Articulo> {
	Logger logger = Logging.obtenerServerLogger();

	@Override
	public synchronized int agregar(Articulo articulo) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;
		int idArticulo = -1;
		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement(
							"insert into articulos"
									+ " (tomo, folio,asiento,idEtiqueta,descripcion,marca,modelo,serie,condicion,idUbicacion,adquisicion,observaciones)"
									+ " values (?, ?, ?,?, ?, ?, ?, ?, ?, ?,?, ?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			int contador = 0;
			// establecer parametros
			myStmt.setInt(++contador, articulo.getTomo());
			myStmt.setInt(++contador, articulo.getFolio());
			myStmt.setInt(++contador, articulo.getAsiento());
			myStmt.setString(++contador, articulo.getIdEtiqueta());
			myStmt.setString(++contador, articulo.getDescripcion());
			myStmt.setString(++contador, articulo.getMarca());
			myStmt.setString(++contador, articulo.getModelo());
			myStmt.setString(++contador, articulo.getSerie());
			myStmt.setString(++contador, articulo.getCondicion());
			myStmt.setInt(++contador, articulo.getIdUbicacion());
			myStmt.setString(++contador, articulo.getAdquisicion());
			myStmt.setString(++contador, articulo.getObservaciones());

			// ejecutar mySQL
			myStmt.executeUpdate();
			ResultSet rs = myStmt.getGeneratedKeys();
			if (rs.next())
				idArticulo = rs.getInt(1);

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para agregar artículos",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return idArticulo;
	}

	@Override
	public synchronized boolean actualizar(Articulo articulo)
			throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("update articulos"
							+ " set tomo=?, folio=?, asiento=?, idEtiqueta=?, descripcion=?,"
							+ " marca=?, modelo=?, serie=?, condicion=?, idUbicacion=?, adquisicion=?,observaciones=? "
							+ "where idArticulo=?");
			int contador = 0;
			// establecer parametros
			myStmt.setInt(++contador, articulo.getTomo());
			myStmt.setInt(++contador, articulo.getFolio());
			myStmt.setInt(++contador, articulo.getAsiento());
			myStmt.setString(++contador, articulo.getIdEtiqueta());
			myStmt.setString(++contador, articulo.getDescripcion());
			myStmt.setString(++contador, articulo.getMarca());
			myStmt.setString(++contador, articulo.getModelo());
			myStmt.setString(++contador, articulo.getSerie());
			myStmt.setString(++contador, articulo.getCondicion());
			myStmt.setInt(++contador, articulo.getIdUbicacion());
			myStmt.setString(++contador, articulo.getAdquisicion());
			myStmt.setString(++contador, articulo.getObservaciones());
			myStmt.setInt(++contador, articulo.getIdArticulo());

			// ejecutar mySQL
			myStmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para actualizar artículos",
					e);
			return false;
		} finally {
			logger.info("Articulo actualizado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return true;
	}

	@Override
	public synchronized boolean eliminar(int idArticulo) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("DELETE FROM articulos where idArticulo=?");

			myStmt.setInt(1, idArticulo);

			myStmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para eliminar articulos",
					e);
			return false;
		} finally {
			logger.info("Articulo eliminado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}

		return true;
	}

	@Override
	public synchronized Articulo mostrar(int idArticulo) throws SQLException {
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		Articulo articulo = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt
					.executeQuery("SELECT * from articulos where idArticulo="
							+ idArticulo);

			while (resultado.next()) {

				articulo = obtenerArticulo(resultado);
				break;
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar un articulo",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return articulo;
	}

	@Override
	public synchronized List<Articulo> mostrarTodo() throws SQLException {
		ArrayList<Articulo> articulos = new ArrayList<Articulo>();
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt.executeQuery("SELECT * from articulos");

			while (resultado.next()) {

				articulos.add(obtenerArticulo(resultado));
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar todos los articulos",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return articulos;
	}

	private Articulo obtenerArticulo(ResultSet resultado) throws SQLException {

		Articulo articulo = new Articulo(
				resultado.getInt("articulos.idArticulo"),
				resultado.getInt("articulos.tomo"),
				resultado.getInt("articulos.folio"),
				resultado.getInt("articulos.asiento"),
				resultado.getString("articulos.idEtiqueta"),
				resultado.getString("articulos.descripcion"),
				resultado.getString("articulos.marca"),
				resultado.getString("articulos.modelo"),
				resultado.getString("articulos.serie"),
				resultado.getString("articulos.condicion"),
				resultado.getInt("articulos.idUbicacion"),
				resultado.getString("articulos.adquisicion"),
				resultado.getString("articulos.observaciones"));
		return articulo;
	}

	public void cerrarConexion() throws SQLException {
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			sqlConnection.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error cerrando conexion de la clase ArticuloDAO ", e);
		}
	}
}