package datatools.engines;

import datatools.Engine;
import datatools.Move;
import java.io.Serializable;

//Category A, Type VIII
/**game engine that avoid barbarian cards when they are turned*/
public class Agis extends Engine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7634215901527054462L;
	/**constructor tells this engine for whom it plays*/
	public Agis(int rating){super("Agis", rating);}

	/**overwrites unuseful method*/
	public void setPass(String p){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
        return getId()+";Agis;"+Double.valueOf(getRating()).toString();
    }
    /**this method evaluates a single move*/
    public Move evaluate(Move move){
        if(Engine.toBarbarian(move)){move.setProb(2);}
        if(Engine.mightBeBarbarian(move)){move.setProb(move.getProb()/10);}
        if(Engine.toTown(move)){move.setProb(move.getProb()*10);}
        if(Engine.toEnemy(move)){move.setProb(move.getProb()*50);}
        if(Engine.toArchon(move)){move.setProb(move.getProb()*1000);}
        int n = Engine.protectArchon(move);
        if(n>0){move.setProb(move.getProb()*200);}
        if(n<0){move.setProb(2);}
        if(Engine.avoidMountains(move)){
            double p = move.getProb()/2;
            move.setProb((int)p);
        }
        if(Engine.toForrest(move)){
            double p = move.getProb()*1.5;
            move.setProb((int)p);
        }
        if(Engine.exposingArchon(move)){move.setProb(1);}
        return move;
    }
}
