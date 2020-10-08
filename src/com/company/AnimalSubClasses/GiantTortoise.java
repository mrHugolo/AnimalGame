package com.company.AnimalSubClasses;

import com.company.Animal;

import java.util.ArrayList;

public class GiantTortoise extends Animal {


    public GiantTortoise(String name, int gender) {
        super(name, gender);
        super.MAX_AGE = 30;
        super.MAX_PRICE = 12000;
        super.numberOfPossibleBabies = 8;
        super.veterinarianCost = 4000;
        super.chanceOfDeath = 50;
        super.howMuchFoodICanEat = 5;
        super.foodsICanEat = new ArrayList<>();
        foodsICanEat.add(0);  //Fruit

    }
}
