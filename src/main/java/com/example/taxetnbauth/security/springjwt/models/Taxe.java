package com.example.taxetnbauth.security.springjwt.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;


public class Taxe implements Serializable {


    private int id;
    private int tnbAnnee;
    private String description;
    private double montantBase;


    private Category category;

    private Redevable redevable;

    private Terain terain;

    private Taux taux;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getTnbAnnee() {
        return tnbAnnee;
    }
    public void setTnbAnnee(int tnbAnnee) {
        this.tnbAnnee = tnbAnnee;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getMontantBase() {
        return montantBase;
    }
    public void setMontantBase(double montantBase) {
        this.montantBase = montantBase;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public Redevable getRedevable() {
        return redevable;
    }
    public void setRedevable(Redevable redevable) {
        this.redevable = redevable;
    }
    public Terain getTerain() {
        return terain;
    }
    public void setTerain(Terain terain) {
        this.terain = terain;
    }
    public Taux getTaux() {
        return taux;
    }
    public void setTaux(Taux taux) {
        this.taux = taux;
    }



}

