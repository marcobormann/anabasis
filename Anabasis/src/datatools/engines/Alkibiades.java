package datatools.engines;

import datatools.Engine;
import datatools.Move;
import java.io.Serializable;

//Category A, Type III
/**game engine that avoid barbarian cards when they are turned*/
public class Alkibiades extends Engine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8613426349761205700L;
	/**constructor tells this engine for whom it plays*/
	public Alkibiades(int rating){super("Alkibiades", rating);}

	/**overwrites unuseful method*/
	public void setPass(String p){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
        return getId()+";Alkibiades;"+Double.valueOf(getRating()).toString();
    }
    /**this method evaluates a single move*/
    public Move evaluate(Move move){
        if(Engine.toBarbarian(move)){move.setProb(1);}
        if(Engine.mightBeBarbarian(move)){move.setProb(move.getProb()/10);}
        return move;
    }
}
