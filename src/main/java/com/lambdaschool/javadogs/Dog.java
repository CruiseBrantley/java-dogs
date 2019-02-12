package com.lambdaschool.javadogs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data // creates getters, setters, toString
@Entity // object ready JPA storage
public class Dog
{
    private @Id
    @GeneratedValue
    Long id; // primary key automatically populated
    private String breed;
    private int weight;
    private boolean apartment;

    // needed for JPA
    public Dog()
    {
        // default constructor
    }

    public Dog(String breed, int weight, boolean apartment)
    {
        this.breed = breed;
        this.weight = weight;
        this.apartment = apartment;
    }
}