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
import capaNegocio.Registro;

import com.mysql.jdbc.Connection;

public class RegistroDAO extends AbstractBaseDAO<Registro> {
	Logger logger = Logging.obtenerServerLogger();

	@Override
	public synchronized int agregar(Registro registro) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;
		int idRegistro = -1;
		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.prepareStatement("insert into registros"
					+ " (descripcion,idUsuario)" + " values (?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);

			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, registro.getDescripcion());
			myStmt.setInt(++contador, registro.getIdUsuario());

			// ejecutar mySQL
			myStmt.executeUpdate();
			ResultSet rs = myStmt.getGeneratedKeys();
			if (rs.next())
				idRegistro = rs.getInt(1);

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para agregar registros",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return idRegistro;
	}

	@Override
	public synchronized boolean actualizar(Registro registro)
			throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.prepareStatement("update registros"
					+ " set descripcion=?" + " where idRegistro=?");

			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, registro.getDescripcion());
			myStmt.setInt(++contador, registro.getIdRegistro());

			// ejecutar mySQL
			myStmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para actualizar registros",
					e);
			return false;
		} finally {
			logger.info("Registros Actualizados");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return true;
	}

	@Override
	public synchronized boolean eliminar(int idRegistro) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("DELETE FROM registros where idRegistro=?");

			myStmt.setInt(1, idRegistro);

			myStmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para eliminar registros",
					e);
			return false;
		} finally {
			logger.info("Registro eliminado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return true;
	}

	@Override
	public synchronized Registro mostrar(int idRegistro) throws SQLException {
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		Registro registro = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt
					.executeQuery("SELECT * from registros where idRegistro="
							+ idRegistro);

			while (resultado.next()) {

				registro = obtenerRegistro(resultado);
				break;
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar una registro",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return registro;
	}

	@Override
	public synchronized List<Registro> mostrarTodo() throws SQLException {

		ArrayList<Registro> registros = new ArrayList<Registro>();
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt.executeQuery("SELECT * from registros");

			while (resultado.next()) {

				registros.add(obtenerRegistro(resultado));
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar todas los registros ",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return registros;
	}

	private Registro obtenerRegistro(ResultSet resultado) throws SQLException {

		Registro registro = new Registro(
				resultado.getInt("registros.idRegistro"),
				resultado.getString("registros.descripcion"),
				resultado.getInt("registros.idUsuario"),
				resultado.getString("registros.timeStamp"));
		return registro;
	}

	public void cerrarConexion() throws SQLException {
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			sqlConnection.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error cerrando conexion de la clase RegistroDAO ", e);
		}
	}
}