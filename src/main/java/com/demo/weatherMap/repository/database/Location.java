package com.demo.weatherMap.repository.database;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Location implements Serializable {

    private String city;

    private String country;
}
