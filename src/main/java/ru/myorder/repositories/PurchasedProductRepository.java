package ru.myorder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.myorder.models.PurchasedProduct;

import java.util.List;

@Repository
public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Long> {

    List<PurchasedProduct> findAll();
}
