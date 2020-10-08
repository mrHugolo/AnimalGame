package com.company.HelpClasses;

import java.util.*;

public class Dialogs {
    private final static Scanner scan = new Scanner(System.in);



    public static void continuePlaying(){
        System.out.println("Press c followed by ENTER to continue!");
        scan.next();
    }

    public static int promptInt(String question, int min, int max){
        System.out.println(question + " (" + min + "-" + max + ")");
        int choice = min;
        try{choice = scan.nextInt();}
        catch (Exception ignore){}
        choice = Math.max(min, choice);
        choice = Math.min(max, choice);
        return choice;
    }

    public static void cleanSlate(int invisibleAt27){
        System.out.println("\n".repeat(invisibleAt27));
    }

    public static int charCounter(String word, int number) {
        if (!word.equals("")) {
            return word.split("").length;
        }
        return String.valueOf(number).split("").length;
    }
    //loseATurn is a red warning text
    public final static String loseATurn = "\u001B[31m\n" +
            "If you type a number/letter that doesn't correspond to any available option," +
            " you will lose your turn.\n\u001B[0m";

    public static void rules(){
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
        Dialogs.cleanSlate(30);
    }


}
