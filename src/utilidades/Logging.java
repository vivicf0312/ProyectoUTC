package utilidades;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {

	public static String myServerLogger = "ProyectoUTCServer";
	private static Logger serverlogger = null;
	public static String myClientLogger = "ProyectoUTCClient";
	private static Logger clientlogger = null;

	public static Logger obtenerClientLogger() {
		if (clientlogger == null) {
			clientlogger = Logger.getLogger(myClientLogger);
			initClientLogger();
		}
		return clientlogger;
	}

	public static Logger obtenerServerLogger() {
		if (serverlogger == null) {
			serverlogger = Logger.getLogger(myServerLogger);
			initServerLogger();
		}
		return serverlogger;
	}

	public static void logException(Exception e) {
		serverlogger.log(Level.SEVERE, "Error", e);
	}

	public static void initServerLogger() {

		FileHandler fh = null;
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			fh = new FileHandler("ProyectoUTC_server.log"
					+ format.format(Calendar.getInstance().getTime()) + ".log");
			fh.setFormatter(new Formatter() {
				public String format(LogRecord record) {
					return record.getLevel() + " : "
							+ record.getSourceClassName() + " -:- "
							+ record.getSourceMethodName() + " -:- "
							+ record.getMessage() + "\n";
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		serverlogger.addHandler(fh);
	}

	public static void initClientLogger() {

		FileHandler fh = null;
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
		try {
			fh = new FileHandler("ProyectoUTC_client.log"
					+ format.format(Calendar.getInstance().getTime()) + ".log");
			fh.setFormatter(new Formatter() {

				public String format(LogRecord record) {
					return record.getLevel() + " : "
							+ record.getSourceClassName() + " -:- "
							+ record.getSourceMethodName() + " -:- "
							+ record.getMessage() + "\n";
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		clientlogger.addHandler(fh);
	}
}
