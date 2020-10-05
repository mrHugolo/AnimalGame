package com.company;



public class Store {

    protected static Food[] foodList = {
            new Fruit(),
            new Berries(),
            new Nuts(),
            new Fish(),
            new Grass()
    };

    protected static Animal[] animalList = {
            new GiantTortoise("GiantTortoise", 0),
            new Crocodile("Crocodile", 0),
            new Parrot("Parrot", 0),
            new Bear("Bear", 0),
            new Goat("Goat", 0),
            new Kangaroo("Kangaroo", 0)
            };

    public Store(){
    }

    public static Animal createAnimal(String animalClass, String name, int gender){
        switch (animalClass) {
            case "GiantTortoise" -> {return new GiantTortoise(name, gender);}
            case "Crocodile" -> {return new Crocodile(name, gender);}
            case "Parrot" -> {return new Parrot(name, gender);}
            case "Bear" -> {return new Bear(name, gender);}
            case "Goat" -> {return new Goat(name, gender);}
            case "Kangaroo" -> {return new Kangaroo(name, gender);}
            default -> {return null;}
        }
    }








}
