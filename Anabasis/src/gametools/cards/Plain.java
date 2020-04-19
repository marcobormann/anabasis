package gametools.cards;

import gametools.*;
import java.io.Serializable;

/**This class admistrates all cards of the type plain*/
public class Plain extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2093769609623967785L;
	/**constructor using the coordinates of the card*/
	public Plain(int x,int y, Game g)
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
	  *simply moves a piece to this square*/
	public void pieceComes(Piece p)
	{
		Piece piece = getPiece();
		if(!(piece==null)){piece.getOwner().killPiece(piece);}else{}
		setPiece(p);
		p.getOwner().setArmyImmovable(false);
		p.getOwner().setSupplies(false);
	}
	/**a piece moving away from this card*/
	public void pieceLeaves(Piece p){setPiece(null);}
	/**gets the first piece*/
	public Piece getPieceTwo(){return null;}
	/**returns null becaus this object has no owner*/
	public Player getOwner(){return null;}
	/**method to get the cards name*/
	public String getName(){return "plain";}
	/**abstract method to set the second piece*/
	public void setPieceTwo(Piece p){}
}//end class
