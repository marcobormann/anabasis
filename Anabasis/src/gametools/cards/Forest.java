package gametools.cards;

import gametools.*;
import java.io.Serializable;
import java.util.Random;

/**This class admistrates all cards of the type forest*/
public class Forest extends Card implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**another piece that also can be on this card*/
	private Piece pieceTwo;
	/**creates the a random generator to make a random choice if two pieces are attacked*/
	private Random rnd = new Random();

	/**constructor using the coordinates of the card
     * sets the second piece to null*/
	public Forest(int x,int y, Game g)
	{
		super(x,y,g);
		pieceTwo = null;
	}
	/**tests if a piece can move to this square*/
	public boolean accessPossible(Piece p)
	{
		boolean possible;
		Piece piece = getPiece();
		if(!(piece==null))
		{//there is a piece here
			if(!(pieceTwo==null))
			{//there are two pieces here
				if((p.getOwner()==piece.getOwner())&&(p.getOwner()==pieceTwo.getOwner())){possible = false;}
				//both are owned by the owner of the incoming piece
				else
				{
					possible = true;
				}
			}
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
	  *tests if there is already one piece and then adds the second*/
	public void pieceComes(Piece p)
	{
		boolean archon = false;
		if(p.isArchon()){archon = true;}else{}
		Piece piece = getPiece();
		if(piece==null){setPiece(p);}//there is no piece here
		else
		{//there is a piece here
			if(pieceTwo==null)
			{//there is one piece here
				if(p.getOwner()==piece.getOwner()){pieceTwo = p;}//the piece is an own one
				else
				{
					if((piece.isArchon())||archon)
					{//there is an archon or the card or an archon coming
						piece.getOwner().killPiece(piece);
						setPiece(p);
					}
					else
					{//take the second spot
						pieceTwo = p;
					}
				}
			}
			else
			{//there are two pieces here
				if(p.getOwner()==piece.getOwner())
				{//the first piece is an own one, so the second cannot be, so it must be foreign and hoplite, possibilty check done before
					pieceTwo.getOwner().killPiece(pieceTwo);
					pieceTwo = p;
				}
				else
				{//first piece foreign one
					if(p.getOwner()==pieceTwo.getOwner())
					{//second piece is an own one so the first must be foreign and cannot be an archon
						piece.getOwner().killPiece(piece);
						pieceTwo = p;//because killing the first moves the second to the first spot
					}
					else
					{//both pieces foreign one
						if(piece.isArchon())
						{//the first piece is an archon, so the second will be removed first
							pieceTwo.getOwner().killPiece(pieceTwo);
							piece.getOwner().killPiece(piece);
							setPiece(p);
						}
						else
						{//the first is no archon
							if((pieceTwo.isArchon())||archon)
							{//the second is an archon or an archon is coming
								piece.getOwner().killPiece(piece);//will move the second piece to the first spot
								getPiece().getOwner().killPiece(getPiece());
								setPiece(p);
							}
							else
							{//both are hoplites and there is no archon coming, so only one is killed and replaced
							int target = rnd.nextInt(2);
								if(target == 1)
								{
									piece.getOwner().killPiece(piece);//will move the second piece to the first spot
									pieceTwo = p;
								}
								else
								{
									pieceTwo.getOwner().killPiece(pieceTwo);
									pieceTwo = p;
								}
							}
						}
					}
				}
			}
		}
		//restore moving conditions
		p.getOwner().setArmyImmovable(false);
		p.getOwner().setSupplies(false);
	}//end piece comes
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
	/**returns null because this object has no owner*/
	public Player getOwner(){return null;}
	/**method to get the cards name*/
	public String getName(){return "forest";}
	/**abstract method to set the second piece*/
	public void setPieceTwo(Piece p){pieceTwo = p;}
}//end class

