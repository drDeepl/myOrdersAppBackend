package ru.myorder.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.myorder.models.serializers.ProductSerializer;

@Entity
@Data
@NoArgsConstructor
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, unique = true)
    private String name;

    @JsonIgnore
    @OneToOne(mappedBy = "category")
    private Product product;

}