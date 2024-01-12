package ru.myorder.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.myorder.services.PurchasedProductService;

@Tag(name="PurchasedProductController")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/purchased_product")
public class PurchasedProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchasedProductController.class);

    @Autowired
    private PurchasedProductService purchasedProductService;




}
