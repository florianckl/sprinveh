package com.example.microserviceMoto.controller;

import com.example.microserviceMoto.model.Moto;
import com.example.microserviceMoto.controller.MotoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class MotoModelAssembler implements RepresentationModelAssembler<Moto, EntityModel<Moto>> {

    @Override
    public EntityModel<Moto> toModel(Moto moto) {

        return EntityModel.of(moto,
                linkTo(methodOn(MotoController.class).one(moto.getId())).withSelfRel(),
                linkTo(methodOn(MotoController.class).all()).withRel("motos"));
    }
}