package com.company;

import java.util.*;

public abstract class Animal {
    Random rand = new Random();

    protected String name;
    //0 = male, 1 = female
    protected int gender;
    protected int health;
    protected int MAX_AGE = 30;
    protected int age;
    protected int MAX_PRICE = 50000;
    protected int price;
    protected int numberOfPossibleBabies = 2;
    protected int veterinarianCost = 50000;
    protected ArrayList<Integer> foodsICanEat = new ArrayList<>();

    public Animal(String name, int gender) {
        this.name = name;
        this.gender = gender;
        health = 100;
        age = 0;

    }

    public void loseHealth(){
        health -= rand.nextInt(21) + 10;
    }

    public boolean isSick(){
        return rand.nextInt(5) % 5 == 0;

    }

    public String showFoodsICanEat(){
        String list = "";
        for(int food : foodsICanEat) {
            list += Store.foodList[food].getClass().getSimpleName() + " ";
        }
        return list;
    }

    public String getGenderString() {
        return gender == 0 ? "male" : "female";
    }

    public int calculatePrice() {
        //price increase linearly the first 1/3 of its maximum lifespan
        //but price decrease linearly the last 2/3 of its maximum lifespan
        price = (int) (age <= MAX_AGE / 3 ? MAX_PRICE * ((3 * age) / (5 * MAX_AGE) + 0.8)
                        : MAX_PRICE * (1.5 - (3 * age) / (2 * MAX_AGE)));
        return (int) price;
    }


}
