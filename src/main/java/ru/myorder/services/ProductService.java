package ru.myorder.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.myorder.models.Category;
import ru.myorder.models.Product;
import ru.myorder.payloads.AddProductRequest;
import ru.myorder.repositories.CategoryRepository;
import ru.myorder.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts(){
        LOGGER.info("GET ALL PRODUCTS");
        return productRepository.findAll();
    }

    public Product addProduct(AddProductRequest addProductRequest){
        LOGGER.info("ADD PRODUCT");
        Category category = categoryRepository.getReferenceById(addProductRequest.getCategoryId());
        Product product = new Product(addProductRequest.getName(), category);
        return productRepository.save(product);
    }
}
