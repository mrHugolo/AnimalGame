package com.company;

import java.util.*;

public class Bear extends Animal{


    public Bear(String name, int gender){
        super(name, gender);
        super.MAX_AGE = 12;
        super.MAX_PRICE = 10000;
        super.numberOfPossibleBabies = 5;
        super.veterinarianCost = 1000;
        super.chanceOfDeath = 0;
        super.howMuchFoodICanEat = 10;
        super.foodsICanEat = new ArrayList<>();
        foodsICanEat.add(1);  //Berries
        foodsICanEat.add(3);  //Fish
    }


}
