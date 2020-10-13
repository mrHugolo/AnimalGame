package com.company;

import java.util.*;

public abstract class Animal {
    Random rand = new Random();

    protected String name;
    //0 = male, 1 = female
    protected int gender;
    protected int health;
    protected double MAX_AGE = 30;
    protected int age;
    protected double MAX_PRICE = 50000;
    protected double price;
    protected int numberOfPossibleBabies = 2;
    protected int veterinarianCost = 50000;
    protected ArrayList<Integer> foodsICanEat = new ArrayList<>();
    protected int howMuchFoodICanEat = 10;
    protected int howMuchFoodIAteToday;
    protected boolean loseHp = true;
    protected boolean isSick = false;
    protected boolean isDead = false;
    protected int chanceOfDeath = 20;

    public Animal(String name, int gender) {
        this.name = name;
        this.gender = gender;
        health = 100;
        age = 0;
        howMuchFoodIAteToday = 0;
    }

    public void loseHealth() {
        health -= rand.nextInt(21) + 10;
    }

    public void isSick() {
        int chanceOfSickness = (int) ((70 / (health + 1)) * (10 + age * (30 / MAX_AGE)));
        isSick = (int) ((Math.random() * 100) + 1) <= chanceOfSickness;
    }

    public String showFoodsICanEat() {
        StringBuilder list = new StringBuilder();
        for (int food : foodsICanEat) {
            list.append(Mall.foodList[food].getClass().getSimpleName()).append(" ");
        }
        return list.toString();
    }

    public boolean endOfTurn(){
        if(++age > MAX_AGE) isDead = true;
        howMuchFoodIAteToday = 0;
        if (loseHp) loseHealth();
        if(health <= 0) isDead = true;
        loseHp = true;
        isSick();
        return isDead;
    }

    public String getName(){ return name; }

    public String getGenderString() {
        return gender == 0 ? "male" : "female";
    }

    public int calculatePrice() {
        //price increase linearly the first 1/3 of its maximum lifespan
        //but price decrease linearly the last 2/3 of its maximum lifespan
        price = (age <= MAX_AGE / 3 ? MAX_PRICE * ((3 * age) / (5 * MAX_AGE) + 0.8)
                : MAX_PRICE * (1.5 - (3 * age) / (2 * MAX_AGE)));
        price *= ((double) health / 100);
        return (int) price;
    }

}
