package product_catalogue.exception;

public class NegativeValueException extends RuntimeException {

	public NegativeValueException() {
		super();
	}

	public NegativeValueException(String message) {
		super(message);
	}
}
