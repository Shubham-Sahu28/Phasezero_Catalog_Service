package product_catalogue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import product_catalogue.dto.ErrorStructure;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<ErrorStructure> handleduplicateDataException(DuplicateDataException exception){
		ErrorStructure response = new ErrorStructure();
		response.setStatusCode(HttpStatus.CONFLICT.value());
		response.setMessage(exception.getMessage());
		response.setError("Duplicate Data");
		return new ResponseEntity<ErrorStructure>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(NegativeValueException.class)
	public ResponseEntity<ErrorStructure> handleNegativeValueException(NegativeValueException exception){
		ErrorStructure response = new ErrorStructure();
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setError("Negative Value");
		return new ResponseEntity<ErrorStructure>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NoRecordException.class)
	public ResponseEntity<ErrorStructure> handleNoDataFoundException(NoRecordException exception){
		ErrorStructure response = new ErrorStructure();
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(exception.getMessage());
		response.setError("Data not found");
		return new ResponseEntity<ErrorStructure>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NullInputException.class)
	public ResponseEntity<ErrorStructure> handleNullInputException(NullInputException exception){
		ErrorStructure response = new ErrorStructure();
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setError("Null Values");
		return new ResponseEntity<ErrorStructure>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorStructure> handleInvalidInputException(InvalidInputException exception){
		ErrorStructure response = new ErrorStructure();
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(exception.getMessage());
		response.setError("Invalid input");
		return new ResponseEntity<ErrorStructure>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ErrorStructure> handleIdNotFoundException(IdNotFoundException exception){
		ErrorStructure response = new ErrorStructure();
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(exception.getMessage());
		response.setError("Invalid ID");
		return new ResponseEntity<ErrorStructure>(response, HttpStatus.NOT_FOUND);
	}
}
