package datatools;

import gametools.Card;
import gametools.Piece;

/**a possible move in a game by a certain player
 * to be used by the engines */
public class Move {
    /**the piece to be moved*/
    private Piece piece;
    /**the card where the move starts from*/
    private Card from;
    /**the card where the move goes to*/
    private Card to;
    /**the probability that this move will be played*/
    private int probability;
    //to test
    private String comment = "";

    /**constructor*/
    public Move(Piece p, Card f, Card t){
        piece = p;
        from = f;
        to = t;
        probability = 100;
    }

    /**access to the piece*/
    public Piece getPiece(){return piece;}
    /**access to the start*/
    public Card getFrom(){return from;}
    /**access to the destination*/
    public Card getTo(){return to;}
    /**access to the probability attribute*/
    public int getProb(){return probability;}
    /**write probability attribute*/
    public void setProb(int p){probability = p;}

    //test
    public String getCom(){return comment;}
    public void setCom(String c){comment = c;}

}
