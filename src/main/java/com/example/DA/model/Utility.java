package com.example.DA.model;

import com.example.DA.model.enums_entity.District;
import com.example.DA.model.enums_entity.Phuong;
import com.example.DA.model.enums_entity.Province;
import com.example.DA.model.enums_entity.UtilityType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "Utilities")
@Getter
@Setter
public class Utility extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer utilityId;

    @ManyToOne
    @JoinColumn(name = "utility_type_id")
    private UtilityType utilityType;

    private String utilityName;
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

    @ManyToMany(mappedBy = "utilities")
    private Set<Property> properties;
}

