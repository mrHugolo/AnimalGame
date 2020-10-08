package com.company;

import java.util.ArrayList;

public class Crocodile extends Animal {

    public Crocodile(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 24;
        super.MAX_PRICE = 15000;
        super.numberOfPossibleBabies = 10;
        super.veterinarianCost = 5000;
        super.chanceOfDeath = 0;
        super.howMuchFoodICanEat = 3;
        super.foodsICanEat = new ArrayList<Integer>();
        foodsICanEat.add(3);  //Fish
    }
}
