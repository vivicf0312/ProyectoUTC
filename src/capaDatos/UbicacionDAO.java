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
import capaNegocio.Ubicacion;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class UbicacionDAO extends AbstractBaseDAO<Ubicacion> {
	Logger logger = Logging.obtenerServerLogger();

	@Override
	public synchronized int agregar(Ubicacion ubicacion) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;
		int idUbicacion = -1;

		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.prepareStatement("insert into ubicaciones"
					+ " (descripcion)" + " values (?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, ubicacion.getDescripcion());

			// ejecutar mySQL
			myStmt.executeUpdate();

			ResultSet rs = myStmt.getGeneratedKeys();
			if (rs.next())
				idUbicacion = rs.getInt(1);

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para agregar ubicaciones",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return idUbicacion;
	}

	@Override
	public synchronized boolean actualizar(Ubicacion ubicacion)
			throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.prepareStatement("update ubicaciones"
					+ " set descripcion=?" + " where idUbicacion=?");

			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, ubicacion.getDescripcion());
			myStmt.setInt(++contador, ubicacion.getIdUbicacion());

			// ejecutar mySQL
			myStmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para actualizar ubicaciones",
					e);
			return false;
		} finally {
			logger.info("Ubicacion actualizada");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return true;
	}

	@Override
	public synchronized boolean eliminar(int idUbicacion) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("DELETE FROM ubicaciones where idUbicacion=?");

			myStmt.setInt(1, idUbicacion);
			logger.info(myStmt.toString());
			myStmt.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			logger.log(Level.SEVERE, "No puedes eliminar una ubicacion en uso",
					e);
			throw new IllegalArgumentException(
					"No puedes eliminar una ubicacion en uso");
		} catch (SQLException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para eliminar ubicaciones",
					e);
			return false;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para eliminar ubicaciones",
					e);
			return false;
		} finally {
			logger.info("Ubicacion eliminada");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}

		return true;
	}

	@Override
	public synchronized Ubicacion mostrar(int idUbicacion) throws SQLException {

		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		Ubicacion ubicacion = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt
					.executeQuery("SELECT * from ubicaciones where idUbicacion="
							+ idUbicacion);

			while (resultado.next()) {

				ubicacion = obtenerUbicacion(resultado);
				break;
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar una ubicacion",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return ubicacion;
	}

	@Override
	public synchronized List<Ubicacion> mostrarTodo() throws SQLException {

		ArrayList<Ubicacion> ubicaciones = new ArrayList<Ubicacion>();
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt.executeQuery("SELECT * from ubicaciones");

			while (resultado.next()) {

				ubicaciones.add(obtenerUbicacion(resultado));

			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar todas las ubicaciones",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return ubicaciones;
	}

	private Ubicacion obtenerUbicacion(ResultSet resultado) throws SQLException {

		Ubicacion ubicacion = new Ubicacion(
				resultado.getInt("ubicaciones.idUbicacion"),
				resultado.getString("ubicaciones.descripcion"));
		return ubicacion;
	}

	public void cerrarConexion() throws SQLException {
		Connection sqlConnection = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			sqlConnection.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error cerrando conexion de la clase UbicacionDAO ", e);
		}
	}
}