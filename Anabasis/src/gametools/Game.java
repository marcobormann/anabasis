package gametools;

import gametools.cards.Water;
import gametools.cards.Town;
import gametools.cards.Mountain;
import gametools.cards.Plain;
import gametools.cards.Grain;
import gametools.cards.Forest;
import gametools.cards.Barbarians;
import datatools.User;
import datatools.Match;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**creates a map of 8x8 squares distributing a given number of cards and is able to paint the game*/
public class Game implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3765429890289755004L;
	/**a map of 8x8 squares which contains cards as objects and adds four players*/
	private Card[][] squares = new Card[8][8];
	/**a Vector with the list of players*/
	private List<Player> players = new Vector<Player>();
    /**the time left for a move*/
    private int time = 60;
    /**has the user got a notification, that his player has to make a move?
     * used for engines since the game control wants to tell them only once that they have to make a move*/
    private boolean notified = false;
    /**the match which this game is playing*/
    private Match match;
    /**count the moves to identify errors *
     * the count may also be used to declare a game a draw after a certain number of moves*/
    private int moves_made = 0;

    public Game(){}
	/**constructs the objects in the map and the players*/
	public Game(Match m){
                Random rnd = new Random();
                match = m;
                User[] users = new User[4];
                boolean[] taken = {false, false, false, false};
                for(int x=0;x<4;x++){
                   while(users[x]==null){
                       int i = rnd.nextInt(4);
                       if(!taken[i]){
                           users[x] = m.getUser(i);
                           taken[i]=true;
                       }
                   }
                }
		String[] values = {"forest", "grain", "water", "town", "mountain", "barbarians", "plain"};
		int[] cards = {6, 10, 16, 6, 7, 3, 16};
		int nb_cards = 0, rd, position, typecount;
		boolean notfound;
		while (nb_cards < 64){
			notfound = true;
			rd = rnd.nextInt(64-nb_cards)+1;
			position = 0;
			typecount = 0;
			while (notfound){
				position = position + cards[typecount];//go to the next cardset
				if (rd <= position){//random number points on cardset
					int x = nb_cards-((int)(nb_cards/8)*8);
					int y = (int)(nb_cards/8);
					char test = values[typecount].charAt(0);
					switch(test){
						case 'f': squares[x][y] = new Forest(x,y,this);break;
						case 'g': squares[x][y] = new Grain(x,y,this);break;
						case 'w': squares[x][y] = new Water(x,y,this);break;
						case 't': squares[x][y] = new Town(x,y,this);break;
						case 'm': squares[x][y] = new Mountain(x,y,this);break;
						case 'b': squares[x][y] = new Barbarians(x,y,this);break;
						case 'p': squares[x][y] = new Plain(x,y,this);break;
					}
					notfound = false;
					cards[typecount]--;
				}
				else {typecount++;}
			}
			nb_cards++;
		}//cards distributed
		//creation of players
		String[] type = {"Makedonian","Athenian","Spartan","Theban"};
		for(int i=0;i<4;i++){
			Player p = new Player(users[i], type[i], this);
			players.add(p);
		}
	}//end constructor
    /**constructs the game based on a given game
     * this is used to make a copy of a game needed for the engines to test moves
     * the copy will not be exact but work with asumptions for the natured of covered cards */
    public Game(Game g){
        //recreate the map
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                char type = g.getCard(x,y).getName().charAt(0);
                if(!g.getCard(x,y).isTurned()){type='p';}
                switch(type){
					case 'f': squares[x][y] = new Forest(x,y,this);break;
					case 'g': squares[x][y] = new Grain(x,y,this);break;
					case 'w': squares[x][y] = new Water(x,y,this);break;
					case 't': squares[x][y] = new Town(x,y,this);break;
					case 'm': squares[x][y] = new Mountain(x,y,this);break;
					case 'b': squares[x][y] = new Barbarians(x,y,this);break;
					case 'p': squares[x][y] = new Plain(x,y,this);break;
				}
                squares[x][y].turn();
            }
        }
        //create the players
        int count=0;
        while(count<g.numberOfPlayers()){
            Player p = new Player(g.getPlayer(count), this);
            players.add(p);
            count++;
        }
    }

	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	/**gets the object on (x,y) on the map*/
	public Card getCard(int x, int y){return squares[x][y];}
	/**gets a player from the current game*/
	public Player getPlayer(int i){return (Player)players.get(i);}
	/**gets a player knowing his type*/
	public Player getPlayer(String type){
        for(Player p : players){
            if(p.getType().compareTo(type)==0){return p;}
        }
        return null;
    }
	/**gets number of players in the game*/
	public int numberOfPlayers(){return players.size();}
	/**removes a player from the game*/
	public void removePlayer(Player p){
		if(players.size()>1)
		{
			int size = p.armySize();
			for(int i=0;i<size;i++)
			{
				Piece tokill = (Piece)p.getPiece(0);
				p.killPiece(tokill);
			}
			players.remove(p);
			//in case of a suicide the next player will be on top of the list and to be made to move again, otherwise the next method will skip him
			if(p.commitedSuicide())
			{
				getPlayer(0).setMoveAgain(true);
				getPlayer(0).deblockAll();
			}else{}
		}else{}//the player to be removed is the only one left, thus not removed and winner
	}
	/**determines the next player to move*/
	public Player next(){
		Player p = players.get(0);
		if((!p.getMoveAgain())||p.allBlocked()){//get next player
			p.setArmyImmovable(false);//disable all inner turn blockades of pieces
			p.setCanPass(false);
			boolean nextfound = false;
			while(!nextfound){
				players.remove(p);
				players.add(p);
				p.deblockAll();
				p = (Player)players.get(0);//get the new player
				if(!p.allBlocked()){nextfound=true;}else{p.deblockAll();}
			}
		} else {
			if(p.allBlocked()){p.setArmyImmovable(false);}//disable inner turn blockades only if all pieces are blocked
            p.setMoves(p.getMoves()+1);
		}
		if(p.hadSupplies()){
			p.setArmyImmovable(true);
			Piece a = p.getPiece(0);
			a.setAnotherMustMove(false);
			Piece b = a.getCard().getPieceTwo();
			b.setAnotherMustMove(false);
			p.setSupplies(false);
		}
        //a move has been made and the new players in game parameters have to be adjusted
		p.setMoveAgain(false);
        moves_made++;
        p.setMoves(0);
        time = 60;
        notified = false;
		return p;
    }
    /**sets the time to move*/
    public void setTime(int t){time = t;}
    /**reads the selected piece*/
    public int getTime(){return time;}
    /**reads the match a game is playing*/
    public Match getMatch(){return match;}
    /**sets the notified parameter*/
    public void setNotified(boolean b){notified = b;}
    /**reads the notified parameter*/
    public boolean getNotified(){return notified;}
    /**reads the selected piece*/
    public int getMoves(){return moves_made;}
    /**method to allow the cloning of a game*/
    public Object clone(){
        try {
            return super.clone();
        }catch(CloneNotSupportedException e){}
        return null;
    }
    /**checks if a player is still in the game*/
    public boolean inGame(Player player){
        for(Player p : players){
            if(p.getType().compareTo(player.getType())==0){return true;}
        }
        return false;
    }
}//end game