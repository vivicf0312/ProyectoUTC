package utilidades;

@SuppressWarnings("serial")
public class RequestFailureException extends Exception {

	int httpCode;

	public RequestFailureException(int httpCode, String message) {
		super(message);
		this.httpCode = httpCode;
	}

	public int getHttpCode() {
		return httpCode;
	}
}
