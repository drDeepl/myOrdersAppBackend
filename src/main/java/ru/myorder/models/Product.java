package ru.myorder.models;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.myorder.models.serializers.ProductSerializer;

@Entity
@Data
@NoArgsConstructor
@Table(name="products")
@JsonSerialize(using= ProductSerializer.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", nullable = false, unique = true)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;


    public Product(String name, Category category){
        this.name = name;
        this.category = category;
    }
}
