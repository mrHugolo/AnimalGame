package com.company.HelpClasses;

import com.company.Animal;

import java.util.*;

public class Dialogs {
    private final static Scanner scan = new Scanner(System.in);


    public static String menuWithLetters(String title, String ... options){
        System.out.println(title);
        for(int i = 0; i < options.length; i += 2){
            System.out.println(options[i] + " ".repeat(40 - charCounter(options[i])) + options[i + 1]);
        }
        String choice = scan.nextLine();
        if(options.length == 0) return choice;
        for(String option : options){
            if(choice.equals(option.split("")[0])) return choice;
        }
        return "default";
    }

    public static int menu(String title, String ... options){
        System.out.println(title);
        int tracker = 1;
        for(String option : options)
            System.out.println(tracker++ + ". " + option);
        int choice = 0;
        try{
            choice = Integer.parseInt(scan.next());
        }
        catch (Exception ignore){}
        return choice < 1 || choice > options.length ? menu(title, options) : choice;
    }

    public static void continuePlaying(){
        System.out.println("\nPress c followed by ENTER to continue!");
        scan.next();
    }

    public static boolean promptString(String question, String answer){
        System.out.println(question);
        String check = scan.next();
        return check.equals(answer);
    }

    public static int promptInt(String question, int min, int max){
        System.out.println(question + " (" + min + "-" + max + ")");
        int choice = min - 1;
        String temp = scan.next();
        try{choice = Integer.parseInt(temp);}
        catch (Exception ignore){}

        return choice < min || choice > max ? promptInt(question, min, max) : choice;
    }

    public static ArrayList<String> enterName(String message, int numberOfNames){
        System.out.println(message);

        Scanner nameScan = new Scanner(System.in);
        ArrayList<String> names = new ArrayList<>(Arrays.asList(nameScan.nextLine().split("_")));
        System.out.println(names.size());
        for(int i = 0; i < numberOfNames; i++) {
            int rand = (int) (Math.random() * AnimalNames.animalNames.length);
            try {
                String name = charCounter(names.get(i)) < 2 || charCounter(names.get(i)) > 20 ?
                        AnimalNames.animalNames[rand] : names.get(i);
                names.set(i, name);
            }
            catch (Exception e){
                names.add(i, "");
                names.set(i, AnimalNames.animalNames[rand]);
            }
        }
        while(names.size() > numberOfNames) names.remove(names.size() - 1);
        System.out.println(names.size());
        return names;
    }

    public static String animalName(Animal animal){
        return animal.getName() + " the " + animal.getClass().getSimpleName().toLowerCase();
    }

    public static void cleanSlate(int invisibleAt27){
        System.out.println("\n".repeat(invisibleAt27));
    }

    public static int charCounter(String word) {
        return word.split("").length;
    }

    public static void rules(){
        System.out.println("\n\nWelcome to AniMall!\n" +
                "Read through the rules and press ENTER to start the game.\n\n" +
                "You will decide how many rounds you will play (5-30) and how many players will play (1-4).\n" +
                "Each player will start with 50.000 coins and are free to purchase different animals and food " +
                "for these animals. Remember that you have to spend money to gain money!\n" +
                "Every player has a choice each round to either buy animals, buy food, feed your existing animals, " +
                "sell your animals or politely ask your existing animals to make more animals.\n" +
                "Although animals obviously need to be of age to have babies of their own and the age of consent " +
                "in the animal kingdom is four rounds old.\n" +
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
        Dialogs.cleanSlate(30);
    }


}
