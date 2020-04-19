
package datatools;

import java.io.Serializable;

/**
 *
 * @author: Marco Bormann
 */
/**the class human collects all the common properties of a human player*/
public class Human extends User implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5959206254408650704L;
	/**the users name*/
	private String password;

	/**constructs the human player as a guest*/
    public Human(){}
    /**constructs the human player with his properties*/
	public Human(String name, int rating){super(name, rating);}
	public Human(int id, String name, int rating){super(id, name, rating);}
	/**reads the users password*/
	public String getPass(){return password;}
	/**changes the players password*/
	public void setPass(String p){password = p;}
	/**checks the password*/
	public boolean checkPass(String p){if (password.compareTo(p) == 0){return true;}else{return false;}}
	/**method to move*/
	public void move(){}
    /**method to transform the objects date to CSV format*/
    public String toCSV()
    {
         return getId()+";"+getName()+";"+Double.valueOf(getRating()).toString()+";"+getPass();
    }
    /**see if it is a human player*/
    public boolean isHuman(){return true;}
}