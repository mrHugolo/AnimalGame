package com.company;

import java.util.*;

public class Player {

    private String name;
    private int money;
    private ArrayList<Animal> animals;

    public Player(String name){
        this.name = name;
        money = 50000;
    }
    //***Right now there are no way to tell a customer if they doesn't have enough money***
    public void buyAnimal(Animal animal){
        if(animals.contains(animal)){
            return;
        }

        animals.add(animal);
        this.increaseMoney(animal.getPrice() * -1);
        animal.beBought(this);
    }

    public void buyAnimalFromPlayer(Animal animal, Player player){
        player.animals.remove(animal);
        player.increaseMoney(animal.getPrice());
        animal.beBought(this);
    }

    public void sellAnimal(Animal animal){
        animals.remove(animal);
        this.increaseMoney(animal.getPrice());
        animal.die();
    }

    public void sellAnimalToPlayer(Animal animal, Player player){
        player.buyAnimalFromPlayer(animal, this);
    }

    public void increaseMoney(int addedMoney){
        this.money += addedMoney;
    }

    public int getMoney(){
        return money;
    }


}
