package product_catalogue.exception;

public class NullInputException extends RuntimeException{

	public NullInputException() {
		super();
	}
	
	public NullInputException(String message) {
		super(message);
	}
}
