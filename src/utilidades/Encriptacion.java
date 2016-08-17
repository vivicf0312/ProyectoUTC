package utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Encriptacion {
	private static MessageDigest md;
	public static Logger logger = Logging.obtenerClientLogger();

	public static String encriptar(String clave) {
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = clave.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			logger.log(Level.SEVERE, "Error al encriptar", ex);
		}
		return null;
	}
}