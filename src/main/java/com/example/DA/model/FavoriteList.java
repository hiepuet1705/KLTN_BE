package com.example.DA.model;

import com.example.DA.model.DateTime;
import com.example.DA.model.Property;
import com.example.DA.model.User;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Favorite_List")
@Getter
@Setter
public class FavoriteList extends DateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "added_date")
    private LocalDateTime addedDate;


}

