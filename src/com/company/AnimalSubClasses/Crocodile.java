package com.company.AnimalSubClasses;

import com.company.Animal;

import java.util.ArrayList;

public class Crocodile extends Animal {

    public Crocodile(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 24;
        super.MAX_PRICE = 20000;
        super.numberOfPossibleBabies = 10;
        super.veterinarianCost = 5000;
        super.chanceOfDeath = 67;
        super.howMuchFoodICanEat = 3;
        super.foodsICanEat = new ArrayList<>();
        foodsICanEat.add(3);  //Fish
    }
}
