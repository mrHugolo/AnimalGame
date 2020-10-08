package com.company.AnimalSubClasses;

import com.company.Animal;

import java.util.*;

public class Bear extends Animal {


    public Bear(String name, int gender){
        super(name, gender);
        super.MAX_AGE = 12;
        super.MAX_PRICE = 10000;
        super.numberOfPossibleBabies = 5;
        super.veterinarianCost = 3000;
        super.chanceOfDeath = 33;
        super.howMuchFoodICanEat = 10;
        super.foodsICanEat = new ArrayList<>();
        foodsICanEat.add(1);  //Berries
        foodsICanEat.add(3);  //Fish
    }


}
