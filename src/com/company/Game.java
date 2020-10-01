package com.company;

import java.util.*;

public class Game {
    Scanner scan = new Scanner(System.in);

    private int rounds = 10;
    private int players = 1;

    public Game(){
        rules();
        decideRoundsAndPlayers();

    }

    public void rules(){
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
                "When a player neither have animals nor coins, they are eliminated from the game " +
                    "otherwise the game is over after the agreed number of rounds have been played.\n" +
                "At that time all remaining animals will be sold and the player with the most coins win!");
        scan.nextLine();
        cleanSlate(27);
    }

    public void decideRoundsAndPlayers(){
        System.out.println("How many rounds will you be playing? (Write a number between 5-30 and press ENTER)");
        try{
            int temp = scan.nextInt();
            rounds = Math.max(5, temp);
            rounds = Math.min(30, rounds);
            System.out.println("You will play " + rounds + " rounds!");
        }
        catch (Exception e){
            System.out.println("I don't even know what that could mean... I assume you want to play 10 rounds!");
        }

        System.out.println("How many players will be playing? (Write a number between 1-4 and press ENTER)");
        try{
            int temp = scan.nextInt();
            players = Math.max(1, temp);
            players = Math.min(4, players);
            System.out.println("There will be " + players + " players playing!");
        }
        catch (Exception e){
            System.out.println("I don't even know what that could mean... I guess you'll have to play alone!");
        }
        //for-loop won't let you write name for last player so add 1 to players
        for(int i = 1; i <= players + 1; i++){
            new Player(scan.nextLine());
            System.out.println("Player " + i + ", please enter your name.");
        }
        cleanSlate(27);
    }

    public void cleanSlate(int invisibleAt27){
        System.out.println("\n".repeat(invisibleAt27));
    }

}
