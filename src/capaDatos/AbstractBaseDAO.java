package capaDatos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.mysql.jdbc.Connection;

public abstract class AbstractBaseDAO<T> {

	protected ConnectionManager connectionManager;

	public AbstractBaseDAO() {
		connectionManager = ConnectionManager.obtenerInstancia();
	}

	public ResultSet executeQuery(String query) throws SQLException {
		Connection sqlConnection = connectionManager.obtenerConexion();
		Statement myStmt = sqlConnection.createStatement();
		ResultSet resultado = myStmt.executeQuery(query);
		return resultado;

	}

	public abstract int agregar(T t) throws SQLException;

	public abstract boolean actualizar(T t) throws SQLException;

	public abstract boolean eliminar(int id) throws SQLException;

	public abstract T mostrar(int id) throws SQLException;

	public abstract List<T> mostrarTodo() throws SQLException;

}
