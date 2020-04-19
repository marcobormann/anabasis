package gametools.pieces;

import gametools.*;
import java.io.Serializable;

/**hoplits are simple pieces*/
public class Hoplite extends Piece implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7821091599610867517L;
	/**constructs an hoplite based on the player who owns him, his type and the card is is placed on*/
	public Hoplite(Player p, String type, Card c)
	{
		super(p, c);
	}
	/**method to get the pieces name*/
	public String getName(){return "hoplite";}
    /**method to see if it's an archon*/
    public boolean isArchon(){return false;}
}