package com.demo.weatherMap.repository.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@IdClass(Location.class)
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    @Id
    private String city;

    @Id
    private String country;

    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String description;

}
