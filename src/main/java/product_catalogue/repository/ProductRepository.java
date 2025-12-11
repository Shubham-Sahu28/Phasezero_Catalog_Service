package product_catalogue.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import product_catalogue.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Query("select count(p) from Product p where p.partNumber = :partNumber")
	Integer countOfPartNumber(@Param("partNumber") String partNumber);

	@Query("select p from Product p where p.partName like %:name%")
	List<Product> searchByPartName(@Param("name") String name);
	
	@Query("select p from Product p where p.category = :category")
	List<Product> filterByCategory(@Param("category") String category);
	
	@Query("select p from Product p order by p.price asc")
	List<Product> sortProductByPrice();
}
