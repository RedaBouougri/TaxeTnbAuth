package com.example.taxetnbauth.security.springjwt.models;

import javax.persistence.*;


public class Terain {


	private int id;
	private String nom;
	private String description;
	private double surface;


	private Redevable redevable;

	private Category category;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSurface() {
		return surface;
	}

	public void setSurface(double surface) {
		this.surface = surface;
	}

	public Redevable getRedevable() {
		return redevable;
	}

	public void setRedevable(Redevable redevable) {
		this.redevable = redevable;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

}
