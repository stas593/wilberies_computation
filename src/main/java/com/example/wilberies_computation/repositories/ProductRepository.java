package com.example.wilberies_computation.repositories;

import com.example.wilberies_computation.entity.wildberies.ProductEntity;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {

  Optional<ProductEntity> getByVendorCode(String vendorCode);

}
