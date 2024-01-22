package ru.myorder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.myorder.models.PurchasedProduct;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PurchasedProductRepository extends JpaRepository<PurchasedProduct, Long> {

    List<PurchasedProduct> findAll();

    @Query(value = "SELECT * FROM purchased_products WHERE purchase_date = :timestamp", nativeQuery = true)
    List<PurchasedProduct> getPurchasedProductsByTimestamp(@Param("timestamp")Timestamp timestamp);

    @Query(value = "SELECT * FROM purchased_products WHERE user_id = :user_id LIMIT :offset", nativeQuery = true)
    List<PurchasedProduct> findAllByUserId(@Param("user_id") Long userId, @Param("offset") int offset);
}
