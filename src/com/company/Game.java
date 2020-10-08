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
            for (Player player : Player.players) {
                if(player.checkIfPlayerDied()) {
                    Dialogs.continuePlaying();
                    continue;
                }
                readOutVeterinarianBills(player);
                chooseWhatToDo(player, "");
            }
            for (Player player : Player.players) {
                for (Animal animal : player.animals) {
                    if(animal.endOfTurn()) player.getVeterinarianBill(animal);
                }
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
        int bills = player.veterinarianBill.size();
        if (bills == 0) return;

        Dialogs.cleanSlate(30);
        System.out.println(player.name + ": You have " + bills + " sick animal" + (bills == 1 ? "." : "s."));
        for (Animal animal : player.veterinarianBill) {
            Dialogs.cleanSlate(30);
            System.out.println(Dialogs.animalName(animal));
        }
            Dialogs.continuePlaying();
        for(Animal animal : player.animals) {
            System.out.println(animal.name + " is sick. If you don't pay " + animal.veterinarianCost + " coins, " +
                    (animal.gender == 0 ? "he" : "she") + " will die! Even if you do pay, there is still " +
                    animal.chanceOfDeath + " % chance that " + (animal.gender == 0 ? "he" : "she") + " will die!" +
                    "\nYou have " + player.money + " coins.");

            if (Dialogs.promptString("Would you like to pay the bill? (y/n)", "y")) player.payBill(animal);
            else player.kill(animal);
            Dialogs.continuePlaying();
        }

    }

    public void sellAllAnimals(){
        for (Player player : Player.players) {
            for (Animal animal : player.animals) {
                player.sellAnimal(animal);
            }
        }
    }

    //Put number as "2" if you want case "2".
    public void chooseWhatToDo(Player player, String ... number) {
        Dialogs.cleanSlate(30);
        String choice;
        if (number.length == 0) {
            choice = Dialogs.menuWithLetters(player.name +
                            ": You have " + player.money +" coins. " +
                            "Enter the number/letter next to the action you want to do!\n",

                    "1. Buy Animals", "a. Show Animal List",
                    "2. Buy Food", "f. Show Food List",
                    "3. Feed Animals", "i. Show Info",
                    "4. Sell Animals", "s. Skip Your Turn",
                    "5. Pick Two Animals to Mate", "");
        }
        else choice = number[0];

        switch (choice) {
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

    public String buyAnimal(Player player) {
        Dialogs.cleanSlate(30);
        System.out.println(player.name + ": From where would you like to buy your animal?" + Dialogs.loseATurn +
                "\n1. The Store\n2. Other Players");
        String choice = scan.next();
        if (choice.equals("1")) {
            if (!buyAnimalFromStore(player).equals("I bought an animal.")) return "";
            Dialogs.cleanSlate(30);
        } else if (choice.equals("2")) {
            String buyAnimalFromPlayer = buyAnimalFromPlayer(player);
            if (buyAnimalFromPlayer.equals("Try again.")) {/*do nothing*/}
            else if (!buyAnimalFromPlayer.equals("I bought an animal.")) return "";
            else Dialogs.cleanSlate(30);

        } else return "";

        System.out.println(player.name + ": Would you like to buy more animals? (y/n)");
        Scanner returnScan = new Scanner(System.in);
        return returnScan.next();
    }

    public String buyAnimalFromStore(Player player) {
        Dialogs.cleanSlate(30);
        System.out.println("What animal would you like to buy?" + Dialogs.loseATurn + "\n");
        int i = 1;
        for (Animal animal : Store.animalList) {
            System.out.println(i++ + ": " + animal.name + " " + animal.calculatePrice() + " coins.");
        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int animal = animalScan.nextInt() - 1;
            Dialogs.cleanSlate(30);
            System.out.println("Which gender should your " + Store.animalList[animal].name.toLowerCase() +
                    " be?" + Dialogs.loseATurn + "\n1. Male\n2. Female");
            int gender = animalScan.nextInt() - 1;
            Dialogs.cleanSlate(30);
            System.out.println("Please enter in a name for your " +
                    (gender == 0 ? "male " : "female ") + Store.animalList[animal].name.toLowerCase() +
                    " (1-20 characters or I will pick a name for you!)");
            Scanner nameScan = new Scanner(System.in);
            String name = nameScan.nextLine();
            if (Dialogs.charCounter(name, 0) > 20 || Dialogs.charCounter(name, 0) == 0)
                name = (AnimalNames.animalNames[(int) (Math.random() * 4)]);
            player.buyAnimal(Objects.requireNonNull(Store.createAnimal(Store.animalList[animal].name, name, gender)));
        } catch (Exception Return) {
            return "";
        }

        return "I bought an animal.";
    }

    public String buyAnimalFromPlayer(Player player) {
        Dialogs.cleanSlate(30);
        System.out.println("\nWhich player do you want to buy from?" + Dialogs.loseATurn);

        for (int i = 1; i < Player.players.size(); i++) {
            System.out.println(i + ". " + (Player.players.indexOf(player) < i ? Player.players.get(i).name :
                    Player.players.get(i - 1).name));
        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int number = animalScan.nextInt();
            number = Player.players.indexOf(player) < number ? number : number - 1;
            if (seeAnimalList(Player.players.get(number), "hideContinueText") != 0) {
                System.out.println("\nWhich animal would you like to buy?" + Dialogs.loseATurn);
            } else return "Try again.";

            int animal = animalScan.nextInt() - 1;
            if (player.buyAnimal(Player.players.get(number).animals.get(animal), Player.players.get(number)) != -1) {
                Dialogs.cleanSlate(30);
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
        Dialogs.cleanSlate(30);
        String[] options = new String[Store.foodList.length];
        for(int i = 0; i < Store.foodList.length; i++)
            options[i] = Store.foodList[i].name + " " + Store.foodList[i].price + " coins/kg";
        int food = Dialogs.menu("What kind of food would you like to buy?", options) - 1;

        Dialogs.cleanSlate(30);
        int kg = Dialogs.promptInt("How many kg of " + Store.foodList[food].name +
                " would you like to buy?", 0, (player.money / Store.foodList[food].price));
        player.buyFood(food, kg);
        Dialogs.cleanSlate(30);

        if(Dialogs.promptString("You bought " + kg + "kg of " + Store.foodList[food].name +
                ". Would you like to buy more food? (y/n)", "y")) return "y";
        return"";
    }

    public String feedAnimal(Player player) {
        Dialogs.cleanSlate(30);
        seeAnimalList(player, "hideContinueText");
        System.out.println("\nWhich animal do you want to feed?" + Dialogs.loseATurn);
        try {
            Scanner feedScan = new Scanner(System.in);
            int animal = feedScan.nextInt() - 1;
            Dialogs.cleanSlate(30);
            System.out.println("Which food would you like to give " +
                    player.animals.get(animal).name + "?" + Dialogs.loseATurn);
            int i = 1;
            for (Food food : Store.foodList) {
                System.out.println(i++ + ": " + food.getClass().getSimpleName());
            }
            int food = feedScan.nextInt() - 1;
            Dialogs.cleanSlate(30);
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
            Dialogs.cleanSlate(30);
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
        Dialogs.cleanSlate(30);
        System.out.println("Which animal do you want to sell?" + Dialogs.loseATurn + "\n");
        int i = 1;
        for (Animal animal : player.animals) {
            System.out.println(i++ + ": " + animal.name + " the " + animal.getClass().getSimpleName().toLowerCase() +
                    ": " + animal.calculatePrice() + " coins.");

        }
        try {
            Scanner animalScan = new Scanner(System.in);
            int animal = animalScan.nextInt() - 1;
            Dialogs.cleanSlate(30);
            System.out.println("Where would you like to sell " + player.animals.get(animal).name + "?" + Dialogs.loseATurn +
                    "\n1. The Store\n2. Other Player");
            String number = animalScan.next();
            if (number.equals("1")) player.sellAnimal(player.animals.get(animal));
            else if (number.equals("2")) {
                Dialogs.cleanSlate(30);
                System.out.println("Which player do you want to sell " + player.animals.get(animal).name + " to?");
                for (int j = 1; j < Player.players.size(); j++) {
                    System.out.println(j + ". " + (Player.players.indexOf(player) < j ? Player.players.get(j).name :
                            Player.players.get(j - 1).name));
                }
                Scanner playerScan = new Scanner(System.in);
                int choice = playerScan.nextInt();
                choice = Player.players.indexOf(player) < choice ? choice : choice - 1;
                Dialogs.cleanSlate(30);
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
        Dialogs.cleanSlate(30);
        System.out.println("Pick two animals, separated by a space." +
                " They will try to make a few babies." + Dialogs.loseATurn);
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

    public int seeAnimalList(Player player, String hideContinueText) {
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

            System.out.println(i++ + ". " + name + " ".repeat(40 - Dialogs.charCounter(name, 0)) +
                    gender + " ".repeat(20 - Dialogs.charCounter(gender, 0)) +
                    age + " ".repeat(20 - Dialogs.charCounter(age, 0)) +
                    health + " ".repeat(20 - Dialogs.charCounter(health, 0)) +
                    price + " ".repeat(20 - Dialogs.charCounter(price, 0)) +
                    eats + " ".repeat(20 - Dialogs.charCounter(eats, 0)));
        }
        if (player.animals.size() == 0) System.out.println("You don't have any animals!");
        if (!hideContinueText.equals("hideContinueText")) {
            System.out.println("\nPress c followed by ENTER to continue!");
            scan.next();
        }
        return player.animals.size();
    }

    public void seeFoodList(Player player) {
        Dialogs.cleanSlate(30);
        System.out.println(player.name + "'s FoodList:\n");
        for (Food food : Store.foodList) {
            System.out.printf("%s %d%s  ", food.getClass().getSimpleName() + ":",
                    player.foods.get(food.getClass().getSimpleName()), "kg");
        }
        System.out.println("\n\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public void seeInfo() {
        Dialogs.cleanSlate(30);
        StringBuilder players = new StringBuilder();
        for (Player player : Player.players) {
            players.append(player.name).append(" ");
        }
        System.out.println("Rounds played: " + round + "/" + rounds + "     Players left: " + players +
                "\n\nAnimals:        MaxAge:         MaxPrice:       Eats:");
        for (Animal animal : Store.animalList) {
            System.out.println(animal.getClass().getSimpleName() + " ".repeat(16 - Dialogs.charCounter(
                    animal.getClass().getSimpleName(), 0)) +
                    (int) animal.MAX_AGE + " ".repeat(16 - Dialogs.charCounter("", (int) animal.MAX_AGE)) +
                    (int) animal.MAX_PRICE + " ".repeat(16 - Dialogs.charCounter("", (int) animal.MAX_PRICE)) +
                    animal.showFoodsICanEat());
        }
        System.out.println("\nPress c followed by ENTER to continue!");
        scan.next();
    }

}
