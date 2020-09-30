package com.company;

public class Main {

    public static void main(String[] args) {
		Player hugo = new Player("Hugo");
	    hugo.buyAnimal(Store.createAnimal("Bear","Yogi", 0));
	    hugo.buyAnimal(Store.createAnimal("Bear","Brumme-Lisa", 1));
		hugo.getAnimals().get(0).mate(hugo.getAnimals().get(1));
		System.out.println(hugo.getAnimals());



    }
}
