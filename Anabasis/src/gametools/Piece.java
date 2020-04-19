package gametools;

import java.io.Serializable;
import java.util.List;

/**The attributs and methods of this class are common for both types of pieces*/
public abstract class Piece implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6590308549295742342L;
	/**the player who owns the piece*/
	private Player owner;
	/**the card of which this piece is placed*/
	private Card place;
	/**indicates that another piece reached a water spot and is to move next turn*/
	private boolean another_must_move;
	/**indicates the remaining number of turns a piece stays blocked*/
	private int blocked;

	/**sets attributes false and the defines the owner of the piece,
	  *enlists to his army and gives the piece its type-name*/
	public Piece(Player p, Card c){
		blocked = 0;
		another_must_move = false;
		owner = p;
		owner.addPiece(this);
		place = c;
	}

	/**checks if this piece is still blocked*/
	public boolean blocked(){if(blocked>0){return true;}else{return false;}}
	/**blocks this piece*/
	public void block(){blocked = 2;}
	/**deblocks a piece by one turn*/
	public void deblock(){if(blocked>0){blocked--;}else{}}
	/**checks if another piece must move*/
	public boolean anotherMustMove(){return another_must_move;}
	/**changes the must_move status of this piece*/
	public void setAnotherMustMove(boolean b){another_must_move = b;}
	/**gets the Player who owns the piece*/
	public Player getOwner(){return owner;}
	/**gets the name of the piece*/
	public abstract String getName();
    /**see if it's an archon*/
    public abstract boolean isArchon();
	/**gets the card on which the piece is*/
	public Card getCard(){return place;}
	/**actually trys to move the piece
	  *this method is private since it is only used by the move methods which assure the necessary checks*/
	public boolean move(int x, int y){//the square exists
		Card newplace = owner.getGame().getCard(x,y);
		boolean possible = newplace.accessPossible(this);
		if(possible){
			newplace.pieceComes(this);
			place.pieceLeaves(this);
			place = newplace;
			place.turn();
		}else{}
		return possible;
	}//end move
    /**is this piece covered by another piece*/
    public boolean isCovered(){
        List<Card> v = place.getNeighbors();
        boolean found = false;
        for(Card card : v){
            Piece one = card.getPiece();
            Piece two = card.getPieceTwo();
            if(one!=null){
                if(one.getOwner()==owner){found=true;}
            }
            if(two!=null){
                if(two.getOwner()==owner){found=true;}
            }
        }
        return found;
    }
    /**is this piece under fire by an enemy piece*/
    public boolean underFire(){
        List<Card> v = place.getNeighbors();
        boolean found = false;
        for(Card card : v){
            Piece one = card.getPiece();
            Piece two = card.getPieceTwo();
            if(one!=null){
                if((one.getOwner()!=owner)&&(!one.blocked())){found=true;}
            }
            if(two!=null){
                if(two.getOwner()!=owner){found=true;}
            }
        }
        return found;
    }
}