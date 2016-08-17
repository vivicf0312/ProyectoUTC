package capaDatos;

import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import utilidades.Logging;

import com.mysql.jdbc.Connection;

public class ConnectionManager {
	Logger logger = Logging.obtenerServerLogger();
	private static ConnectionManager connectionManager = null;
	PreparedStatement myStmt = null;
	List<Connection> connectionList = new ArrayList<Connection>();
	int maxConnection = 3;

	private ConnectionManager() {
	}

	public synchronized static ConnectionManager obtenerInstancia() {
		if (connectionManager == null) {
			connectionManager = new ConnectionManager();
		}
		return connectionManager;
	}

	private Connection crearConexion() {

		Connection sqlConnection = null;
		try {
			String user;
			String password;
			String dburl;
			try {
				Properties prop = new Properties();
				prop.load(new FileInputStream("javaconnection.properties"));

				user = prop.getProperty("user");
				password = prop.getProperty("password");
				dburl = prop.getProperty("dburl");
			} catch (Exception exp) {
				logger.warning("Error reading javaconnection.properties. Using defaults.");
				user = "root";
				password = "root";
				dburl = "jdbc:mysql://localhost:3306/inventario";
			}
			Class.forName("com.mysql.jdbc.Driver");
			sqlConnection = (Connection) DriverManager.getConnection(dburl,
					user, password);

			logger.info("MySql exitosamente conectado al url: " + dburl);

		} catch (SQLException e) {
			logger.log(Level.SEVERE,
					"Error creando conexion con la Base de Datos ", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error creando conexion ", e);
		}
		return sqlConnection;
	}

	public synchronized Connection obtenerConexion() {
		Connection sqlConnection = null;
		while (true) {
			if (connectionList.size() < maxConnection) {
				sqlConnection = crearConexion();
				connectionList.add(sqlConnection);
				break;
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					logger.log(Level.SEVERE, "Error obteniendo conexion ", e);
				}
			}
		}
		return sqlConnection;
	}

	public synchronized void devolverConexion(Connection sqlConnection) {
		if (sqlConnection == null) {
			logger.log(Level.WARNING, "sqlConnection es nulo");
		} else if (!connectionList.contains(sqlConnection)) {
			logger.log(Level.WARNING, "sqlConnection no esta en lista");
		} else {
			connectionList.remove(sqlConnection);
			try {
				sqlConnection.close();
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Error cerrando conexion", e);
			}
			sqlConnection = null;
			notifyAll();
		}
		logger.info("Cantidad conectados= " + connectionList.size());
	}
}
