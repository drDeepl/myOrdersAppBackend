package ru.myorder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.myorder.dtos.JwtDTO;
import ru.myorder.dtos.MessageDTO;
import ru.myorder.dtos.PurchasedProductDTO;
import ru.myorder.models.MeasurementUnit;
import ru.myorder.models.PurchasedProduct;
import ru.myorder.payloads.AddPurchasedProductRequest;
import ru.myorder.payloads.EditPurchasedProductRequest;
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
    @GetMapping("/all")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array=@ArraySchema(schema=@Schema(implementation = PurchasedProductDTO.class)))})
    public ResponseEntity<List<PurchasedProduct>> getPurchasedProducts(){
        LOGGER.info("GET PURCHASED PRODUCTS");
        List<PurchasedProduct> purchasedProducts = purchasedProductService.getPurchasedProducts();
        return ResponseEntity.ok(purchasedProducts);
    }

    @Operation(summary="добавление купленного товара")
    @PostMapping("/add")
    public ResponseEntity<MessageDTO> addPurchasedProduct(@RequestBody AddPurchasedProductRequest addPurchasedProductRequest){
        LOGGER.info("ADD PURCHASED PRODUCT");
        try{
            purchasedProductService.addPurchasedProduct(addPurchasedProductRequest);
            return ResponseEntity.ok(new MessageDTO("купленный товар успешно добавлен"));
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("ошибка при добавлении купленного товара"));
        }
    }

    @Operation(summary="редактирование купленного товара")
    @PostMapping("/edit/{purchased_product_id}")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array=@ArraySchema(schema=@Schema(implementation = PurchasedProductDTO.class)))})
    public ResponseEntity<?> editPurchasedProduct(@PathVariable("purchased_product_id") Long purchasedProductId,@RequestBody EditPurchasedProductRequest editPurchasedProductRequest){
        LOGGER.info("EDIT PURCHASED PRODUCT");
        LOGGER.info(purchasedProductId.toString());
        try{
            return ResponseEntity.ok(purchasedProductService.editPurchasedProduct(purchasedProductId, editPurchasedProductRequest));
        }
        catch (RuntimeException re){
            LOGGER.error(re.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("ошибка редактирования"));

        }
    }

    @Operation(summary="удаление купленного товара")
    @DeleteMapping("/delete/{purchased_product_id}")
    public ResponseEntity<MessageDTO> deletePurchasedProductByid(@PathVariable("purchased_product_id") Long purchasedProductId){
        LOGGER.info("DELETE PURCHASED PRODUCT ID");
        try{
            purchasedProductService.deletePurchasedProduct(purchasedProductId);
            return ResponseEntity.ok(new MessageDTO("купленный товар удален"));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("что-то пошло не так"));

        }


    }








}
