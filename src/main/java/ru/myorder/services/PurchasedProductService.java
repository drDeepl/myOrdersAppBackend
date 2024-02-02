package ru.myorder.services;

import org.checkerframework.checker.units.qual.A;
import org.hibernate.annotations.NotFound;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.myorder.exceptions.NotFoundException;
import ru.myorder.models.MeasurementUnit;
import ru.myorder.models.Product;
import ru.myorder.models.PurchasedProduct;
import ru.myorder.models.User;
import ru.myorder.payloads.AddPurchasedProductRequest;
import ru.myorder.payloads.EditPurchasedProductRequest;
import ru.myorder.repositories.MeasurementUnitRepository;
import ru.myorder.repositories.ProductRepository;
import ru.myorder.repositories.PurchasedProductRepository;
import ru.myorder.repositories.UserRepository;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;

@Service
public class PurchasedProductService {

    private final Logger LOGGER = LoggerFactory.getLogger(PurchasedProductService.class);
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PurchasedProductRepository purchasedProductRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    public List<PurchasedProduct> getPurchasedProducts(Long userId, int offset){
        LOGGER.info("GET PURCHASED PRODUCTS");
        return purchasedProductRepository.findAllByUserId(userId, offset);
    }

    public PurchasedProduct addPurchasedProduct(AddPurchasedProductRequest addPurchasedProductRequest, Long userId){
        LOGGER.info("ADD PURCHASED PRODUCT");
        User user = userRepository.getReferenceById(userId);
        Product product = productRepository.getReferenceById(addPurchasedProductRequest.getProductId());
        MeasurementUnit measurementUnit = measurementUnitRepository.getReferenceById(addPurchasedProductRequest.getUnitMeasurement());
        PurchasedProduct purchasedProduct = new PurchasedProduct(user, product, addPurchasedProductRequest.getCount(), addPurchasedProductRequest.getPrice(), measurementUnit, addPurchasedProductRequest.getPurchasedDatetime());
        return purchasedProductRepository.save(purchasedProduct);
    }

    public PurchasedProduct editPurchasedProduct(Long purchaseProductId, EditPurchasedProductRequest editPurchasedProductRequest){
        LOGGER.info("EDIT PURCHASED PRODUCT");
        PurchasedProduct editPurchaseProduct = purchasedProductRepository.findById(purchaseProductId).orElseThrow(() ->new NotFoundException("товар не найден"));
        Product product = productRepository.getReferenceById(editPurchasedProductRequest.getProductId());
        MeasurementUnit measurementUnit = measurementUnitRepository.getReferenceById(editPurchasedProductRequest.getMeasurementUnitId());
        editPurchaseProduct.setProduct(product);
        editPurchaseProduct.setCount(editPurchasedProductRequest.getCount());
        editPurchaseProduct.setMeasurementUnit(measurementUnit);
        editPurchaseProduct.setPrice(editPurchasedProductRequest.getPrice());
        editPurchaseProduct.setPurchaseDate(editPurchasedProductRequest.getPurchasedDatetime());
        return purchasedProductRepository.save(editPurchaseProduct);
    }

    public void deletePurchasedProduct(Long id){
        LOGGER.info("DELETE PURCHASE PRODUCT ID");
        purchasedProductRepository.deleteById(id);
    }

    public List<PurchasedProduct> getPurchasedProductsByTimestampUserId(Timestamp timestamp, Long userId){
        LOGGER.info("GET PURCHASED PRODUCT BY TIMESTAMP");
        String date = timestamp.toString().split(" ")[0];
        Timestamp startDayDate = Timestamp.valueOf( date+ " 00:00:00");
        Timestamp endDayDate = Timestamp.valueOf(date + " 23:59:59.9");
        return purchasedProductRepository.getPurchasedProductsByTimestampUserId(startDayDate, endDayDate, userId);
    }

}
