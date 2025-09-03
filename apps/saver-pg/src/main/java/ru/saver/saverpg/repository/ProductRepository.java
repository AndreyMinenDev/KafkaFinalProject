package ru.saver.saverpg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.saver.saverpg.models.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {
}
