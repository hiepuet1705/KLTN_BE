package com.example.DA.model;

import com.example.DA.model.enums_entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Properties")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Property extends DateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propertyId;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private PropertyStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String title;
    private String description;
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String location;

    @ManyToOne
    @JoinColumn(name = "phuong_id")
    private Phuong phuong;


    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @ManyToMany
    @JoinTable(
            name = "PropertyUtilities",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "utility_id")
    )
    private Set<Utility> utilities;

    // Getters and setters
}

