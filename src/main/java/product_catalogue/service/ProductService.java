package product_catalogue.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import product_catalogue.dto.ResponseStructure;
import product_catalogue.entity.Product;
import product_catalogue.exception.DuplicateDataException;
import product_catalogue.exception.IdNotFoundException;
import product_catalogue.exception.InvalidInputException;
import product_catalogue.exception.NegativeValueException;
import product_catalogue.exception.NoRecordException;
import product_catalogue.exception.NullInputException;
import product_catalogue.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public ResponseEntity<ResponseStructure<Product>> addNewProduct(Product product) {

		if (product.getPartNumber() == null || product.getPartName() == null || product.getCategory() == null
				|| product.getPrice() == null || product.getStock() == null) {
			throw new NullInputException("Null value cannot be inserted, please provide a valid Input");
		} 
		else if (productRepository.countOfPartNumber(product.getPartNumber()) > 0) {
			throw new DuplicateDataException("Part Number already exists: " + product.getPartNumber());
		}
		else if (product.getPrice() < 0 || product.getStock() < 0) {
			throw new NegativeValueException("Products price or stock cannot have a negative value");
		}

		product.setCategory(product.getCategory().toLowerCase());
		product.setPartName(product.getPartName().toLowerCase());

		ResponseStructure<Product> response = new ResponseStructure<Product>();
		response.setStatusCode(HttpStatus.OK);
		response.setMessage("Product data successfully saved");
		response.setData(productRepository.save(product));

		return new ResponseEntity<ResponseStructure<Product>>(response, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Product>>> getAllProducts() {
		List<Product> products = productRepository.findAll();
		ResponseStructure<List<Product>> response = new ResponseStructure<List<Product>>();

		if (!products.isEmpty()) {
			response.setStatusCode(HttpStatus.OK);
			response.setMessage("Product details found successfully");
			response.setData(products);
			return new ResponseEntity<ResponseStructure<List<Product>>>(response, HttpStatus.OK);
		} else {
			throw new NoRecordException("No Data exist in the Database");
		}
	}

	public ResponseEntity<ResponseStructure<List<Product>>> searchByPartName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidInputException("Invalid Part Name, please provide a valid part name");
		}

		List<Product> products = productRepository.searchByPartName(name.toLowerCase());
		ResponseStructure<List<Product>> response = new ResponseStructure<List<Product>>();

		if (products.size() > 0) {
			response.setStatusCode(HttpStatus.OK);
			response.setMessage("Products Info found by Part Name: " + name);
			response.setData(products);
			return new ResponseEntity<ResponseStructure<List<Product>>>(response, HttpStatus.OK);
		} else {
			throw new NoRecordException("No Product is present with the Part Name: " + name);
		}
	}

	public ResponseEntity<ResponseStructure<List<Product>>> filterByCategory(String category) {
		List<Product> products = productRepository.filterByCategory(category.toLowerCase());
		ResponseStructure<List<Product>> response = new ResponseStructure<List<Product>>();
		if (products.size() > 0) {
			response.setStatusCode(HttpStatus.OK);
			response.setMessage("Products fetched based on category: " + category);
			response.setData(products);
			return new ResponseEntity<ResponseStructure<List<Product>>>(response, HttpStatus.OK);
		} else {
			throw new NoRecordException("No Product found under the Category: " + category);
		}
	}

	public ResponseEntity<ResponseStructure<List<Product>>> sortProductByPrice() {
		List<Product> products = productRepository.sortProductByPrice();
		ResponseStructure<List<Product>> response = new ResponseStructure<List<Product>>();
		if (products.size() > 0) {
			response.setStatusCode(HttpStatus.OK);
			response.setMessage("Products are sorted based on price in ascending order");
			response.setData(products);
			return new ResponseEntity<ResponseStructure<List<Product>>>(response, HttpStatus.OK);
		} else {
			throw new NoRecordException("No Product data found in the Database");
		}
	}

	public ResponseEntity<ResponseStructure<String>> totalInventoryValue() {
		List<Product> products = productRepository.findAll();
		ResponseStructure<String> response = new ResponseStructure<String>();
		Double inventoryValue = 0.0;
		if (products.size() > 0) {
			for (Product product : products) {
				inventoryValue += product.getPrice() * product.getStock();
			}
			response.setStatusCode(HttpStatus.OK);
			response.setMessage("Inventory value computed successfully");
			response.setData("Total Inventory value : " + inventoryValue);
			return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.OK);
		} else {
			throw new NoRecordException("No Product data found in the Database");
		}

	}

	public ResponseEntity<ResponseStructure<Product>> getByID(Integer id) {
		Optional<Product> opt = productRepository.findById(id);
		ResponseStructure<Product> response = new ResponseStructure<Product>();
		if (opt.isPresent()) {
			response.setStatusCode(HttpStatus.OK);
			response.setMessage("Product Info found based on Id:" + id);
			response.setData(opt.get());
			return new ResponseEntity<ResponseStructure<Product>>(response, HttpStatus.OK);
		} else {
			throw new IdNotFoundException("Invalid Id, no Product information found");
		}
	}
}
