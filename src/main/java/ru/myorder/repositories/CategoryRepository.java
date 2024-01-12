package ru.myorder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.myorder.models.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
    Boolean existsByName(String name);




}
