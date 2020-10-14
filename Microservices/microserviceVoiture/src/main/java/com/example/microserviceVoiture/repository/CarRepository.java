package com.example.microserviceVoiture.repository;

import com.example.microserviceVoiture.model.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByOrderByPrix();
    List<Car> findByOrderByMarque();


}