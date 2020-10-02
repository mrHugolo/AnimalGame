package com.company;

import java.util.*;

public class Game {
    Scanner scan = new Scanner(System.in);

    private int round = 1;
    private int rounds = 10;
    private int players = 1;

    public Game() {
        Store s = new Store();

        rules();
        decideRoundsAndPlayers();
        //seeInfo();
        while (round < rounds || Player.players.size() == 0) {
            for (Player player : Player.players) {
                chooseWhatToDo(player, "");
            }
            round++;
        }
        System.out.println("GAME OVER Hugo won :)");
    }

    public void rules() {
        System.out.println("\n\nWelcome to the Animal Game!\n" +
                "Read through the rules and press ENTER to start the game.\n\n" +
                "You will decide how many rounds you will play (5-30) and how many players will play (1-4).\n" +
                "Each player will start with 50.000 coins and are free to purchase different animals and food " +
                "for these animals. Remember that you have to spend money to gain money!\n" +
                "Every player has a choice each round to either buy animals, buy food, feed your existing animals, " +
                "sell your animals or politely ask your existing animals to make more animals.\n" +
                "All animals start with 100 hp and can't get more than that " +
                "but if you don't feed your animals for a round they will lose 10-30 hp.\n" +
                "If you choose to feed your animals they will gain 10 hp for every kg of food, " +
                "but make sure to feed them the right kind of food.\n" +
                "Animals will age each round and that will effect their price. " +
                "The price will increase the first few rounds but will later on start to decrease." +
                " Sell at the optimal time!\n" +
                "Their hp will also effect the price. An animal with 80 hp is only worth 80 % of the price " +
                "and an animal with 0 hp is dead and can never be sold or make babies.\n" +
                "There is a chance that animals get sick, if you don't pay the veterinarian bill the animal will die " +
                "but even if you do pay, there is still a chance that they'll die.\n" +
                "When a player neither have animals nor coins, they are eliminated from the game " +
                "otherwise the game is over after the agreed number of rounds have been played.\n" +
                "At that time all remaining animals will be sold and the player with the most coins win!");
        scan.nextLine();
        cleanSlate(27);
    }

    public void decideRoundsAndPlayers() {
        System.out.println("How many rounds will you be playing? (Write a number between 5-30 and press ENTER)");
        try {
            Scanner scanRounds = new Scanner(System.in);
            int temp = scanRounds.nextInt();
            rounds = Math.max(5, temp);
            rounds = Math.min(30, rounds);
            System.out.println("You will play " + rounds + " rounds!");
        } catch (Exception e) {
            System.out.println("I don't even know what that could mean... I assume you want to play 10 rounds!");
        }

        System.out.println("How many players will be playing? (Write a number between 1-4 and press ENTER)");
        try {
            Scanner scanPlayers = new Scanner(System.in);
            int temp = scanPlayers.nextInt();
            players = Math.max(1, temp);
            players = Math.min(4, players);
            System.out.println("There will be " + players + " players playing!");
        } catch (Exception e) {
            System.out.println("I don't even know what that could mean... I guess you'll have to play alone!");
        }

        for (int i = 1; i <= players; i++) {
            System.out.println("Player " + i + ", please enter your name.");
            new Player(scan.nextLine());

        }
        cleanSlate(27);
    }

    //Put number as "" if you start a new round. Put number as "2" if you want case "2".
    public void chooseWhatToDo(Player player, String number) {
        if (number.equals("")) {
            int chars = charactersInWord(player.name, 0);
            cleanSlate(27);
            System.out.println("It's " + player.name + "'s turn. Enter the number/letter next to the action you want to do!"
                    + "\n\n1. Buy animals" + " ".repeat(40 + chars) + "a. Show AnimalList\n"
                    + "2. Buy food" + " ".repeat(43 + chars) + "f. Show FoodList");

            System.out.println(player.animals.size() == 0 ? " ".repeat(54 + chars) + "i. Show Info" :
                    "3. Feed animals" + " ".repeat(39 + chars) + "i. Show Info\n" +
                            "4. Sell animals\n5. Pick two animals to mate");
            String choice = scan.next();
            if (player.animals.size() == 0 && choice.matches("[3-5]")) choice = "default";
            number = choice;
        }
        switch (number) {
            case "1" -> {
                System.out.println("Buy animal");
            }
            case "2" -> {
                System.out.println("What kind of food would you like to buy?" +
                        "\n1. Carrot " + Store.foodList[0].price + " coins/kg" +
                        "\n2. Steak " + Store.foodList[1].price + " coins/kg");

                Scanner foodScan = new Scanner(System.in);
                String temp = foodScan.nextLine();
                if (!temp.matches("[12]")) {
                    System.out.println("That was not an option and you just wasted your turn...");
                    return;
                }
                int food = temp.compareTo("0");
                System.out.println("How many kg of " + Store.foodList[food - 1].getClass().getSimpleName().toLowerCase() +
                        " would you like to buy?\nThe max amount you can buy is " +
                        (player.money / Store.foodList[food - 1].price) + "kg.");
                try {
                    player.buyFood(food - 1, foodScan.nextInt());
                } catch (Exception e) {
                    System.out.println("That was not an option and you just wasted your turn...");
                    return;
                }
                System.out.println("You successfully bought your food. Would you like to buy more food? (y/n)");
                if (scan.next().equals("y")) chooseWhatToDo(player, "2");
            }
            case "3" -> {
                System.out.println("Feed animal");
            }
            case "4" -> {
                System.out.println("Sell animal");
            }
            case "5" -> {
                System.out.println("Mate...");
            }
            case "a" -> {
                seeAnimalList(player);
                chooseWhatToDo(player, "");
            }
            case "f" -> {
                seeFoodList(player);
                chooseWhatToDo(player, "");
            }
            case "i" -> {
                seeInfo();
                chooseWhatToDo(player, "");
            }
            default -> System.out.println("Default");
        }
    }

    public void seeAnimalList(Player player) {
        for (Animal animal : player.animals) {
            System.out.println(animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() + ":" +
                    "   Age: " + animal.age + "/" + animal.MAX_AGE + "     HP: " + animal.health + "/100" +
                    "   Price: " + animal.calculatePrice() + "/" + animal.MAX_PRICE +
                    "   Eats: " + animal.showFoodsICanEat());
        }
        if (player.animals.size() == 0) System.out.println("You don't have any animals!");
        System.out.println("\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public void seeFoodList(Player player) {
        for (Food food : Store.foodList) {
            System.out.printf("%s %d%s  ", food.getClass().getSimpleName() + ":",
                    player.foods.get(food.getClass().getSimpleName()), "kg");
        }
        System.out.println("\n\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public void seeInfo() {
        String players = "";
        for (Player player : Player.players) {
            players += player.name + " ";
        }
        System.out.println("Rounds played: " + round + "/" + rounds + "     Players left: " + players +
                "\n\nAnimals:        MaxAge:         MaxPrice:       Eats:");
        for (Animal animal : Store.animalList) {
            System.out.println(animal.getClass().getSimpleName() + " ".repeat(16 - charactersInWord(
                    animal.getClass().getSimpleName(), 0)) +
                    (int) animal.MAX_AGE + " ".repeat(16 - charactersInWord("", (int) animal.MAX_AGE)) +
                    (int) animal.MAX_PRICE + " ".repeat(16 - charactersInWord("", (int) animal.MAX_PRICE)) +
                    animal.showFoodsICanEat());
        }
        System.out.println("\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public void cleanSlate(int invisibleAt27) {
        System.out.println("\n".repeat(invisibleAt27));
    }

    public int charactersInWord(String word, int number) {
        if (!word.equals("")) {
            return word.split("").length;
        }
        return String.valueOf(number).split("").length;
    }

}
