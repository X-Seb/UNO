public class Card {
    
    private String suit;
    private String rank;
    
    // 2 parameter Card constructor
    public Card(String suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }
    
    // Getter methodds:
    
    public String getSuit(){
        return this.suit;
    }
    
    public String getRank(){
        return this.rank;
    }
    
    public boolean canPlace(Card other){
        if(this.suit.equals("Wild")){return true;}
        if(other.getSuit().equals(this.suit)){return true;}
        if(other.getRank().equals(this.rank)){return true;}
        return false;
    }
    
    // Setter methods:
    
    public void setSuit(String suit){
        this.suit = suit;
    }
    
    public void setRank(String rank){
        this.rank = rank;
    }
    
    // Override toString method
    @Override
    public String toString(){
        return "[ " + this.rank + " of " + this.suit + " ]";
    }
    
    public boolean equalsRank(Card other){
        if(this.rank.equals(other.getRank())){
            return true;
        }return false;
    }
    
    public boolean equalsSuit(Card other){
        if(this.suit.equals(other.getSuit())){
            return true;
        }return false;
    }
}
