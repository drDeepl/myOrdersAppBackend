package ru.myorder.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.myorder.models.PurchasedProduct;
import ru.myorder.repositories.PurchasedProductRepository;

import java.util.List;

@Service
public class PurchasedProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(PurchasedProductService.class);

    @Autowired
    private PurchasedProductRepository purchasedProductRepository;

    public List<PurchasedProduct> getPurchasedProducts(){
        LOGGER.info("GET PURCHASED PRODUCTS");
        return purchasedProductRepository.findAll();
    }

}
