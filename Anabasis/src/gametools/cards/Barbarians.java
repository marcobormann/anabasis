package gametools.cards;

import gametools.*;
import java.io.Serializable;

/**This class admistrates all cards of the type barbarians*/
public class Barbarians extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5190102389409508698L;
	/**constructor using the coordinates of the card*/
	public Barbarians(int x,int y, Game g)
	{
		super(x,y,g);
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
	  *destoys the piece by cutting it from the owners army Vector*/
	public void pieceComes(Piece p)
	{
		Piece piece = getPiece();
		if(!(piece==null)){piece.getOwner().killPiece(piece);}else{}
		setPiece(null);
		if(p.isArchon()){p.getOwner().setSuicide();}else{}
		p.getOwner().killPiece(p);
		p.getOwner().setArmyImmovable(false);
		p.getOwner().setSupplies(false);
	}
	/**a piece moving away from this card*/
	public void pieceLeaves(Piece p){setPiece(null);}
	/**there is no second piece here and it will return null*/
	public Piece getPieceTwo(){return null;}
	/**returns null becaus this object has no owner*/
	public Player getOwner(){return null;}
	/**method to get the cards name*/
	public String getName(){return "barbarians";}
 	/**abstract method to set the second piece*/
	public void setPieceTwo(Piece p){}
}//end class
