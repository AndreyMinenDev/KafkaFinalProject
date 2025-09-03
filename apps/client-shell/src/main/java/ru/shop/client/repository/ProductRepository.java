package ru.shop.client.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shop.client.models.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    @Query("select p from ProductEntity p where lower(p.name) like lower(:q)")
    List<ProductEntity> searchByNameLike(@Param("q") String q, Pageable pageable);

    List<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
