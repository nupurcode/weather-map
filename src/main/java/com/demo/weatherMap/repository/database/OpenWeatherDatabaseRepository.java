package com.demo.weatherMap.repository.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenWeatherDatabaseRepository extends JpaRepository<Weather, Location> {
}
