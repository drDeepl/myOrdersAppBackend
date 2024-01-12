package ru.myorder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.myorder.dtos.JwtDTO;
import ru.myorder.dtos.PurchasedProductDTO;
import ru.myorder.models.MeasurementUnit;
import ru.myorder.models.PurchasedProduct;
import ru.myorder.services.PurchasedProductService;

import java.util.List;

@Tag(name="PurchasedProductController")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/purchased_product")
public class PurchasedProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PurchasedProductController.class);

    @Autowired
    private PurchasedProductService purchasedProductService;

    @Operation(summary="получение списка всех купленных товаров")
    @PostMapping("/all")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array=@ArraySchema(schema=@Schema(implementation = PurchasedProductDTO.class)))})
    public ResponseEntity<List<PurchasedProduct>> getPurchasedProducts(){
        LOGGER.info("GET PURCHASED PRODUCTS");
        List<PurchasedProduct> purchasedProducts = purchasedProductService.getPurchasedProducts();
        return ResponseEntity.ok(purchasedProducts);
    }





}
