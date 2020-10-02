package com.company;

import java.util.ArrayList;

public class Snake extends Animal{

    public Snake(String name, int gender){
        super(name, gender);
        super.MAX_AGE = 6;
        super.MAX_PRICE = 20000;
        super.numberOfPossibleBabies = 10;
        super.veterinarianCost = 2000;
        super.foodsICanEat = new ArrayList<Integer>();
        foodsICanEat.add(0);
    }
}
