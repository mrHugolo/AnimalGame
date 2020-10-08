package com.company;

import java.util.ArrayList;

public class Kangaroo extends Animal {

    public Kangaroo(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 6;
        super.MAX_PRICE = 10000;
        super.numberOfPossibleBabies = 3;
        super.veterinarianCost = 1000;
        super.chanceOfDeath = 0;
        super.howMuchFoodICanEat = 4;
        super.foodsICanEat = new ArrayList<Integer>();
        foodsICanEat.add(0);  //Fruit
        foodsICanEat.add(4);  //Grass
    }
}
