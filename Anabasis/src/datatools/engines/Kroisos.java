package datatools.engines;

import datatools.Engine;
import datatools.Move;
import java.io.Serializable;

//Category A, Type I
/**the most simple engine that just makes any random move if it is allowed*/
public class Kroisos extends Engine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7814179705955546269L;
	/**constructor tells this engine for whom it plays*/
	public Kroisos(int rating){super("Kroisos", rating);}

	/**overwrites unuseful method*/
	public void setPass(String p){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
        return getId()+";Kroisos;"+Double.valueOf(getRating()).toString();
    }
    /**this method evaluates a movelist*/
	public Move evaluate(Move m){return m;}
}//end class
