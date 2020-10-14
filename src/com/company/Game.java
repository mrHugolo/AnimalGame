package com.company;


import com.company.HelpClasses.*;

import java.util.*;

public class Game {
    Scanner scan = new Scanner(System.in);

    private int round = 1;
    private int rounds = 5;

    public Game() {
        Dialogs.rules();
        decideRoundsAndPlayers();
        while (round <= rounds || Player.players.size() == 0) {
            for (int i = 0; i < Player.players.size(); i++) {
                readOutVeterinarianBills(Player.players.get(i));
                chooseWhatToDo(Player.players.get(i));
                if(Player.players.get(i).checkIfPlayerDied()) {
                    i--;
                    Dialogs.continuePlaying();
                }
            }
            for (Player player : Player.players) {
                player.animals.removeIf(Animal::endOfTurn);
            }
            round++;
        }
        sellAllAnimals();
        showWhoWon(Player.players);
    }

    public void decideRoundsAndPlayers() {
        rounds = Dialogs.promptInt("How many rounds do you want to play?", 5, 30);
        Dialogs.cleanSlate(30);
        System.out.println("You will play for " + rounds + " rounds!");
        int players = Dialogs.promptInt("How many players will be playing?", 1, 4);
        Dialogs.cleanSlate(30);
        System.out.println("There will be " + players + " players!");

        for (int i = 1; i <= players; i++) {
            System.out.println("Player " + i + ", please enter your name.");
            new Player(scan.nextLine());
        }
    }

    public void readOutVeterinarianBills(Player player) {

        Dialogs.cleanSlate(30);
        int counter = 0;
        System.out.println(player.name + ": These animals are sick!");
        for (Animal animal : player.animals) {
            if(animal.isSick)System.out.println(++counter + ". " + Dialogs.animalName(animal));
        }
        if(counter == 0) return;
        Dialogs.continuePlaying();
        for(int i = player.animals.size() - 1; i >= 0; i--) {
            Dialogs.cleanSlate(30);
            if(player.animals.get(i).isSick) {
                if (Dialogs.promptString(Dialogs.animalName(player.animals.get(i)) +
                        " is sick. If you don't pay " + player.animals.get(i).veterinarianCost + " coins, " +
                        (player.animals.get(i).gender == 0 ? "he" : "she") + " will die! Even if you do pay, " +
                        "there is still " + player.animals.get(i).chanceOfDeath + " % chance that " +
                        (player.animals.get(i).gender == 0 ? "he" : "she") + " will die!" +
                        "\nYou have " + player.money + " coins. Would you like to pay the bill? (y/n)", "y"))
                    player.payBill(player.animals.get(i));
                else player.kill(player.animals.get(i));
                Dialogs.continuePlaying();
            }
        }

    }

    public void sellAllAnimals(){
        for (Player player : Player.players) {
            while (player.animals.size() > 0) {
                player.sellAnimal(player.animals.get(0));
            }
        }
    }

    public void chooseWhatToDo(Player player, String ... number) {
        Dialogs.cleanSlate(30);
        String choice;
        if (number.length == 0) {
            choice = Dialogs.menuWithLetters(player.name +
                            ": You have " + player.money + " coins. " +
                            "Enter the number/letter next to the action you want to do!\n",

                    "1. Buy Animals", "a. Show Animal List",
                    "2. Buy Food", "f. Show Food List",
                    "3. Feed Animals", "i. Show Info",
                    "4. Sell Animals", "s. Skip Your Turn",
                    "5. Pick Two Animals to Mate", "");
        }
        else choice = number[0];

        switch (choice) {
            case "1" ->  buyAnimal(player);
            case "2" -> buyFood(player);
            case "3" -> feedAnimal(player);
            case "4" -> sellAnimal(player);
            case "5" -> mate(player);
            case "a" -> {
                seeAnimalList(player);
                Dialogs.continuePlaying();
                chooseWhatToDo(player);
            }
            case "f" -> {
                seeFoodList(player);
                chooseWhatToDo(player);
            }
            case "i" -> {
                seeInfo();
                chooseWhatToDo(player);
            }
            case "s" -> {}
            default -> chooseWhatToDo(player );
        }
    }

    public void buyAnimal(Player player){
        Dialogs.cleanSlate(30);
        int notEnoughMoney = 0;
        if(Dialogs.menu(player.name + ": From where would you like to buy your animal?",
                "The Mall", "Other Players") == 1){
            String[] animals = new String[Mall.animalList.length];
            int i = 0;
            for(Animal animal : Mall.animalList) animals[i++] = animal.name + " " + animal.calculatePrice() + " coins.";
            Dialogs.cleanSlate(30);
            int animalChoice = Dialogs.menu("What animal would you like to buy?", animals) - 1;
            Dialogs.cleanSlate(30);
            int genderChoice = Dialogs.menu("Which gender should your " + Mall.animalList[animalChoice].name +
                    " be?", "Male", "Female") - 1;
            Dialogs.cleanSlate(30);
            String nameChoice = Dialogs.enterName("Please enter in a name for your " +
                    (genderChoice == 0 ? "male " : "female ") + Mall.animalList[animalChoice].name +
                    " (2-20 characters or I will pick a name for you!)", 1).get(0);
            notEnoughMoney = player.buyAnimal(Objects.requireNonNull(
                    Mall.createAnimal(Mall.animalList[animalChoice].name, nameChoice, genderChoice)));
        }
        else if (Player.players.size() > 1){
            Dialogs.cleanSlate(30);
            int playerChoice = Dialogs.menu("Which player do you want to buy from?", player.otherPlayerNames());
            playerChoice = Player.players.indexOf(player) < playerChoice ? playerChoice : playerChoice - 1;
            Dialogs.cleanSlate(30);
            int max = seeAnimalList(Player.players.get(playerChoice));
            if(max == 0){
                chooseWhatToDo(player, "1");
                return;
            }
            int animalChoice = Dialogs.promptInt("Which animal do you want to buy?", 1, max) - 1;
            Dialogs.cleanSlate(30);
            if(Dialogs.promptString(Player.players.get(playerChoice).name + ": Do you want to sell " +
                    Player.players.get(playerChoice).animals.get(animalChoice).name +
                    " for " + Player.players.get(playerChoice).animals.get(animalChoice).calculatePrice() +
                    " coins? (y/n)", "y"))
                notEnoughMoney = player.buyAnimal(
                        Player.players.get(playerChoice).animals.get(animalChoice),
                        Player.players.get(playerChoice));
        }
        Dialogs.cleanSlate(30);
        if(notEnoughMoney == -1) System.out.println("You can't afford this animal.");
        if(Dialogs.promptString(player.name + ": Do you want to buy more animals? (y/n)", "y"))
            chooseWhatToDo(player, "1");
    }

    public void buyFood(Player player) {
        Dialogs.cleanSlate(30);
        String[] options = new String[Mall.foodList.length];
        for(int i = 0; i < Mall.foodList.length; i++)
            options[i] = Mall.foodList[i].name + " " + Mall.foodList[i].price + " coins/kg";
        int food = Dialogs.menu("What kind of food would you like to buy?", options) - 1;

        Dialogs.cleanSlate(30);
        int kg = Dialogs.promptInt("How many kg of " + Mall.foodList[food].name +
                " would you like to buy?", 0, (player.money / Mall.foodList[food].price));
        player.buyFood(food, kg);
        Dialogs.cleanSlate(30);

        if(Dialogs.promptString("You bought " + kg + "kg of " + Mall.foodList[food].name +
                ". Would you like to buy more food? (y/n)", "y")) chooseWhatToDo(player, "2");
    }

    public void feedAnimal(Player player) {
        Dialogs.cleanSlate(30);
        if(seeAnimalList(player) == 0){
            chooseWhatToDo(player);
            return;
        }
        int animalChoice = Dialogs.promptInt(
                "\nWhich animal do you want to feed?", 1, player.animals.size()) - 1;
        Dialogs.cleanSlate(30);
        String[] options = new String[Mall.foodList.length];
        int i = 0;
        for(Food food : Mall.foodList)options[i++] = food.name + " " + player.foods.get(food.name) + "kg";
        int foodChoice = Dialogs.menu("What food would you like to give to " +
                player.animals.get(animalChoice).name + "?", options) - 1;
        Dialogs.cleanSlate(30);
        int kgChoice = Dialogs.promptInt(
                "How many kg of " + Mall.foodList[foodChoice].getClass().getSimpleName() +
                " would you like to feed " + player.animals.get(animalChoice).name + "?\n" +
                (player.animals.get(animalChoice).gender == 0 ? "He" : "She") +
                " can eat " + (player.animals.get(animalChoice).howMuchFoodICanEat -
                player.animals.get(animalChoice).howMuchFoodIAteToday) + "kg. You have " +
                player.foods.get(Mall.foodList[foodChoice].getClass().getSimpleName()) + "kg", 0,
                Math.min((player.animals.get(animalChoice).howMuchFoodICanEat -
                        player.animals.get(animalChoice).howMuchFoodIAteToday),
                        player.foods.get(Mall.foodList[foodChoice].getClass().getSimpleName())));
        Dialogs.cleanSlate(30);
        if (player.animals.get(animalChoice).foodsICanEat.contains(foodChoice)) {
            player.feedAnimal(player.animals.get(animalChoice), foodChoice, kgChoice);
            player.animals.get(animalChoice).loseHp = false;
            System.out.println("Tasty! " + player.animals.get(animalChoice).name + " ate " + kgChoice +
                    "kg and now has " +
                    player.animals.get(animalChoice).health + "hp.");
        } else {
            String foodName = Mall.foodList[foodChoice].getClass().getSimpleName();
            player.foods.put(foodName, player.foods.get(foodName) - kgChoice);
            System.out.println("That was a waste of food... " + player.animals.get(animalChoice).name + " can't eat " +
                    Mall.foodList[foodChoice].getClass().getSimpleName() + " so " +
                    (player.animals.get(animalChoice).gender == 0 ? "he " : "she ") + "didn't gain any hp.");
        }
        player.animals.get(animalChoice).howMuchFoodIAteToday += kgChoice;
        if(Dialogs.promptString("Would you like to feed more animals? (y/n)", "y"))
            chooseWhatToDo(player, "3");
    }

    public void sellAnimal(Player player) {
        Dialogs.cleanSlate(30);
        if(seeAnimalList(player) == 0){
            chooseWhatToDo(player);
            return;
        }
        int animalChoice = Dialogs.promptInt(
                "Which animal do you want to sell?", 1, player.animals.size()) - 1;
        Dialogs.cleanSlate(30);
        String[] options = {"The Mall", "Other Players"};
        if(Dialogs.menu(
                "Where would you like to sell " + player.animals.get(animalChoice).name + "?", options) == 1)
            player.sellAnimal(player.animals.get(animalChoice));
        else {
            int playerChoice = Dialogs.menu("Which player do you want to sell " +
                            player.animals.get(animalChoice).name + " to?",
                    player.otherPlayerNames());
            playerChoice = Player.players.indexOf(player) < playerChoice ? playerChoice : playerChoice - 1;
            if(Dialogs.promptString((Player.players.get(playerChoice).name + ": Do you want to buy " +
                    Dialogs.animalName(player.animals.get(animalChoice)) +
                    " for " + player.animals.get(animalChoice).calculatePrice() + " coins? (y/n)"), "y"))
            player.sellAnimal(player.animals.get(animalChoice), Player.players.get(playerChoice));
        }
        if(Dialogs.promptString("Would you like to sell more animals? (y/n)", "y")
                && player.animals.size() > 0)
            chooseWhatToDo(player, "4");
    }

    public void mate(Player player) {
        Dialogs.cleanSlate(30);
        if(seeAnimalList(player) < 2) {
            chooseWhatToDo(player);
            return;
        }
        String animals = Dialogs.menuWithLetters("Pick two animals, separated by an underscore (_)." +
                " They will try to make a few babies.");
        try {
            int animal1 = Integer.parseInt(animals.split("_")[0]) - 1;
            int animal2 = Integer.parseInt(animals.split("_")[1]) - 1;
            player.mateTwoAnimals(player.animals.get(animal1), player.animals.get(animal2));
        }
        catch(Exception e){chooseWhatToDo(player, "5");}


    }

    public void showWhoWon(ArrayList<Player> players) {
        Dialogs.cleanSlate(30);
        System.out.println("HERE ARE THE STANDINGS:");
        ArrayList<Player> order = new ArrayList<>();
        order.add(players.get(0));

        for (int j = 1; j < players.size(); j++) {
            for (int i = 0; i < order.size(); i++) {
                if (players.get(j).money >= order.get(i).money) {
                    order.add(i, players.get(j));
                    break;
                } else if (i == order.size() - 1) {
                    order.add(i + 1, players.get(j));
                    break;
                }
            }
        }
        for (int i = 0; i < order.size(); i++)
            System.out.println((i + 1) + ". " + order.get(i).name +
                    "    : " + order.get(i).money + " coins");
    }

    public int seeAnimalList(Player player) {
        Dialogs.cleanSlate(30);
        System.out.println(player.name + "'s AnimalList:\n");
        int i = 1;
        for (Animal animal : player.animals) {
            String name = animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() + ":";
            String gender = "Gender: " + animal.getGenderString();
            String age = "Age: " + animal.age + "/" + (int) animal.MAX_AGE;
            String health = "HP: " + animal.health + "/100";
            String price = "Price: " + animal.calculatePrice() + "/" + (int) animal.MAX_PRICE;
            String eats = "Eats: " + animal.showFoodsICanEat();

            System.out.println(i++ + ". " +
                    name + " ".repeat(40 - Dialogs.charCounter(name)) +
                    gender + " ".repeat(20 - Dialogs.charCounter(gender)) +
                    age + " ".repeat(15 - Dialogs.charCounter(age)) +
                    health + " ".repeat(15 - Dialogs.charCounter(health)) +
                    price + " ".repeat(20 - Dialogs.charCounter(price)) +
                    eats);
        }
        if (player.animals.size() == 0) System.out.println("You don't have any animals!");
        return player.animals.size();
    }

    public void seeFoodList(Player player) {
        Dialogs.cleanSlate(30);
        System.out.println(player.name + "'s FoodList:");
        for (Food food : Mall.foodList) {
            System.out.printf("\n%s %d%s  ", food.getClass().getSimpleName() + ":",
                    player.foods.get(food.getClass().getSimpleName()), "kg");
        }
        Dialogs.continuePlaying();
    }

    public void seeInfo() {
        Dialogs.cleanSlate(30);
        StringBuilder players = new StringBuilder();
        for (Player player : Player.players) {
            players.append(player.name).append(" ");
        }
        System.out.println("Rounds played: " + round + "/" + rounds + "     Players left: " + players +
                "\n\nAnimals:        MaxAge:         MaxPrice:       Eats:");
        for (Animal animal : Mall.animalList) {
            System.out.println(animal.getClass().getSimpleName() + " ".repeat(16 - Dialogs.charCounter(
                    animal.getClass().getSimpleName())) +
                    (int) animal.MAX_AGE + " ".repeat(16 - Dialogs.charCounter((int) animal.MAX_AGE + "")) +
                    (int) animal.MAX_PRICE + " ".repeat(16 - Dialogs.charCounter((int) animal.MAX_PRICE + "")) +
                    animal.showFoodsICanEat());
        }
        Dialogs.continuePlaying();
    }

}
