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

    @Query(value = "SELECT * FROM purchased_products WHERE purchase_date >= :startDayDate AND purchase_date <= :endDayDate AND user_id = :userId", nativeQuery = true)
    List<PurchasedProduct> getPurchasedProductsByTimestampUserId(@Param("startDayDate") Timestamp startDayDate, @Param("endDayDate") Timestamp endDayDate, @Param("userId") Long userId);

    @Query(value = "SELECT * FROM purchased_products WHERE user_id = :user_id LIMIT :offset", nativeQuery = true)
    List<PurchasedProduct> findAllByUserId(@Param("user_id") Long userId, @Param("offset") int offset);
}
