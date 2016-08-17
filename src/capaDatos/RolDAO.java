package capaDatos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilidades.Enumeracion.Operaciones;
import utilidades.Logging;
import utilidades.MetodoCerrar;
import capaNegocio.Rol;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class RolDAO extends AbstractBaseDAO<Rol> {
	Logger logger = Logging.obtenerServerLogger();

	@Override
	public synchronized int agregar(Rol rol) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;
		int idRol = -1;
		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.prepareStatement("insert into roles"
					+ " (descripcion,operacion)" + " values (?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, rol.getDescripcion());
			myStmt.setString(++contador,
					obtenerOperacionesStr(rol.getOperaciones()));

			// ejecutar mySQL
			myStmt.executeUpdate();
			ResultSet rs = myStmt.getGeneratedKeys();
			if (rs.next())
				idRol = rs.getInt(1);

		} catch (SQLException e) {
			logger.log(Level.SEVERE,
					"Error conectando a la Base de Datos para agregar roles", e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return idRol;
	}

	@Override
	public synchronized boolean actualizar(Rol rol) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.prepareStatement("update roles"
					+ " set descripcion=?, operacion=?" + " where idRol=?");
			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, rol.getDescripcion());
			myStmt.setString(++contador,
					obtenerOperacionesStr(rol.getOperaciones()));
			myStmt.setInt(++contador, rol.getIdRol());

			// ejecutar mySQL
			myStmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para actualizar roles",
					e);
			return false;
		} finally {
			logger.info("Rol Actualizado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return true;
	}

	@Override
	public synchronized boolean eliminar(int idRol) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("DELETE FROM roles where idRol=?");
			myStmt.setInt(1, idRol);
			myStmt.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			logger.log(Level.SEVERE, "No puedes eliminar un rol en uso", e);
			throw new IllegalArgumentException(
					"No puedes eliminar un rol en uso");
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para actualizar roles",
					e);
			return false;
		} finally {
			logger.info("Rol eliminado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}

		return true;
	}

	@Override
	public synchronized Rol mostrar(int idRol) throws SQLException {
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		Rol rol = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt.executeQuery("SELECT * from roles where idRol="
					+ idRol);

			while (resultado.next()) {

				rol = obtenerRol(resultado);
				break;
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar un rol",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return rol;
	}

	@Override
	public synchronized List<Rol> mostrarTodo() throws SQLException {

		ArrayList<Rol> roles = new ArrayList<Rol>();
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt.executeQuery("SELECT * from roles");

			while (resultado.next()) {

				roles.add(obtenerRol(resultado));
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar todos los roles",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return roles;
	}

	private Rol obtenerRol(ResultSet resultado) throws SQLException {

		Rol rol = new Rol(resultado.getInt("roles.idRol"),
				resultado.getString("roles.descripcion"),
				obtenerOperaciones(resultado.getString("roles.operacion")));

		return rol;
	}

	private Set<Operaciones> obtenerOperaciones(String operacionesStr) {
		HashSet<Operaciones> operaciones = new HashSet<Operaciones>();
		String[] resultado = operacionesStr.split(",");
		for (String operacion : resultado) {
			operaciones.add(Operaciones.valueOf(operacion));
		}
		return operaciones;
	}

	private String obtenerOperacionesStr(Set<Operaciones> operaciones) {
		String operacionStr = "";

		for (Operaciones operacion : operaciones) {
			operacionStr += operacion.name() + ",";
		}
		return operacionStr;
	}

	public void cerrarConexion() throws SQLException {
		Connection sqlConnection = null;

		try {
			sqlConnection = connectionManager.obtenerConexion();
			sqlConnection.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error cerrando conexion de la clase RolDAO ", e);
		}
	}
}