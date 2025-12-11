package product_catalogue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import product_catalogue.dto.ResponseStructure;
import product_catalogue.entity.Product;
import product_catalogue.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<ResponseStructure<Product>> addNewProduct(@RequestBody Product product){
		return productService.addNewProduct(product);
	}
	
	@GetMapping
	public ResponseEntity<ResponseStructure<List<Product>>> getAllProducts(){
		return productService.getAllProducts();
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<ResponseStructure<List<Product>>> searchByPartName(@PathVariable String name){
		return productService.searchByPartName(name);
	}
	
	@GetMapping("/filter/{category}")
	public ResponseEntity<ResponseStructure<List<Product>>> filterByCategory(@PathVariable String category){
		return productService.filterByCategory(category);
	}
	
	@GetMapping("/sort")
	public ResponseEntity<ResponseStructure<List<Product>>> sortProductByPrice(){
		return productService.sortProductByPrice();
	}
	
	@GetMapping("/inventory/value")
	public ResponseEntity<ResponseStructure<String>> totalInventoryValue(){
		return productService.totalInventoryValue();
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ResponseStructure<Product>> getByID(@PathVariable Integer id){
		return productService.getByID(id);
	}	
	
}
