package utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MetodoCerrar {

	public static void cerrarConexion(Statement myStmt, ResultSet myRs)
			throws SQLException {

		if (myRs != null) {
			myRs.close();
		}

		if (myStmt != null) {
			myStmt.close();
		}
	}

	public static void cerrarConexion(Statement myStmt) throws SQLException {
		cerrarConexion(myStmt, null);
	}
}
