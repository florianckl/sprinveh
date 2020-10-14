package com.example.microserviceVoiture.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import javax.persistence.Entity;


@Entity
public class Car {

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private String type;
    private @Id
    @GeneratedValue
    Long id;
    private String marque;
    private String nom;
    private int prix;
    private String image;

    Car() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Car(String marque, String nom, int prix,String image) {
        this.type="voiture";
        this.image=image;
        this.marque = marque;
        this.nom = nom;
        this.prix = prix;
    }

    public Car(String type, String marque, String nom, int prix,String image) {
        this.type=type;
        this.image=image;
        this.marque = marque;
        this.nom = nom;
        this.prix = prix;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof com.example.microserviceVoiture.model.Car))
            return false;
        com.example.microserviceVoiture.model.Car car = (com.example.microserviceVoiture.model.Car) o;
        return Objects.equals(this.id, car.id) && Objects.equals(this.marque, car.marque)
                && Objects.equals(this.nom, car.nom) && Objects.equals(this.prix, car.prix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.marque, this.nom, this.prix);
    }

    @Override
    public String toString() {
        return "Voiture{" + "id=" + this.id + ", marque='" + this.marque + '\'' + ", nom='" + this.nom
                + '\'' + ", prix='" + this.prix + '\'' + '}';
    }
}