package com.company;

import java.util.*;

public class Player {
    Scanner scan = new Scanner(System.in);

    protected static ArrayList<Player> players = new ArrayList<>();
    protected String name;
    protected int money;
    protected ArrayList<Animal> animals;
    protected LinkedHashMap<String, Integer> foods;

    public Player(String name){
        players.add(this);
        this.name = name;
        money = 50000;
        animals = new ArrayList<Animal>();
        foods = new LinkedHashMap<>();
        foods.put("Carrot", 0);
        foods.put("Steak", 0);


    }

    public void buyFood(int foodInFoodList, int kg){
       String food = Store.foodList[foodInFoodList].getClass().getSimpleName();
       foods.put(food, foods.get(food) + kg);
       increaseMoney(Store.foodList[foodInFoodList].price * kg * -1);
    }

    //players will only be 0 or 1
    public void buyAnimal(Animal animal, Player ... players){
        if(players.length != 0) {
            players[0].animals.remove(animal);
            players[0].increaseMoney(animal.calculatePrice());
        }
        this.animals.add(animal);
        increaseMoney(animal.calculatePrice() * -1);
    }

    //players will only be 0 or 1
    public void sellAnimal(Animal animal, Player ... players){
        if(players.length != 0){
            players[0].buyAnimal(animal, this);
        }
        else {
            animals.remove(animal);
            this.increaseMoney(animal.calculatePrice());
        }
    }

    public void feedAnimal(Animal animal, int foodInFoodList, int kg){
        if(animal.foodsICanEat.contains(foodInFoodList)) {
            kg = Math.min(kg, foods.get(Store.foodList[foodInFoodList].getClass().getSimpleName()));
            //increase as much money as Player lose in buyFood()
            increaseMoney(Store.foodList[foodInFoodList].price * kg);
            buyFood(foodInFoodList, kg * -1);
            animal.health = Math.min(100, animal.health + 10 * kg);
            return;
        }
        System.out.println(animal.name + " can't eat " +
                Store.foodList[foodInFoodList].getClass().getSimpleName().toLowerCase() +
        "! That was a waste of a turn...");
    }

    public void mateTwoAnimals(Animal animal1, Animal animal2) {
        int males = 0;
        int females = 0;
        for (int i = 0; i < animal1.numberOfPossibleBabies; i ++) {
            int tryToMakeABaby = !animal1.getClass().getSimpleName().equals(animal2.getClass().getSimpleName())
                    ? 0 : animal1.gender == animal2.gender ? 0 : (int) (Math.random() * 2);
            if (tryToMakeABaby == 1) {
                int genderReveal = (int) (Math.random() * 2);
                males = genderReveal == 0 ? males + 1 : males;
                females = genderReveal == 1 ? females + 1 : females;
            }
        }
        if(males + females == 0) {
            System.out.println("You didn't get any babies!");
            return;
        }
        System.out.printf("%s %d %s %d %s %d %s %d %s",
                "You got ", males + females, " out of ", animal1.numberOfPossibleBabies, " possible babies, ",
                males, " male and ", females, " female. What would you like to name them?");
        System.out.println(" Enter " + (males + females) + " name" + (males + females == 1 ? "" : "s differentiated by a space"));

        String[] names = scan.nextLine().split(" ");
        if(names.length > males + females) System.out.println("You wrote too many names, I'll remove some of them!");
        else if(names.length < males + females) System.out.println("You didn't write enough names, I'll add some!");

        for(int i = 0; i < males; i++){
            try{animals.add(Store.createAnimal(animal1.getClass().getSimpleName(), names[i], 0));}
            catch (Exception e){
                animals.add(Store.createAnimal(animal1.getClass().getSimpleName(), AnimalNames.animalNames[i],0));
            }
        }
        for(int i = 0; i < females; i++){
            try{animals.add(Store.createAnimal(animal1.getClass().getSimpleName(), names[i], 1));}
            catch (Exception e){
                animals.add(Store.createAnimal(animal1.getClass().getSimpleName(), AnimalNames.animalNames[i],0));
            }
        }
    }

    public void increaseMoney(int addedMoney){
        this.money += addedMoney;
    }

    public ArrayList<String> getAnimalNames(){
        ArrayList<String> animalNames = new ArrayList<>();
        for(Animal animal : animals) animalNames.add(animal.name);
        return animalNames;
    }




}
