package gametools.cards;

import gametools.*;
import gametools.pieces.Hoplite;
import java.io.Serializable;

/**This class admistrates all cards of the type town*/
public class Town extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1642484722125079566L;
	/**another piece that also can be on this card*/
	private Piece pieceTwo;
	/**Player who owns this town*/
	private Player owner;

	/**constructor using the coordinates of the card
     * it also sets the second piece and the owner to null*/
	public Town(int x,int y, Game g)
	{
		super(x,y,g);
		pieceTwo = null;
		owner = null;
	}

	/**tests if a piece can move to this square*/
	public boolean accessPossible(Piece p)
	{
		boolean possible;
		Piece piece = getPiece();
		if(!(piece==null))
		{
			if(p.getOwner()==piece.getOwner()){possible = false;}
			else
			{
				possible = true;
			}
		}
		else
		{
			possible = true;
		}
		return possible;
	}
	/**specifies the action if a piece moves to this card
	  *returns true if the move is possible
	  *if the piece coming to the town is an archon and he has less than 7 pieces,
	  *he gets an extra hoplite and one of them has to move next turn*/
	public void pieceComes(Piece p)
	{
		Piece piece = getPiece();
		if(!(piece==null)){piece.getOwner().killPiece(piece);}else{}
		setPiece(p);
		if((p.isArchon())&&(owner!=p.getOwner()))
		{
			owner = p.getOwner();
			if(!owner.isComplete())
			{
				Hoplite h = new Hoplite(owner, owner.getType(), this);
				pieceTwo = h;
				owner.setSupplies(true);
			}
			//if no new piece is created the moving conditions have to be reset because there might have been a move from town to town
			else
			{
				p.getOwner().setArmyImmovable(false);
				p.getOwner().setSupplies(false);
			}
		}
		else
		{
			p.getOwner().setArmyImmovable(false);
			p.getOwner().setSupplies(false);
		}
	}
	/**a piece moving away from this card*/
	public void pieceLeaves(Piece p)
	{
		if(p==getPiece())
		{
			setPiece(pieceTwo);
			pieceTwo = null;
		}
		else{pieceTwo = null;}
	}
	/**gets the second piece*/
	public Piece getPieceTwo(){return pieceTwo;}
	/**gets the owner of the town*/
	public Player getOwner(){return owner;}
	/**method to get the cards name*/
	public String getName(){return "town";}
	/**abstract method to set the second piece*/
	public void setPieceTwo(Piece p){pieceTwo = p;}
}//end class
