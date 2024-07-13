package com.example.DA.model.enums_entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "UtilityType")
@Getter
@Setter
public class UtilityType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer utilityTypeId;

    private String utilityTypeName;
}
