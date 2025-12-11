package product_catalogue.exception;

public class NoRecordException extends RuntimeException{

	public NoRecordException() {
		super();
	}
	
	public NoRecordException(String message) {
		super(message);
	}
}
