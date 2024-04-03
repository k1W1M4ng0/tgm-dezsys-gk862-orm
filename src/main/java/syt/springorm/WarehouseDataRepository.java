package syt.springorm;

import org.springframework.data.repository.CrudRepository;

import syt.springorm.data.WarehouseData;

public interface WarehouseDataRepository extends CrudRepository<WarehouseData, Integer> {
    
}
