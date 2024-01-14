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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.myorder.config.jwt.JwtUtils;
import ru.myorder.dtos.JwtDTO;
import ru.myorder.dtos.MessageDTO;
import ru.myorder.dtos.ProductDTO;
import ru.myorder.dtos.PurchasedProductDTO;
import ru.myorder.exceptions.CategoryAlreadyExists;
import ru.myorder.exceptions.CategoryException;
import ru.myorder.models.Category;
import ru.myorder.models.Product;
import ru.myorder.payloads.AddCategoryRequest;
import ru.myorder.payloads.AddProductRequest;
import ru.myorder.payloads.EditCategoryRequest;
import ru.myorder.services.CategoryService;
import ru.myorder.services.ProductService;

import java.util.List;

@Tag(name="ProductController")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    JwtUtils jwtUtils;


    @Operation(summary="получение списка категорий")
    @GetMapping("/category/all")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema=@Schema(implementation = Category.class)))})
    public ResponseEntity<?> getAllCategories(){
        LOGGER.info("GET ALL CATEGORIES");

        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @Operation(summary="добавление категории для продукта")
    @PostMapping("/category/add")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = Category.class))})
    @ApiResponse(responseCode = "403", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = ResponseEntity.class))})
    public ResponseEntity<?> addCategoryProduct(@RequestBody AddCategoryRequest addCategoryRequest){
        LOGGER.info("ADD CATEGORY PRODUCT");
        Category addedCategory =categoryService.addCategory(addCategoryRequest.getName());
        if(addedCategory != null){
            return ResponseEntity.ok(addedCategory);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("ошибка при добавлении категории"));
    }


    @Operation(summary="редактирование категории для продукта")
    @PutMapping("/category/edit/{category_id}")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = Category.class))})
    @ApiResponse(responseCode = "403", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = ResponseEntity.class))})
    @ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = ResponseEntity.class))})
    public ResponseEntity<?> editCategory(@PathVariable("category_id") Long categoryId, @RequestBody EditCategoryRequest editCategoryRequest){
        LOGGER.info("EDIT CATEGORY");
        try{
            Category editedCategory = categoryService.editCategory(categoryId, editCategoryRequest);
            return ResponseEntity.ok(editedCategory);
        }
        catch (CategoryAlreadyExists cae){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO(cae.getMessage()));
        }
        catch (CategoryException ce){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO(ce.getMessage()));
        }

    }




    @Operation(summary="удаление категории для продукта")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<MessageDTO> deleteCategoryById(@PathVariable("id") Long id){
        LOGGER.info("DELETE CATEGORY BY ID");
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(new MessageDTO("категория успешно удалена"));

    }

    @Operation(summary="получение списка продуктов")
    @GetMapping("/all")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", array=@ArraySchema(schema=@Schema(implementation = ProductDTO.class)))})
    public ResponseEntity<?> getAllProducts(){
        LOGGER.info("GET ALL PRODUCT");
        return ResponseEntity.ok(productService.getAllProducts());
    }



    @Operation(summary="добавление продукта")
    @PostMapping("/add")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = Product.class))})
    @ApiResponse(responseCode = "403", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = ResponseEntity.class))})
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest addProductRequest){
        LOGGER.info("ADD PRODUCT");
        try {
            Product addedProduct = productService.addProduct(addProductRequest);
            return ResponseEntity.ok(addedProduct);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("ошибка при добавлении продукта"));
        }


    }

}
