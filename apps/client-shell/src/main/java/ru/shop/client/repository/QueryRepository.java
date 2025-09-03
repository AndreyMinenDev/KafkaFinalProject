package ru.shop.client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shop.client.models.QueryEntity;

@Repository
public interface QueryRepository extends JpaRepository<QueryEntity, Long> {
}
