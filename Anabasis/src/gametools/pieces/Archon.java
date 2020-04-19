package gametools.pieces;

import gametools.*;
import java.io.Serializable;

/**archons are leaders of holpites and essential pieces*/
public class Archon extends Piece implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3581075756947072926L;
	/**constructs an archon based on the player who owns him
	  *it also needs the players type and the cards it's placed on*/
	public Archon(Player p, String type, Card c)
	{
		super(p, c);
	}
	/**method to get the pieces name*/
	public String getName(){return "archon";}
    /**method to see if it's an archon*/
    public boolean isArchon(){return true;}
}