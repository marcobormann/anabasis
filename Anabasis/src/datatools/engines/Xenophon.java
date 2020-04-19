package datatools.engines;

import datatools.Engine;
import datatools.Move;
import java.io.Serializable;

//Category A, Type II
/**game engine that avoid barbarian cards when they are turned*/
public class Xenophon extends Engine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6675990569871401448L;
	/**constructor tells this engine for whom it plays*/
	public Xenophon(int rating){super("Xenophon", rating);}

	/**overwrites unuseful method*/
	public void setPass(String p){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
        return getId()+";Xenophon;"+Double.valueOf(getRating()).toString();
    }
    /**this method evaluates a single move*/
    public Move evaluate(Move move){
        if(Engine.toBarbarian(move)){move.setProb(1);}
        return move;
    }
}
