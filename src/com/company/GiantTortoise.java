package com.company;

import java.util.ArrayList;

public class GiantTortoise extends Animal {


    public GiantTortoise(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 30;
        super.MAX_PRICE = 10000;
        super.numberOfPossibleBabies = 8;
        super.veterinarianCost = 1000;
        super.chanceOfDeath = 0;
        super.howMuchFoodICanEat = 5;
        super.foodsICanEat = new ArrayList<Integer>();
        foodsICanEat.add(0);  //Fruit

    }
}
