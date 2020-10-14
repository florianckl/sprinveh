package com.example.microserviceMoto.repository;

import com.example.microserviceMoto.model.Moto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MotoRepository extends CrudRepository<Moto, Long> {
    List<Moto> findByOrderByPrix();
    List<Moto> findByOrderByMarque();


}