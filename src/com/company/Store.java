package com.company;

import java.util.LinkedHashMap;

public class Store {

    protected static Food[] foodList = {new Carrot(), new Steak()};
    protected static Animal[] animalList = {new Bear("Bear", 0),
            new Snake("Snake", 0)};

    public Store(){




    }

    public static Animal createAnimal(String animalClass, String name, int gender){
        switch (animalClass) {
            case "Bear" -> {return new Bear(name, gender);}
            case "Snake" -> {return new Snake(name, gender);}
            case "Bear3" -> {return new Bear(name + " ", gender);}
            case "Bear4" -> {return new Bear(name + "  ", gender);}
            case "Bear5" -> {return new Bear(name + "   ", gender);}
            case "Bear6" -> {return new Bear(name + "    ", gender);}
            default -> {return null;}
        }
    }








}
