package com.company;

import java.util.*;

public abstract class Animal {
    Scanner scan = new Scanner(System.in);

    private String name;
    //0 = male, 1 = female
    private int gender;
    private int health;
    private double MAX_AGE = 30;
    private int age;
    private double MAX_PRICE = 50000;
    private double price;
    private Player owner;
    private boolean isAlive = true;

    public Animal(String name, int gender) {
        this.name = name;
        this.gender = gender;
        health = 100;
        age = 0;
    }

    public void mate(Animal animal){
        int tryToMakeABaby = !this.getClass().getSimpleName().equals(animal.getClass().getSimpleName())
                ? 0 : this.gender == animal.getGender() ? 0 : (int) (Math.random() * 2);
        if(tryToMakeABaby == 1){
            int genderReveal = (int) (Math.random() * 2);
            System.out.println("Congratulation! It's a " + (genderReveal == 0 ? "male" : "female")
                    + ". What would you like to name your " + this.getClass().getSimpleName().toLowerCase() + "?");
            owner.getAnimals().add(Store.createAnimal(this.getClass().getSimpleName(), scan.nextLine(), genderReveal));
        }
        else System.out.println("No baby for you!");
    }

    public void die(){
        isAlive = false;
    }

    public void beBought(Player owner){
        if(this.owner == owner){
            return;
        }

        this.owner = owner;
        owner.buyAnimal(this);
    }

    public void changeMaxAge(int maxAge){
        MAX_AGE = maxAge;
    }

    public void changeMaxPrice(int maxPrice){
        MAX_PRICE = maxPrice;
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

    public Player getOwner(){
        return owner;
    }

}
