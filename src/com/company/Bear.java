package com.company;

public class Bear extends Animal{


    public Bear(String name, int gender){
        super(name, gender);
        super.changeMaxAge(12);
        super.changeMaxPrice(10000);
    }


}
