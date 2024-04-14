package syt.springorm;

import org.springframework.data.repository.CrudRepository;

import syt.springorm.data.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    
}
