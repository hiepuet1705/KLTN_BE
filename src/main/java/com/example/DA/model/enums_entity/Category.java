package com.example.DA.model.enums_entity;

import com.example.DA.model.DateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Categories")
@Getter
@Setter
public class Category {
    // Loai BDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(unique = true)
    private String categoryName;
}

