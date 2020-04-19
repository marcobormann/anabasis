package gametools.cards;

import gametools.*;
import java.io.Serializable;

/**This class admistrates all cards of the type water*/
public class Water extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4455376822383239077L;
	/**constructor using the coordinates of the card*/
	public Water(int x,int y, Game g)
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
	/**moves a piece to this square*/
	public void pieceComes(Piece p)
	{
		Piece piece = getPiece();
		if(!(piece==null)){piece.getOwner().killPiece(piece);}else{}
		setPiece(p);
		p.getOwner().setArmyImmovable(true);
		p.setAnotherMustMove(false);
		p.getOwner().setMoveAgain(true);
		p.getOwner().setSupplies(false);
		p.getOwner().setCanPass(false);
	}
	/**a piece moving away from this card*/
	public void pieceLeaves(Piece p)
	{
		setPiece(null);
	}
	/**there is no piece here and it will return null*/
	public Piece getPieceTwo(){return null;}
	/**returns null becaus this object has no owner*/
	public Player getOwner(){return null;}
	/**method to get the cards name*/
	public String getName(){return "water";}
	/**abstract method to set the second piece*/
	public void setPieceTwo(Piece p){}
}//end class
