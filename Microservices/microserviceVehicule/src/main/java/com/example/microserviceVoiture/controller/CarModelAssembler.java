package com.example.microserviceVoiture.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CarModelAssembler implements RepresentationModelAssembler<com.example.microserviceVoiture.model.Car, EntityModel<com.example.microserviceVoiture.model.Car>> {

    @Override
    public EntityModel<com.example.microserviceVoiture.model.Car> toModel(com.example.microserviceVoiture.model.Car car) {

        return EntityModel.of(car,
                linkTo(methodOn(CarController.class).one(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).all()).withRel("cars"));
    }
}