package com.company;

public abstract class Animal {

    private String name;
    //0 = male, 1 = female
    private int gender;
    private int health;

    public Animal(String name, int gender){
        this.name = name;
        this.gender = gender;
        health = 100;
    }

}
