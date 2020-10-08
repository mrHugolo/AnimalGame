package com.company;

import java.util.ArrayList;

public class Goat extends Animal {

    public Goat(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 9;
        super.MAX_PRICE = 10000;
        super.numberOfPossibleBabies = 2;
        super.veterinarianCost = 1000;
        super.chanceOfDeath = 0;
        super.howMuchFoodICanEat = 2;
        super.foodsICanEat = new ArrayList<>();
        foodsICanEat.add(4);  //Grass
    }
}
