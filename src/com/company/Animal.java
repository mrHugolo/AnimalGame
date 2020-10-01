package com.company;

import java.util.*;

public abstract class Animal {

    protected String name;
    //0 = male, 1 = female
    protected int gender;
    protected int health;
    protected double MAX_AGE = 30;
    protected int age;
    protected double MAX_PRICE = 50000;
    protected double price;
    protected int numberOfPossibleBabies;
    protected ArrayList<Integer> foodsICanEat = new ArrayList<>();

    public Animal(String name, int gender) {
        this.name = name;
        this.gender = gender;
        health = 100;
        age = 0;
        numberOfPossibleBabies = 4;

    }

    public String getName() {
        return name;
    }

    public String getGenderString() {
        return getGender() == 0 ? "male" : "female";
    }

    public int getGender() {
        return gender;
    }

    public int getHealth() {
        return health;
    }

    public int getAge() {
        return age;
    }

    public int getPrice() {
        //price increase linearly the first 1/3 of its maximum lifespan
        //but price decrease linearly the last 2/3 of its maximum lifespan
        price = age <= MAX_AGE / 3 ? MAX_PRICE * ((3 * age) / (5 * MAX_AGE) + 0.8)
                : MAX_PRICE * (1.5 - (3 * age) / (2 * MAX_AGE));
        return (int) price;
    }


}
