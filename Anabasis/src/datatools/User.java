package datatools;

import gametools.Player;
import java.io.Serializable;

/**the abstract class User collects all the common properties of human players and game engines*/
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4250817668441865329L;
	/**the users id-code*/
	private int id;
	/**the users name*/
	private String name;
	/**the users rating*/
	private int rating;
	/**the player this user is playing*/
	private Player player;

	/**defines the engines properies*/
	public User(String n, int r){name = n; rating = r;}
	public User(int id, String n, int r){this.id =id; name = n; rating = r;}
    /**default constructor*/
    public User(){}

	/**reads the users id*/
	public int getId(){return id;}
	/**changes the users id*/
	public void setId(int i){id = i;}
	/**reads the users rating*/
	public int getRating(){return rating;}
	/**changes the rating*/
	public void setRating(int r){rating = r;}
	/**defines the player*/
	public void setPlayer(Player p){player = p;}
	/**reads the users name*/
	public String getName(){return name;}
	/**changes the  name*/
	public void setName(String n){name = n;}
	/**reads the users name*/
	public String getPass(){return "";}
	/**abstrcat method to change the password*/
	public void setPass(String p){}
	/**abstract method to check the password*/
	public boolean checkPass(String p){return false;}
	/**gets the player played by this user*/
	public Player getPlayer(){return player;}
    /**abstract method to determine if it is a human user*/
	public boolean isHuman(){return false;}
}