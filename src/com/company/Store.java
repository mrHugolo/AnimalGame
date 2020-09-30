package com.company;

public class Store {

    public static Animal createAnimal(String animalClass, String name, int gender){
        if(animalClass.equals("Bear")) return new Bear(name, gender);
        return null;
    }

    public static Food createFood(String foodClass){
        if(foodClass.equals("Carrot")) return new Carrot();
        return null;

    }

}
