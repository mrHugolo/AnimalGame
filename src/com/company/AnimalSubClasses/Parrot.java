package com.company.AnimalSubClasses;

import com.company.Animal;

import java.util.ArrayList;

public class Parrot extends Animal {

    public Parrot(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 18;
        super.MAX_PRICE = 10000;
        super.numberOfPossibleBabies = 4;
        super.veterinarianCost = 2000;
        super.chanceOfDeath = 10;
        super.howMuchFoodICanEat = 1;
        super.foodsICanEat = new ArrayList<>();
        foodsICanEat.add(0);  //Fruit
        foodsICanEat.add(1);  //Berries
        foodsICanEat.add(2);  //Nuts
    }
}
