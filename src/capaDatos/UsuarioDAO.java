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
import capaNegocio.Usuario;

import com.mysql.jdbc.Connection;

public class UsuarioDAO extends AbstractBaseDAO<Usuario> {
	Logger logger = Logging.obtenerServerLogger();

	@Override
	public synchronized int agregar(Usuario usuario) throws SQLException {

		Connection sqlConnection = null;
		PreparedStatement myStmt = null;
		int idUsuario = -1;
		try {

			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement(
							"insert into usuarios"
									+ " (nombre, apellido1, apellido2, nombreUsuario,clave, idRol)"
									+ " values (?, ?, ?, ?, ?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, usuario.getNombre());
			myStmt.setString(++contador, usuario.getApellido1());
			myStmt.setString(++contador, usuario.getApellido2());
			myStmt.setString(++contador, usuario.getNombreUsuario());
			myStmt.setString(++contador, usuario.getClave());
			myStmt.setInt(++contador, usuario.getIdRol());

			// ejecutar mySQL
			myStmt.executeUpdate();
			ResultSet rs = myStmt.getGeneratedKeys();
			if (rs.next())
				idUsuario = rs.getInt(1);

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para agregar usuarios",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return idUsuario;
	}

	@Override
	public synchronized boolean actualizar(Usuario usuario) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;

		try {
			// preparar sentencia
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("update usuarios"
							+ " set nombre=?, apellido1=?, apellido2=?, nombreUsuario=?,clave=?,idRol=?"
							+ " where idUsuario=?");
			int contador = 0;
			// establecer parametros
			myStmt.setString(++contador, usuario.getNombre());
			myStmt.setString(++contador, usuario.getApellido1());
			myStmt.setString(++contador, usuario.getApellido2());
			myStmt.setString(++contador, usuario.getNombreUsuario());
			myStmt.setString(++contador, usuario.getClave());
			myStmt.setInt(++contador, usuario.getIdRol());
			myStmt.setInt(++contador, usuario.getIdUsuario());

			// ejecutar mySQL
			myStmt.executeUpdate();

		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para actualizar usuarios",
					e);
			return false;
		} finally {
			logger.info("Usuario actualizado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}
		return true;
	}

	@Override
	public synchronized boolean eliminar(int idUsuario) throws SQLException {
		PreparedStatement myStmt = null;
		Connection sqlConnection = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection
					.prepareStatement("DELETE FROM usuarios where idUsuario=?");

			myStmt.setInt(1, idUsuario);

			myStmt.executeUpdate();
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para eliminar usuarios",
					e);
			return false;
		} finally {
			logger.info("Usuario eliminado");
			MetodoCerrar.cerrarConexion(myStmt);
			connectionManager.devolverConexion(sqlConnection);
		}

		return true;
	}

	@Override
	public synchronized Usuario mostrar(int idUsuario) throws SQLException {
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		Usuario usuario = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt
					.executeQuery("SELECT * from usuarios where idUsuario="
							+ idUsuario);

			while (resultado.next()) {

				usuario = obtenerUsuario(resultado);
				break;
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar un usuario",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return usuario;
	}

	@Override
	public synchronized List<Usuario> mostrarTodo() throws SQLException {

		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt.executeQuery("SELECT * from usuarios");

			while (resultado.next()) {

				usuarios.add(obtenerUsuario(resultado));
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar todos los usuarios",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return usuarios;
	}

	private Usuario obtenerUsuario(ResultSet resultado) throws SQLException {

		Usuario usuario = new Usuario(resultado.getInt("usuarios.idUsuario"),
				resultado.getString("usuarios.nombre"),
				resultado.getString("usuarios.apellido1"),
				resultado.getString("usuarios.apellido2"),
				resultado.getString("usuarios.nombreUsuario"),
				resultado.getString("usuarios.clave"),
				resultado.getInt("usuarios.idRol"));
		return usuario;
	}

	public Usuario buscar(String nombreUsuario, String clave)
			throws SQLException {
		Statement myStmt = null;
		Connection sqlConnection = null;
		ResultSet resultado = null;
		Usuario usuario = null;
		try {
			sqlConnection = connectionManager.obtenerConexion();
			myStmt = sqlConnection.createStatement();
			resultado = myStmt
					.executeQuery("SELECT * from usuarios where nombreUsuario='"
							+ nombreUsuario + "' AND clave='" + clave + "'");

			while (resultado.next()) {

				usuario = obtenerUsuario(resultado);
				break;
			}
		} catch (SQLException e) {
			logger.log(
					Level.SEVERE,
					"Error conectando a la Base de Datos para seleccionar un usuario",
					e);
		} finally {
			MetodoCerrar.cerrarConexion(myStmt, resultado);
			connectionManager.devolverConexion(sqlConnection);
		}
		return usuario;
	}
}