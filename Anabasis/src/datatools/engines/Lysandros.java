package datatools.engines;

import datatools.Engine;
import datatools.Move;
import java.io.Serializable;

//Category A, Type VI
/**game engine that avoid barbarian cards when they are turned*/
public class Lysandros extends Engine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -41754612055992770L;
	/**constructor tells this engine for whom it plays*/
	public Lysandros(int rating){super("Lysandros", rating);}

	/**overwrites unuseful method*/
	public void setPass(String p){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
        return getId()+";Lysandros;"+Double.valueOf(getRating()).toString();
    }
    /**this method evaluates a single move*/
    public Move evaluate(Move move){
        if(Engine.toBarbarian(move)){move.setProb(1);}
        if(Engine.mightBeBarbarian(move)){move.setProb(move.getProb()/10);}
        if(Engine.avoidMountains(move)){
            double p = move.getProb()/2;
            move.setProb((int)p);
        }
        if(Engine.toForrest(move)){
            double p = move.getProb()*1.5;
            move.setProb((int)p);
        }
        if(Engine.toTown(move)){move.setProb(move.getProb()*10);}
        if(Engine.toArchon(move)){move.setProb(move.getProb()*100);}
        return move;
    }
}
