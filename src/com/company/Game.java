package com.company;

import java.util.*;

public class Game {
    Scanner scan = new Scanner(System.in);

    private int round = 1;
    private int rounds = 10;
    private int players = 1;
    //loseATurn is a red warning text
    private String loseATurn = "\u001B[31m\nIf you type a number/letter that doesn't correspond to any available player," +
            " you will lose your turn.\n\u001B[0m";

    public Game() {
        Store s = new Store();

        rules();
        decideRoundsAndPlayers();
        Player.players.get(0).buyAnimal(Store.createAnimal("Bear", "Yogi", 0));
        Player.players.get(0).buyAnimal(Store.createAnimal("Bear", "Brumme_Lisa", 1));
        while (round < rounds || Player.players.size() == 0) {
            for (Player player : Player.players) {
                readOutVeterinarianBills(player);
                chooseWhatToDo(player, "");
            }


            for(Player player : Player.players){
                for(Animal animal : player.animals){
                    animal.age++;
                    if(animal.loseHp) animal.loseHealth();
                    animal.loseHp = true;
                    if(animal.isSick()) player.getVeterinarianBill(animal);
                }
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
            cleanSlate(30);
            System.out.println("You will play " + rounds + " rounds!");
        } catch (Exception e) {
            cleanSlate(30);
            System.out.println("I don't even know what that could mean... I assume you want to play 10 rounds!");
        }

        System.out.println("How many players will be playing? (Write a number between 1-4 and press ENTER)");
        try {
            Scanner scanPlayers = new Scanner(System.in);
            int temp = scanPlayers.nextInt();
            players = Math.max(1, temp);
            players = Math.min(4, players);
            cleanSlate(30);
            System.out.println("There will be " + players + " players playing!");
        } catch (Exception e) {
            cleanSlate(30);
            System.out.println("I don't even know what that could mean... I guess you'll have to play alone!");
        }
        for (int i = 1; i <= players; i++) {
            System.out.println("Player " + i + ", please enter your name.");
            new Player(scan.nextLine());

        }
        cleanSlate(27);
    }

    public void readOutVeterinarianBills(Player player){
        cleanSlate(27);
        int bills = player.veterinarianBill.size();
        System.out.println(player.name + ": You have " + bills + " sick animal" + (bills == 1 ? "." : "s.") +
                "\nPress c to continue!");
        scan.next();
        for(Animal animal : player.veterinarianBill){
            cleanSlate(27);
            System.out.println(animal.name + " is sick. If you don't pay " + animal.veterinarianCost + " coins, " +
                    (animal.gender == 0 ? "he" : "she") + " will die! Even if you do pay, there is still " +
                    animal.chanceOfDeath + " % chance that " + (animal.gender == 0 ? "he" : "she") + " will die!" +
                    "\nYou have " + player.money + " coins. Would you like to pay the bill? (y/n)");
            Scanner deathScan = new Scanner(System.in);
            if(deathScan.next().equals("y")) player.payBill(animal);
            else player.kill(animal);
        }

    }

    //Put number as "" if you start a new round. Put number as "2" if you want case "2".
    public void chooseWhatToDo(Player player, String number) {
        if (number.equals("")) {
            int chars = charactersInWord(player.name, 0);
            cleanSlate(27);
            System.out.println("It's " + player.name + "'s turn and you have " + player.money + " coins. " +
                    "Enter the number/letter next to the action you want to do!"
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
                if(buyAnimal(player).equals("y"))
                    chooseWhatToDo(player, "1");
            }
            case "2" -> {
                if (buyFood(player).equals("y"))
                    chooseWhatToDo(player, "2");
            }

            case "3" -> {
                if(feedAnimal(player).equals("y"))
                    chooseWhatToDo(player, "3");
            }
            case "4" -> {
                if(sellAnimal(player).equals("y"))
                    chooseWhatToDo(player, "4");
            }
            case "5" -> {
                System.out.println("Mate...");
            }
            case "a" -> {
                seeAnimalList(player, "");
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
            default -> chooseWhatToDo(player, "");
        }
    }

    public String buyAnimal(Player player){
        cleanSlate(27);
        System.out.println(player.name + ": From where would you like to buy your animal?" + loseATurn +
                "\n1. The Store\n2. Other Players");
        String choice = scan.next();
        if(choice.equals("1")) {if(!buyAnimalFromStore(player).equals("I bought an animal.")) return "";}
        else if(choice.equals("2")) {if(!buyAnimalFromPlayer(player).equals("I bought an animal.")) return "";}
        else return "";

        cleanSlate(27);
        System.out.println(player.name + ": Would you like to buy more animals? (y/n)");
        return scan.next();
    }

    public String buyAnimalFromStore(Player player){
        cleanSlate(27);
        System.out.println("What animal would you like to buy?" + loseATurn + "\n");
        int i = 1;
        for(Animal animal : Store.animalList){
            System.out.println(i++ + ": " + animal.name + " " + animal.calculatePrice() + " coins.");
        }
        try{
            Scanner animalScan = new Scanner(System.in);
            int animal = animalScan.nextInt() - 1;
            cleanSlate(27);
            System.out.println("Which gender should your " + Store.animalList[animal].name.toLowerCase() +
                    " be?" + loseATurn + "\n1. Male\n2. Female");
            int gender = animalScan.nextInt() - 1;
            cleanSlate(27);
            System.out.println("Please enter in a name for your " +
                    (gender == 0 ? "male " : "female ") + Store.animalList[animal].name.toLowerCase() +
                    " (1-20 characters or I will pick a name for you!)");
            Scanner nameScan = new Scanner(System.in);
            String name = nameScan.nextLine();
            if(charactersInWord(name, 0) > 20) name.equals(AnimalNames.animalNames[(int) (Math.random() * 4)]);
            player.buyAnimal(Store.createAnimal(Store.animalList[animal].name, name, gender));
        }
        catch (Exception Return){return "";}

        return "I bought an animal.";
    }

    public String buyAnimalFromPlayer(Player player){
        cleanSlate(27);
        System.out.println("\nWhich player do you want to buy from?" + loseATurn);

        for(int i = 1; i < Player.players.size(); i++){
            System.out.println(i + ". " + (Player.players.indexOf(player) < i ? Player.players.get(i).name :
                    Player.players.get(i - 1).name));
        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int number = animalScan.nextInt();
            number = Player.players.indexOf(player) < number ? number : number - 1;
            if(seeAnimalList(Player.players.get(number), "hideContinueText") != 0) {
                System.out.println("\nWhich animal would you like to buy?" + loseATurn);
            }
            else {
                System.out.println("Press c to buy animals somewhere else.");
                scan.next();
                chooseWhatToDo(player, "1");
            }
            int animal = animalScan.nextInt() - 1;
            if(player.buyAnimal(Player.players.get(number).animals.get(animal), Player.players.get(number)) != -1){
                cleanSlate(27);
                System.out.println(Player.players.get(number).name + ": Would you like to sell " +
                        player.animals.get(player.animals.size() - 1).name +
                        " for " + player.animals.get(player.animals.size() - 1).calculatePrice() + " coins? (y/n)");
                if(!scan.next().equals("y")) {
                    player.sellAnimal(player.animals.get(player.animals.size() - 1), Player.players.get(number));
                    chooseWhatToDo(player, "1");
                }
            }
            else{
                System.out.println("Sorry, you can't afford this animal. Would you like to buy another animal? (y/n)");
                if(!scan.next().equals("n")) chooseWhatToDo(player, "1");
                return "";
            }
        }
        catch (Exception Return){return "";}
        return "I bought an animal.";
    }

    public String buyFood(Player player){
        cleanSlate(27);
        System.out.println("What kind of food would you like to buy?" + loseATurn +
                "\n1. Carrot " + Store.foodList[0].price + " coins/kg" +
                "\n2. Steak " + Store.foodList[1].price + " coins/kg");

        Scanner foodScan = new Scanner(System.in);
        String temp = foodScan.nextLine();
        if (!temp.matches("[12]"))return"";

        int food = temp.compareTo("0");
        cleanSlate(27);
        System.out.println("How many kg of " + Store.foodList[food - 1].getClass().getSimpleName().toLowerCase() +
                " would you like to buy?\nThe max amount you can buy is " +
                (player.money / Store.foodList[food - 1].price) + "kg.");

        int kg = 0;
        try {
            kg = scan.nextInt();
            kg = Math.max(0, kg);
            kg = Math.min(player.money / Store.foodList[food - 1].price, kg);
        } catch (Exception ignore) {}
        player.buyFood(food - 1, kg);
        cleanSlate(27);
        System.out.println("You bought " + kg + "kg of " + Store.foodList[food - 1].getClass().getSimpleName().
                toLowerCase() + ". Would you like to buy more food? (y/n)");
        return scan.next();
    }

    public String feedAnimal(Player player){
        cleanSlate(27);
        seeAnimalList(player, "hideContinueText");
        System.out.println("\nWhich animal do you want to feed?" + loseATurn);
        try{
            Scanner feedScan = new Scanner(System.in);
            int animal = feedScan.nextInt() - 1;
            cleanSlate(27);
            System.out.println("Which food would you like to give " + player.animals.get(animal).name + "?" + loseATurn + "\n");
            int i = 1;
            for(Food food : Store.foodList){
                System.out.println(i++ + ": " + food.getClass().getSimpleName());
            }
            int food = feedScan.nextInt() - 1;
            cleanSlate(27);
            System.out.println("How many kg of " + Store.foodList[food].getClass().getSimpleName().toLowerCase() +
                    " would you like to feed " + player.animals.get(animal).name + "? You have " +
                    player.foods.get(Store.foodList[food].getClass().getSimpleName()) + "kg");
            Scanner kgScan = new Scanner(System.in);
            int kg = kgScan.nextInt();
            kg = Math.max(0, kg);
            kg = Math.min(player.foods.get(Store.foodList[food].getClass().getSimpleName()), kg);
            cleanSlate(27);
            System.out.println("You gave " + player.animals.get(animal).name + " " + kg + "kg of " +
                    Store.foodList[food].getClass().getSimpleName().toLowerCase());
            if(player.animals.get(animal).foodsICanEat.contains(food)) {
                player.feedAnimal(player.animals.get(animal), food, kg);
                player.animals.get(animal).loseHp = false;
                System.out.println("Tasty! " + player.animals.get(animal).name + " now has " +
                        player.animals.get(animal).health  + "hp.");
            }
            else{
                player.foods.put(Store.foodList[food].getClass().getSimpleName(), player.foods.get(food) - kg);
                System.out.println("That was a waste of food... " + player.animals.get(animal).name + " can't eat " +
                        Store.foodList[food].getClass().getSimpleName() + " so " +
                        (player.animals.get(animal).gender == 0 ? "he " : "she ") + "didn't gain any hp.");
            }
        }
        catch (Exception Return){return "";}
        System.out.println("Would you like to feed more animals? (y/n)");
        return scan.next();
    }

    public String sellAnimal(Player player){
        cleanSlate(27);
        System.out.println("Which animal do you want to sell?" + loseATurn + "\n");
        int i = 1;
        for(Animal animal : player.animals){
            System.out.println(i++ + ": " + animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() +
                    ": " + animal.price + " coins.");

        }
        try{
            Scanner animalScan = new Scanner(System.in);
            int animal = animalScan.nextInt() - 1;
            cleanSlate(27);
            System.out.println("Where would you like to sell " + player.animals.get(animal).name + "?" + loseATurn +
                    "\n1. The Store\n2. Other Player");
            String number = animalScan.next();
            if(number.equals("1")) player.sellAnimal(player.animals.get(animal));
            else if(number.equals("2")){
                cleanSlate(27);
                System.out.println("Which player do you want to sell " + player.animals.get(animal).name + " to?");
                for(int j = 1; j < Player.players.size(); j++){
                    System.out.println(j + ". " + (Player.players.indexOf(player) < j ? Player.players.get(j).name :
                            Player.players.get(j - 1).name));
                }
                Scanner playerScan = new Scanner(System.in);
                int choice = playerScan.nextInt();
                choice = Player.players.indexOf(player) < choice ? choice : choice - 1;
                cleanSlate(27);
                System.out.println(Player.players.get(choice).name + ": Do you want to buy " +
                        player.animals.get(animal).name + " the " +
                        player.animals.get(animal).getClass().getSimpleName().toLowerCase() +
                        " for " + player.animals.get(animal).price + " coins? (y/n)");
                String yOrN = scan.next();
                if(yOrN.equals("y")) player.sellAnimal(player.animals.get(animal), Player.players.get(choice));
            }
            else return "";

        }
        catch (Exception Return){return "";}
        if(player.animals.size() == 0) return "";
        System.out.println("Would you like to sell more animals? (y/n)");
        return scan.next();
    }

    public int seeAnimalList(Player player, String hideContinueText) {
        cleanSlate(27);
        System.out.println(player.name + "'s AnimalList:\n");
        int i = 1;
        for (Animal animal : player.animals) {
            System.out.println(i++ + ". " + animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() + ":" +
                    "   Gender: " + animal.getGenderString() +
                    "   Age: " + animal.age + "/" + (int) animal.MAX_AGE + "     HP: " + animal.health + "/100" +
                    "   Price: " + animal.calculatePrice() + "/" + (int) animal.MAX_PRICE +
                    "   Eats: " + animal.showFoodsICanEat());
        }
        if (player.animals.size() == 0) System.out.println("You don't have any animals!");
        if(!hideContinueText.equals("hideContinueText")) {
            System.out.println("\nPress c followed by ENTER to continue!");
            scan.next();
        }
        return player.animals.size();
    }

    public void seeFoodList(Player player) {
        cleanSlate(27);
        System.out.println(player.name + "'s FoodList:\n");
        for (Food food : Store.foodList) {
            System.out.printf("%s %d%s  ", food.getClass().getSimpleName() + ":",
                    player.foods.get(food.getClass().getSimpleName()), "kg");
        }
        System.out.println("\n\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public void seeInfo() {
        cleanSlate(27);
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
