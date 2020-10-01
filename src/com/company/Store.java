package com.company;

import java.util.LinkedHashMap;

public class Store {

    protected static LinkedHashMap<String, Integer> defaultFoodList = new LinkedHashMap<>();
    protected static Food[] foodList = {new Carrot()};

    public Store(){
        defaultFoodList.put("Carrot", 0);
        //  defaultFoodList.put("Carrot1", 0);
        //  defaultFoodList.put("Carrot2", 0);
        //  defaultFoodList.put("Carrot3", 0);
        //  defaultFoodList.put("Carrot4", 0);
        //  defaultFoodList.put("Carrot5", 0);


    }

    public static Animal createAnimal(String animalClass, String name, int gender){
        switch (animalClass) {
            case "Bear" -> {return new Bear(name, gender);}
            case "Bear2" -> {return new Bear(name + "", gender);}
            case "Bear3" -> {return new Bear(name + " ", gender);}
            case "Bear4" -> {return new Bear(name + "  ", gender);}
            case "Bear5" -> {return new Bear(name + "   ", gender);}
            case "Bear6" -> {return new Bear(name + "    ", gender);}
            default -> {return null;}
        }
    }








}
