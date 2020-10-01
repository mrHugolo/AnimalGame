package com.company;

import java.util.*;

public class Bear extends Animal{


    public Bear(String name, int gender){
        super(name, gender);
        super.MAX_AGE = 12;
        super.MAX_PRICE = 10000;
        super.foodsICanEat = new ArrayList<Integer>();
        foodsICanEat.add(0);
    }


}
