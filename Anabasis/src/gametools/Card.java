package gametools;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**The attributs and methods of this class are common for all types of cards*/
abstract public class Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 488245748304372868L;
	/**the piece that can be this card*/
	private Piece piece;
	/**true if the card is turned*/
	private boolean turned;
	/**the x coordinate on the map*/
	private int x;
	/**the y coordinate on the map*/
	private int y;
    /**the game this card is used in*/
    private Game game;

	/**constructor using the coordinates of the card
     * sets the card unturned and the piece to null*/
	public Card(int xco, int yco, Game g){
        turned = false;
        piece = null;
        x = xco;
        y = yco;
        game = g;
    }

	/**reads the attribute turned*/
	public boolean isTurned(){return turned;}
	/**turns a card, so it simply sets the attribute turned = true*/
	public void turn(){turned = true;}
	/**reads the name of a card*/
	public abstract String getName();
	/**sets a piece on this card without consequences*/
	public void setPiece(Piece p){piece = p;};
	/**tests if a piece can move to this square*/
	public abstract boolean accessPossible(Piece p);
	/**a piece moving on this card*/
	public abstract void pieceComes(Piece p);
	/**a piece moving away from this card*/
	public abstract void pieceLeaves(Piece p);
	/**abstract method to get the first piece*/
	public Piece getPiece(){return piece;}
	/**abstract method to get the second piece*/
	public abstract Piece getPieceTwo();
	/**abstract method to set the second piece*/
	public abstract void setPieceTwo(Piece p);
	/**abstract method to get the owner which is needed for the towns*/
	public abstract Player getOwner();
	/**gets the x coordinate*/
	public int getX(){return x;}
	/**gets the y coordinate*/
	public int getY(){return y;}
    /**gets the game*/
    public Game getGame(){return game;}
    /**method to get all the neighboring cards for this square*/
    public List<Card> getNeighbors(){
        List<Card> neighbors = new Vector<Card>();
        for(int d=1;d<9;d++){
            int dx = getX();
            int dy = getY();
            switch(d)
            {
                case 1: dx--;dy--;break;//SW
                case 2:      dy--;break;//W
                case 3: dx++;dy--;break;//NW
                case 4: dx++;     break;//N
                case 5: dx++;dy++;break;//NE
                case 6:      dy++;break;//E
                case 7: dx--;dy++;break;//SE
                case 8: dx--;     break;//S
            }
            if((dx<8)&&(dx>=0)&&(dy<8)&&(dy>=0)){neighbors.add(getGame().getCard(dx,dy));}
        }
        return neighbors;
    }
    /**method to calculate all the ways leading away from this square*/
    public Vector<Vector<Card>> getWays(){
        Vector<Vector<Card>> ways = new Vector<Vector<Card>>();
        //get all the neigboring squares
        List<Card> neighbors = this.getNeighbors();
        //transform them into way-Vectors and add them to ways
        for(Card card : neighbors){
            Vector<Card> way = new Vector<Card>();
            way.add(card);
            ways.add(way);
        }
        boolean found = true;
        //as long as one way could be any longer
        while(found){
            found = false; //for this time we have not yet found anything
            //check out all ways
            int count = 0;
            while(ways.size()>count){
                Vector<Card> way = ways.get(count);
                //get the last step
                Card card = (Card)way.elementAt(way.size()-1);
                //check if it is water and turned
                //remove it from ways because it will be either splitted up and added again
                //or in case of a check for a similar target square, you don't want a check with itself
                count--;
                ways.remove(way);
                if((card.getName().charAt(0)=='w')&&(card.isTurned())){
                    //get the neighbors of the last step
                    neighbors = card.getNeighbors();
                    //check out if one of them is already part of this way
                    for(Card c : neighbors){
                        if(!way.contains(c)){
                            @SuppressWarnings("unchecked")
							Vector<Card> newway = (Vector<Card>) way.clone();
                            newway.add(c);
                            ways.add(newway);
                            found = true;
                        }
                    }
                } else {//this card is a target square so check out if it already exists
                    boolean stays = true;
                    for(Enumeration<Vector<Card>> F = ways.elements(); F.hasMoreElements() ;){
                        Vector<Card> testway = F.nextElement();
                        Card testcard = (Card)testway.elementAt(testway.size()-1);
                        if((card.getX()==testcard.getX())&&(card.getY()==testcard.getY())){//they are the same
                            if(testway.size()>way.size()){//remove the testway
                                //ajust the count
                                if(ways.indexOf(testway)<count){count--;}
                                ways.remove(testway);
                            } else {
                                stays=false;
                            }
                        }
                    }
                    if(stays){
                        count++;
                        ways.add(count, way);
                    }
                }
                count++;
            }
        }
        return ways;
    }
    /**returns a Vector containing all pieces that cover a given square
     * as parameters you have the player for whom the quest is done and
     * a flag to decide whether you are looking for his own pieces or the enemies*/
    public List<Piece> getCoveringPieces(Player p, boolean own){
        List<Piece> pieces = new Vector<Piece>();
        Vector<Vector<Card>> ways = this.getWays();
        int count=0;
        while(ways.size()>count){
           Vector<Card> way = ways.elementAt(count);
           Card c = (Card)way.elementAt(way.size()-1);
           if(c.getPiece()!=null){
               if(own&&(c.getPiece().getOwner()==p)){pieces.add(c.getPiece());}
               if(!own&&(c.getPiece().getOwner()!=p)){pieces.add(c.getPiece());}
           }
           if(c.getPieceTwo()!=null){
               if(own&&(c.getPieceTwo().getOwner()==p)){pieces.add(c.getPieceTwo());}
               if(!own&&(c.getPieceTwo().getOwner()!=p)){pieces.add(c.getPieceTwo());}
           }
           count++;
        }
        //throwing out the pieces on this squere
        if(this.getPiece()!=null){
            if(pieces.contains(this.getPiece())){pieces.remove(this.getPiece());}
        }
        if(this.getPieceTwo()!=null){
            if(pieces.contains(this.getPieceTwo())){pieces.remove(this.getPieceTwo());}
        }
        return pieces;
    }

	@Override
	public String toString() {
		return getName()+"[" + x + "," + y + "]";
	}
    
}
