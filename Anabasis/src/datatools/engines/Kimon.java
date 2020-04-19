package datatools.engines;

import datatools.Engine;
import datatools.Move;
import java.io.Serializable;

//Category B, Type I
/**game engine that avoid barbarian cards when they are turned*/
public class Kimon extends Engine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4199943730514133187L;
	/**constructor tells this engine for whom it plays*/
	public Kimon(int rating){super("Kimon", rating);}

	/**overwrites unuseful method*/
	public void setPass(String p){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
        return getId()+";Kimon;"+Double.valueOf(getRating()).toString();
    }
    /**this method evaluates a single move*/
    public Move evaluate(Move move){
        if(Engine.toBarbarian(move)){move.setProb(1);}
        if(Engine.mightBeBarbarian(move)){move.setProb(move.getProb()/10);}
        if(Engine.toTownOverWater(move)){move.setProb(move.getProb()*10);}
        if(Engine.badAttack(move)){move.setProb(2);}
        if(Engine.toEnemy(move)){move.setProb(move.getProb()*50);}
        int n = Engine.protectArchon(move);
        if(n>0){move.setProb(move.getProb()*200);}
        if(n<0){move.setProb(2);}
        if(Engine.exposingArchon(move)){move.setProb(2);}
        if(Engine.toArchonOverWater(move)){move.setProb(move.getProb()*1000);}
        if(Engine.avoidMountains(move)){
            double p = move.getProb()/2;
            move.setProb((int)p);
        }
        if(Engine.toForrest(move)){
            double p = move.getProb()*1.5;
            move.setProb((int)p);
        }
        return move;
    }
}
