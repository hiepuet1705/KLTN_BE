package com.example.DA.model.enums_entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PropertyStatus")
@Setter
@Getter
public class PropertyStatus {
    // Dang thi cong, da ban
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusId;

    private String statusName;


    // Getters and setters
}
