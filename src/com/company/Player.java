package com.company;

import com.company.HelpClasses.Dialogs;

import java.util.*;

public class Player {

    protected static ArrayList<Player> players = new ArrayList<>();
    protected String name;
    protected int money;
    protected ArrayList<Animal> animals;
    protected LinkedHashMap<String, Integer> foods;

    public Player(String name){
        players.add(this);
        this.name = name;
        money = 50000;
        animals = new ArrayList<>();
        foods = new LinkedHashMap<>();
        for(Food food : Mall.foodList) foods.put(food.name, 0);
    }

    public void buyFood(int foodInFoodList, int kg){
       String food = Mall.foodList[foodInFoodList].getClass().getSimpleName();
       foods.put(food, foods.get(food) + kg);
       increaseMoney(Mall.foodList[foodInFoodList].price * kg * -1);
    }

    public int buyAnimal(Animal animal, Player ... players){
        if(animal.calculatePrice() > money) return -1;
        if(players.length != 0) {
            players[0].animals.remove(animal);
            players[0].increaseMoney(animal.calculatePrice());
        }
        animals.add(animal);
        increaseMoney(animal.calculatePrice() * -1);
        return 0;
    }

    public void sellAnimal(Animal animal, Player ... players){
        if(players.length != 0){
            players[0].buyAnimal(animal, this);
        }
        else {
            animals.remove(animal);
            increaseMoney(animal.calculatePrice());
        }
    }

    public void feedAnimal(Animal animal, int foodInFoodList, int kg){
        //increase as much money as Player lose in buyFood()
        increaseMoney(Mall.foodList[foodInFoodList].price * kg);
        buyFood(foodInFoodList, kg * -1);
        animal.health = Math.min(100, animal.health + 10 * kg);
    }

    public void mateTwoAnimals(Animal animal1, Animal animal2) {
        int males = 0;
        int females = 0;
        for (int i = 0; i < animal1.numberOfPossibleBabies; i ++) {
            int tryToMakeABaby = (animal1.age < 4 || animal2.age < 4) ?
                    0 : !animal1.getClass().getSimpleName().equals(animal2.getClass().getSimpleName()) ?
                    0 : animal1.gender == animal2.gender ?
                    0 : (int) (Math.random() * 2);
            if (tryToMakeABaby == 1) {
                int genderReveal = (int) (Math.random() * 2);
                males = genderReveal == 0 ? males + 1 : males;
                females = genderReveal == 1 ? females + 1 : females;
            }
        }
        if(males + females == 0) {
            System.out.println("You didn't get any babies!");
            Dialogs.continuePlaying();
            return;
        }
        ArrayList<String> names = Dialogs.enterName("You got " + (males + females) + " out of " +
                animal1.numberOfPossibleBabies + " possible babies, " + males + " male and " + females +
                " female. What would you like to name them?\nEnter " + (males + females) + " name" +
                        (males + females == 1 ? "" : "s differentiated by an underscore (_)" +
                                "\n(2-20 characters or I will pick a name for you!)"),
                males + females);
        for(int i = 0; i < males; i++)
            animals.add(Mall.createAnimal(animal1.getClass().getSimpleName(), names.get(i), 0));
        for(int i = males; i < females + males; i++)
            animals.add(Mall.createAnimal(animal1.getClass().getSimpleName(), names.get(i), 1));
        Dialogs.continuePlaying();
    }

    public boolean checkIfPlayerDied(){
        if(money == 0 && animals.size() == 0){
            System.out.println(name + " died!");
            players.remove(this);
            return true;
        }
        return false;
    }

    public void payBill(Animal animal){
        if(money < animal.veterinarianCost) animal.chanceOfDeath += 10;
        increaseMoney(Math.max(animal.veterinarianCost * -1, money * -1));
        if((int) (Math.random() * 100) + 1 <= animal.chanceOfDeath) kill(animal);
        else System.out.println(animal.name + " survived!");

    }

    public void kill(Animal animal){
        System.out.println(animal.name + " died!");
        animal.health = 0;
        animals.remove(animal);
    }

    public void increaseMoney(int addedMoney){
        this.money += addedMoney;
    }

    public String[] otherPlayerNames(){
        String[] nameList = new String[players.size() - 1];
        int i = 0;
        int j = 0;
        while(nameList[nameList.length - 1] == null) {
            if (j == players.indexOf(this)) j++;
            nameList[i++] = players.get(j++).name;
        }
        return nameList;
    }

}
