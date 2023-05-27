import java.util.*;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    
    public Player(String name){
        this.name = name;
        hand = new ArrayList<Card>();
    }
    
    public void addCard(Card card){
        hand.add(card);
    }
    
    public Card removeCard(int x){
        return hand.remove(x);
    }
    
    public Card seeCard(int x){
        return hand.get(x);
    }
    
    public int handSize(){
        return hand.size();
    }
    
    public boolean hasRank(String rank){
        for (int x=0 ;x < hand.size(); x++){
            if(hand.get(x).getRank().equals(rank)){
                return true;
            }
        }
        return false;
    }
    
    public int findRank(String rank){
        for (int x=0;x<hand.size();x++){
            if(hand.get(x).getRank().equals(rank)){
                return x;
            }
        }return 400000;
    }
    
    public boolean hasValid(Card up){
        for (int x=0;x<hand.size();x++){
            if(hand.get(x).canPlace(up)){
                return true;
            }
        }return false;
    }
    
    public void printHand(){
        for (int c=0;c<hand.size();c++){
            System.out.println(c + " => " + hand.get(c));
        }
    }
}
