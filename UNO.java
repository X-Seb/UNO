import java.util.*;

public class UNO
{
    // Static variables
    public static Scanner scanner = new Scanner(System.in);
    public static Deck deck = new Deck(1);
    public static Deck discard = new Deck(true);
    public static ArrayList<Player> players = new ArrayList<Player>();
    public static int playerNum;
    
    public static void main(String[] args)
    {
        // Randomize the deck
        deck.shuffle();
        
        // Print out the rules
        gameRules();
        
        // Get number of players, then setup the game
        boolean valid = false;
        while (!valid){
            try {
                System.out.println("How many people are playing? (2-10)");
                playerNum = scanner.nextInt();
            }  catch (Exception e) {
                System.out.println("Make sure to enter an Integer! ");
                playerNum = -1;
            }
            
            if (playerNum <= 10 && playerNum >= 2){
                valid = true;
            }
        }
        setup(playerNum);
        
        
        // Start playing
        playGame();
    }
    
    public static void gameRules(){
        // Prints out all the game rules
        System.out.println();
        System.out.println("Welcome to UNO! Here are the game rules: ");
        System.out.println("You are playing against other players.");
        System.out.println("The goal is to be the first player to get rid of all your cards.");
        System.out.println("You must play a card that matches the color or value of the current card on the table.");
        System.out.println("If you don't have a matching card, you must draw from the deck.");
        System.out.println("Special cards have specific effects: Skip skips the next player's turn, Reverse changes the direction of play, and Draw Two makes the next player draw two cards. Draw Four and Wild let you choose the color going forward.");
        System.out.println("Let's begin!\n");
        System.out.println();
    }
    
    public static void setup(int numPlayer){
        // Create new player objects for each player, add 7 cards to each player's hand, add each player to the ArrayList called "players".
        for (int i = 0; i < numPlayer; i++){
            Player p = new Player("Player " + (i + 1));
            for (int j = 0; j < 7; j++) {
                p.addCard(deck.dealCard());
            }
            players.add(p);
        }
    }
    
    /*
    * Takes a parameter for the player index that will draw a card
    * Adds a card to that player's hand from the deck
    * Prints out a line that sais who drew a card and what card they got
    * no return value
    */
    public static void draw(int x){
        Card c = deck.dealCard();
        players.get(x).addCard(c);
        System.out.println("Player " + (x+1) + " drew a " + c);
    }
    
    /*
    * The method that controls the entire game
    * Most of the functionality is contained in the while loop that executes for each player turn
    * no return value
    */
    public static void playGame(){
        // Variables
        int winner = 0; int temp = 0; boolean skip = false;
        Card fish = new Card("idc","idc");
        
        // Add the top card of the deck to the discard pile
        while(!fish.getSuit().equals("Wild")){
            fish = deck.dealCard();
            if (!fish.getSuit().equals("Wild")){
                discard.add(fish);
            }else{deck.add(fish);}
            temp++;
        }
        
        // Variables
        Card top = new Card("x", "x");
        Card examining = new Card("x", "x");
        String choice = "";
        int pileup=0;
        int putype=0;
        boolean clockwise = true;
        int x = 0;
        scanner.nextLine(); // THIS FIXES EVERYTHING
            
        // Keep playing until a player wins
        while(winner<1){
            
            // Print out the information for the current turn
            top = discard.get(discard.size() - 1);
            System.out.println();
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println("It's the turn of player " + (x + 1) + ".");
            System.out.println();
            System.out.println("The card in play is " + top);
            System.out.println();
            System.out.println("Here's player " +  (x + 1) + "'s hand (" + players.get(x).handSize() + " cards in hand):");
            players.get(x).printHand();
            System.out.println();
            
            if(!skip){
                System.out.println("What card do you want to play? Enter the INDEX of the card to play. \nOr type 'draw' to draw a card and end your turn.)");
                choice = scanner.nextLine().toLowerCase();
                System.out.println();
                
                if (choice.equals("draw") || !players.get(x).hasValid(top)){
                    draw(x);
                } else {
                    // Make sure the player's choice is a valid index
                    try { 
                        Integer choiceInt = Integer.parseInt(choice);
                        if (choiceInt < 0) {choiceInt = 0;} 
                        else if (choiceInt >= players.get(x).handSize()) {
                            choiceInt = players.get(x).handSize() - 1;
                        } 
                        examining = players.get(x).seeCard(choiceInt);
                    
                        // Make sure the card can be played
                        if (examining.canPlace(top)) {
                            // Wild card
                            if(examining.getSuit().equals("Wild")){
                                System.out.println("Choose a color. (Red, Green, Blue, Yellow)");
                                choice = scanner.nextLine().substring(0,1).toLowerCase();
                                if(choice.equals("y")){
                                    players.get(x).seeCard(choiceInt).setSuit("Yellow");
                                }else if(choice.equals("b")){
                                    players.get(x).seeCard(choiceInt).setSuit("Blue");
                                }else if(choice.equals("g")){
                                    players.get(x).seeCard(choiceInt).setSuit("Green");
                                }else{players.get(x).seeCard(choiceInt).setSuit("Red");}
                            }
                        
                            // Attack cards
                            if(examining.getRank().equals("+4")){pileup=4; putype=4; skip=true;}
                            if(examining.getRank().equals("+2")){pileup=2; putype=2; skip=true;}
                            if(examining.getRank().equals("Skip")){skip=true;}
                            if(examining.getRank().equals("Reverse")){if(playerNum>2){if(clockwise){clockwise=false;}else{clockwise=true;}}else{skip=true;}}
                            
                            // Play the chosen card
                            System.out.println("Player " + (x+1) + " places " + players.get(x).seeCard(choiceInt) + " atop of " + top + ".");
                            discard.add(players.get(x).removeCard(choiceInt));
            
                        }else{
                            System.out.println("Player " + (x + 1) + " will draw instead.");
                            draw(x);}
                    }
                    // If card index is invalid, draw a card
                    catch (Exception e) {
                        System.out.println("Player " + (x + 1) + " will draw instead.");
                        draw(x);
                    }
                }
            }else{
                if(pileup>0){
                    if(players.get(x).hasRank("+2")&&putype==2){
                        System.out.println("You're assaulted by a +2, but you can pass it on to the next person! Counter with another +2? (y/n)");
                        choice = scanner.nextLine().substring(0,1).toLowerCase();
                            if (choice.equals("y")) {
                                temp = players.get(x).findRank("+2");
                                discard.add(players.get(x).removeCard(temp));
                                pileup+=2;
                            }else{
                                while(pileup>0){
                                    draw(x);
                                    pileup--;
                                }
                                System.out.println("Your turn is skipped.");skip=false;
                            }
                    }else if(players.get(x).hasRank("+4")&&putype==4){
                        System.out.println("You're assaulted by a +4, but you can pass it on to the next person! Counter with another +4? (y/n)");
                        choice = scanner.nextLine().substring(0,1).toLowerCase();
                            if (choice.equals("y")) {
                                temp = players.get(x).findRank("+4");
                                System.out.println("Choose a color. (Red, Green, Blue, Yellow)");
                                choice = scanner.nextLine().substring(0,1).toLowerCase();
                                if(choice.equals("y")){
                                    players.get(x).seeCard(temp).setSuit("Yellow");
                                }else if(choice.equals("b")){
                                    players.get(x).seeCard(temp).setSuit("Blue");
                                }else if(choice.equals("g")){
                                    players.get(x).seeCard(temp).setSuit("Green");
                                }else{players.get(x).seeCard(temp).setSuit("Red");}
                                discard.add(players.get(x).removeCard(temp));
                                pileup+=4;
                                
                            }else{
                                while(pileup>0){
                                    draw(x);
                                    pileup--;
                                }
                                System.out.println("Your turn is skipped.");skip=false;}
                            }else{
                                while(pileup>0){
                                    draw(x);
                                    pileup--;
                                }
                                System.out.println("Your turn is skipped.");skip=false;
                            }
                    }else{
                        System.out.println("You have been skipped! Onto the next player.");
                        skip = false;
                    }
                }
                
                if (players.get(x).handSize() <= 0){ winner = (x + 1); break;}
                
                // Adjust x for the next player's turn
                    if (clockwise){ x++; } else { x--; }
                if (x > playerNum - 1){ x = 0; }
                if (x < 0){x = playerNum - 1; }
            }
            
        // See if the player won
        System.out.println();
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("Player " + winner + " wins!");
        System.out.println();
        System.out.println("Thanks for playing!");
        }
    }
