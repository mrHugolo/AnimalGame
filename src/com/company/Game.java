package com.company;


import java.util.*;

public class Game {
    Scanner scan = new Scanner(System.in);

    private int round = 1;
    private int rounds = 10;
    private int players = 1;
    //loseATurn is a red warning text
    private final String loseATurn = "\u001B[31m\n" +
            "If you type a number/letter that doesn't correspond to any available option," +
            " you will lose your turn.\n\u001B[0m";

    public Game() {
        rules();
        decideRoundsAndPlayers();
        while (round <= rounds || Player.players.size() == 0) {
            for (Player player : Player.players) {
                if (player.checkIfPlayerDied()) {
                    System.out.println(player.name + ": You have died!\nPress c to continue");
                    Player.players.remove(player);
                    scan.next();
                    continue;
                }
                readOutVeterinarianBills(player, player.veterinarianBill.size());
                chooseWhatToDo(player, "");
            }
            for (Player player : Player.players) {
                for (Animal animal : player.animals) {
                    animal.age++;
                    if (animal.loseHp) animal.loseHealth();
                    animal.loseHp = true;
                    if (animal.isSick()) player.getVeterinarianBill(animal);
                    animal.howMuchFoodIAteToday = 0;
                }
            }
            round++;
        }
        for (Player player : Player.players) {
            while (player.animals.size() > 0) {
                player.sellAnimal(player.animals.get(0));
            }
        }
        showWhoWon(Player.players);
    }

    public void rules() {
        System.out.println("\n\nWelcome to AniMall!\n" +
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
                "An animal with low hp and high age will more likely get sick than one with high hp and low age.\n" +
                "When a player neither have animals nor coins, they are eliminated from the game " +
                "otherwise the game is over after the agreed number of rounds have been played.\n" +
                "At that time all remaining animals will be sold and the player with the most coins win!");
        scan.nextLine();
        cleanSlate(30);
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
        cleanSlate(30);
    }

    public void readOutVeterinarianBills(Player player, int sickAnimals) {
        if (sickAnimals == 0) return;

        cleanSlate(30);
        int bills = player.veterinarianBill.size();

        for (Animal animal : player.veterinarianBill) {
            cleanSlate(30);
            System.out.println(player.name + ": You have " + bills + " sick animal" + (bills == 1 ? "." : "s.") +
                    "\n" + animal.name + " the " + animal.getClass().getSimpleName().toLowerCase());
            System.out.println("Press c to continue.");
            scan.next();
            System.out.println(animal.name + " is sick. If you don't pay " + animal.veterinarianCost + " coins, " +
                    (animal.gender == 0 ? "he" : "she") + " will die! Even if you do pay, there is still " +
                    animal.chanceOfDeath + " % chance that " + (animal.gender == 0 ? "he" : "she") + " will die!" +
                    "\nYou have " + player.money + " coins. Would you like to pay the bill? (y/n)");
            Scanner deathScan = new Scanner(System.in);
            if (deathScan.next().equals("y")) player.payBill(animal);
            else player.kill(animal);
            System.out.println("Press c to continue!");
            scan.next();
        }

    }

    //Put number as "" if you start a new round. Put number as "2" if you want case "2".
    public void chooseWhatToDo(Player player, String number) {
        if (number.equals("")) {
            int chars = charactersInWord(player.name, 0);
            cleanSlate(30);
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
                if (buyAnimal(player).equals("y"))
                    chooseWhatToDo(player, "1");
            }
            case "2" -> {
                if (buyFood(player).equals("y"))
                    chooseWhatToDo(player, "2");
            }

            case "3" -> {
                if (feedAnimal(player).equals("y"))
                    chooseWhatToDo(player, "3");
            }
            case "4" -> {
                if (sellAnimal(player).equals("y"))
                    chooseWhatToDo(player, "4");
            }
            case "5" -> mate(player);

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

    public String buyAnimal(Player player) {
        cleanSlate(30);
        System.out.println(player.name + ": From where would you like to buy your animal?" + loseATurn +
                "\n1. The Store\n2. Other Players");
        String choice = scan.next();
        if (choice.equals("1")) {
            if (!buyAnimalFromStore(player).equals("I bought an animal.")) return "";
            cleanSlate(30);
        } else if (choice.equals("2")) {
            String buyAnimalFromPlayer = buyAnimalFromPlayer(player);
             if (buyAnimalFromPlayer.equals("Try again.")) {/*do nothing*/}
            else if (!buyAnimalFromPlayer.equals("I bought an animal.")) return "";
            else cleanSlate(30);

        } else return "";

        System.out.println(player.name + ": Would you like to buy more animals? (y/n)");
        Scanner returnScan = new Scanner(System.in);
        return returnScan.next();
    }

    public String buyAnimalFromStore(Player player) {
        cleanSlate(30);
        System.out.println("What animal would you like to buy?" + loseATurn + "\n");
        int i = 1;
        for (Animal animal : Store.animalList) {
            System.out.println(i++ + ": " + animal.name + " " + animal.calculatePrice() + " coins.");
        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int animal = animalScan.nextInt() - 1;
            cleanSlate(30);
            System.out.println("Which gender should your " + Store.animalList[animal].name.toLowerCase() +
                    " be?" + loseATurn + "\n1. Male\n2. Female");
            int gender = animalScan.nextInt() - 1;
            cleanSlate(30);
            System.out.println("Please enter in a name for your " +
                    (gender == 0 ? "male " : "female ") + Store.animalList[animal].name.toLowerCase() +
                    " (1-20 characters or I will pick a name for you!)");
            Scanner nameScan = new Scanner(System.in);
            String name = nameScan.nextLine();
            if (charactersInWord(name, 0) > 20 || charactersInWord(name, 0) == 0)
                name = (AnimalNames.animalNames[(int) (Math.random() * 4)]);
            player.buyAnimal(Objects.requireNonNull(Store.createAnimal(Store.animalList[animal].name, name, gender)));
        } catch (Exception Return) {
            return "";
        }

        return "I bought an animal.";
    }

    public String buyAnimalFromPlayer(Player player) {
        cleanSlate(30);
        System.out.println("\nWhich player do you want to buy from?" + loseATurn);

        for (int i = 1; i < Player.players.size(); i++) {
            System.out.println(i + ". " + (Player.players.indexOf(player) < i ? Player.players.get(i).name :
                    Player.players.get(i - 1).name));
        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int number = animalScan.nextInt();
            number = Player.players.indexOf(player) < number ? number : number - 1;
            if (seeAnimalList(Player.players.get(number), "hideContinueText") != 0) {
                System.out.println("\nWhich animal would you like to buy?" + loseATurn);
            } else return "Try again.";

            int animal = animalScan.nextInt() - 1;
            if (player.buyAnimal(Player.players.get(number).animals.get(animal), Player.players.get(number)) != -1) {
                cleanSlate(30);
                System.out.println(Player.players.get(number).name + ": Would you like to sell " +
                        player.animals.get(player.animals.size() - 1).name +
                        " for " + player.animals.get(player.animals.size() - 1).calculatePrice() + " coins? (y/n)");

                Scanner yOrNScan = new Scanner(System.in);
                String yOrN = yOrNScan.nextLine();
                if (!yOrN.equals("y"))
                    player.sellAnimal(player.animals.get(player.animals.size() - 1), Player.players.get(number));
                return "I bought an animal.";
            } else {
                System.out.println("Sorry, you can't afford this animal. Would you like to buy another animal? (y/n)");
                if (!scan.next().equals("n")) chooseWhatToDo(player, "1");
                return "";
            }
        } catch (Exception Return) {
            return "";
        }

    }

    public String buyFood(Player player) {
        cleanSlate(30);
        System.out.println("What kind of food would you like to buy?" + loseATurn +
                "\n1. Fruit " + Store.foodList[0].price + " coins/kg" +
                "\n2. Berries " + Store.foodList[1].price + " coins/kg" +
                "\n3. Nuts " + Store.foodList[2].price + " coins/kg" +
                "\n4. Fish " + Store.foodList[3].price + " coins/kg" +
                "\n5. Grass " + Store.foodList[4].price + " coins/kg");

        Scanner foodScan = new Scanner(System.in);
        String temp = foodScan.nextLine();
        if (!temp.matches("[1-5]")) return "";

        int food = temp.compareTo("0");
        cleanSlate(30);
        System.out.println("How many kg of " + Store.foodList[food - 1].getClass().getSimpleName().toLowerCase() +
                " would you like to buy?\nThe max amount you can buy is " +
                (player.money / Store.foodList[food - 1].price) + "kg.");

        int kg = 0;
        try {
            kg = scan.nextInt();
            kg = Math.max(0, kg);
            kg = Math.min(player.money / Store.foodList[food - 1].price, kg);
        } catch (Exception ignore) {
        }
        player.buyFood(food - 1, kg);
        cleanSlate(30);
        System.out.println("You bought " + kg + "kg of " + Store.foodList[food - 1].getClass().getSimpleName().
                toLowerCase() + ". Would you like to buy more food? (y/n)");
        return scan.next();
    }

    public String feedAnimal(Player player) {
        cleanSlate(30);
        seeAnimalList(player, "hideContinueText");
        System.out.println("\nWhich animal do you want to feed?" + loseATurn);
        try {
            Scanner feedScan = new Scanner(System.in);
            int animal = feedScan.nextInt() - 1;
            cleanSlate(30);
            System.out.println("Which food would you like to give " + player.animals.get(animal).name + "?" + loseATurn);
            int i = 1;
            for (Food food : Store.foodList) {
                System.out.println(i++ + ": " + food.getClass().getSimpleName());
            }
            int food = feedScan.nextInt() - 1;
            cleanSlate(30);
            System.out.println("How many kg of " + Store.foodList[food].getClass().getSimpleName().toLowerCase() +
                    " would you like to feed " + player.animals.get(animal).name + "?\n" +
                    (player.animals.get(animal).gender == 0 ? "He" : "She") +
                    " can eat " + (player.animals.get(animal).howMuchFoodICanEat -
                    player.animals.get(animal).howMuchFoodIAteToday) + "kg. You have " +
                    player.foods.get(Store.foodList[food].getClass().getSimpleName()) + "kg");
            Scanner kgScan = new Scanner(System.in);
            int kg = kgScan.nextInt();
            kg = Math.max(0, kg);
            kg = Math.min(player.foods.get(Store.foodList[food].getClass().getSimpleName()), kg);
            kg = Math.min(player.animals.get(animal).howMuchFoodICanEat -
                    player.animals.get(animal).howMuchFoodIAteToday, kg);
            cleanSlate(30);
            System.out.println("You gave " + player.animals.get(animal).name + " " + kg + "kg of " +
                    Store.foodList[food].getClass().getSimpleName().toLowerCase());
            if (player.animals.get(animal).foodsICanEat.contains(food)) {
                player.feedAnimal(player.animals.get(animal), food, kg);
                player.animals.get(animal).loseHp = false;
                System.out.println("Tasty! " + player.animals.get(animal).name + " now has " +
                        player.animals.get(animal).health + "hp.");
            } else {
                String foodName = Store.foodList[food].getClass().getSimpleName();
                player.foods.put(foodName, player.foods.get(foodName) - kg);
                System.out.println("That was a waste of food... " + player.animals.get(animal).name + " can't eat " +
                        Store.foodList[food].getClass().getSimpleName() + " so " +
                        (player.animals.get(animal).gender == 0 ? "he " : "she ") + "didn't gain any hp.");
            }
            player.animals.get(animal).howMuchFoodIAteToday += kg;
        } catch (Exception Return) {
            return "";
        }
        System.out.println("Would you like to feed more animals? (y/n)");
        return scan.next();
    }

    public String sellAnimal(Player player) {
        cleanSlate(30);
        System.out.println("Which animal do you want to sell?" + loseATurn + "\n");
        int i = 1;
        for (Animal animal : player.animals) {
            System.out.println(i++ + ": " + animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() +
                    ": " + animal.calculatePrice() + " coins.");

        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int animal = animalScan.nextInt() - 1;
            cleanSlate(30);
            System.out.println("Where would you like to sell " + player.animals.get(animal).name + "?" + loseATurn +
                    "\n1. The Store\n2. Other Player");
            String number = animalScan.next();
            if (number.equals("1")) player.sellAnimal(player.animals.get(animal));
            else if (number.equals("2")) {
                cleanSlate(30);
                System.out.println("Which player do you want to sell " + player.animals.get(animal).name + " to?");
                for (int j = 1; j < Player.players.size(); j++) {
                    System.out.println(j + ". " + (Player.players.indexOf(player) < j ? Player.players.get(j).name :
                            Player.players.get(j - 1).name));
                }
                Scanner playerScan = new Scanner(System.in);
                int choice = playerScan.nextInt();
                choice = Player.players.indexOf(player) < choice ? choice : choice - 1;
                cleanSlate(30);
                System.out.println(Player.players.get(choice).name + ": Do you want to buy " +
                        player.animals.get(animal).name + " the " +
                        player.animals.get(animal).getClass().getSimpleName().toLowerCase() +
                        " for " + player.animals.get(animal).calculatePrice() + " coins? (y/n)");
                String yOrN = scan.next();
                if (yOrN.equals("y")) player.sellAnimal(player.animals.get(animal), Player.players.get(choice));
            } else return "";

        } catch (Exception Return) {
            return "";
        }
        if (player.animals.size() == 0) return "";
        System.out.println("Would you like to sell more animals? (y/n)");
        return scan.next();
    }

    public void mate(Player player) {
        cleanSlate(30);
        System.out.println("Pick two animals, separated by a space." +
                " They will try to make a few babies." + loseATurn);
        int i = 1;
        for (Animal animal : player.animals) {
            System.out.println(i++ + ": " + animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() +
                    "     Gender: " + animal.getGenderString() +
                    "     Possible babies: " + animal.numberOfPossibleBabies);
        }
        try {
            Scanner mateScan = new Scanner(System.in);
            String[] twoNumbers = mateScan.nextLine().split(" ");
            int animal1 = Integer.parseInt(twoNumbers[0]) - 1;
            int animal2 = Integer.parseInt(twoNumbers[1]) - 1;

            player.mateTwoAnimals(player.animals.get(animal1), player.animals.get(animal2));
        } catch (Exception ignore) {
        }
    }

    public void showWhoWon(ArrayList<Player> players) {
        cleanSlate(30);
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

    public int seeAnimalList(Player player, String hideContinueText) {
        cleanSlate(30);
        System.out.println(player.name + "'s AnimalList:\n");
        int i = 1;
        for (Animal animal : player.animals) {
            String name = animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() + ":";
            String gender = "Gender: " + animal.getGenderString();
            String age = "Age: " + animal.age + "/" + (int) animal.MAX_AGE;
            String health = "HP: " + animal.health + "/100";
            String price = "Price: " + animal.calculatePrice() + "/" + (int) animal.MAX_PRICE;
            String eats = "Eats: " + animal.showFoodsICanEat();

            System.out.println(i++ + ". " + name + " ".repeat(40 - charactersInWord(name, 0)) +
                    gender + " ".repeat(20 - charactersInWord(gender, 0)) +
                    age + " ".repeat(20 - charactersInWord(age, 0)) +
                    health + " ".repeat(20 - charactersInWord(health, 0)) +
                    price + " ".repeat(20 - charactersInWord(price, 0)) +
                    eats + " ".repeat(20 - charactersInWord(eats, 0)));
        }
        if (player.animals.size() == 0) System.out.println("You don't have any animals!");
        if (!hideContinueText.equals("hideContinueText")) {
            System.out.println("\nPress c followed by ENTER to continue!");
            scan.next();
        }
        return player.animals.size();
    }

    public void seeFoodList(Player player) {
        cleanSlate(30);
        System.out.println(player.name + "'s FoodList:\n");
        for (Food food : Store.foodList) {
            System.out.printf("%s %d%s  ", food.getClass().getSimpleName() + ":",
                    player.foods.get(food.getClass().getSimpleName()), "kg");
        }
        System.out.println("\n\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public void seeInfo() {
        cleanSlate(30);
        StringBuilder players = new StringBuilder();
        for (Player player : Player.players) {
            players.append(player.name).append(" ");
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

    public static int charactersInWord(String word, int number) {
        if (!word.equals("")) {
            return word.split("").length;
        }
        return String.valueOf(number).split("").length;
    }

}
