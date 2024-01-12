package ru.myorder.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.myorder.exceptions.CategoryAlreadyExists;
import ru.myorder.exceptions.CategoryException;
import ru.myorder.models.Category;
import ru.myorder.payloads.EditCategoryRequest;
import ru.myorder.repositories.CategoryRepository;

@Service
public class CategoryService {

    private final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;


    public Category addCategory(String nameCategory){
        Category category = new Category();
        category.setName(nameCategory.strip().toLowerCase());
        try{
            return categoryRepository.save(category);

        }
        catch (RuntimeException re){
            LOGGER.error(re.getMessage());
            return  null;
        }
    }

    public Category editCategory(Long categoryId, EditCategoryRequest editCategoryRequest){
        LOGGER.info("EDIT CATEGORY");
        String nameCategory = editCategoryRequest.getName().toString().strip().toLowerCase();
        if(categoryRepository.existsByName(nameCategory)){
            throw new CategoryAlreadyExists("Данная категория уже существует");
        }
        else{
            Category category = categoryRepository.findById(categoryId).orElseThrow(() ->new CategoryException("Категории с данным id не существует"));
            category.setName(nameCategory);
            return categoryRepository.save(category);
        }

    }

    public void deleteCategoryById(Long id){
        LOGGER.info("DELETE CATEGORY BY ID");
        categoryRepository.deleteById(id);
    }


}
