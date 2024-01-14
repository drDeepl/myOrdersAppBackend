package ru.myorder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.myorder.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
