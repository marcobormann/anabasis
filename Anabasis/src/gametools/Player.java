package gametools;

import gametools.pieces.Hoplite;
import gametools.pieces.Archon;
import datatools.User;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**object to represent a player*/
public class Player implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -963028448536594811L;
	/**a Vector storing all the pieces owned by this player*/
	private List<Piece> army = new Vector<Piece>();
	/**the players color*/
	private Game game;
	/**the players type needed for the creation of new hoplites*/
	private String type;
	/**the user who plays this players; which might be a game engine*/
	private User user;
	/**this player has to move again*/
	private boolean move_again;
	/**this player may pass his move*/
	private boolean can_pass;
	/**this player just commited suicide*/
	private boolean suicide;
	/**this player has just created supplies*/
	private boolean supplies;
    /**the moves this player has made in a row*/
    private int moves;

	/**default constructor for various needs*/
	public Player(){}
	/**gives the players his starting pieces and places them on the map
	  *the constructor demands the players type and calculates from it his starting position*/
	public Player(User u,String t, Game gm){
		suicide = false;
		can_pass = false;
		move_again = false;
		supplies = false;
		user = u;
		u.setPlayer(this);
		type = t;
		game = gm;
		//creation of pieces
		int x1=0,x2=0,x3=0,y1=0,y2=0,y3=0;
		char test = type.charAt(0);
		switch(test){
			case 'M':x1=0;y1=0;x2=1;y2=0;x3=0;y3=1;break;
			case 'A':x1=7;y1=0;x2=6;y2=0;x3=7;y3=1;break;
			case 'T':x1=0;y1=7;x2=1;y2=7;x3=0;y3=6;break;
			case 'S':x1=7;y1=7;x2=6;y2=7;x3=7;y3=6;break;
		}
		Card c = gm.getCard(x1,y1);//card on which the piece gets placed on
		Archon a = new Archon(this, type, c);
		c.setPiece(a);//set the piece on the card
		c = gm.getCard(x2,y2);
		Hoplite h = new Hoplite(this, type, c);
		c.setPiece(h);
		c = gm.getCard(x3,y3);
		h = new Hoplite(this, type, c);
		c.setPiece(h);
	}
    /**constructs the player for the copy of a game*/
    public Player(Player p, Game gm){
		suicide = false;
		can_pass = p.getCanPass();
		move_again = p.getMoveAgain();
		supplies = p.hadSupplies();
        user = p.getUser();
        type = p.getType();
		game = gm;
		//creation of pieces
        int count=0;
        while(count<p.armySize()){
            Piece piece = p.getPiece(count);
            int x = piece.getCard().getX();
            int y = piece.getCard().getY();
            Card c = gm.getCard(x,y);
            if(piece.isArchon()){
                Archon a = new Archon(this, type, c);
                if(c.getPiece()==null){c.setPiece(a);}else{c.setPieceTwo(a);}
            } else {
                Hoplite h = new Hoplite(this, type, c);
                if(c.getPiece()==null){c.setPiece(h);}else{c.setPieceTwo(h);}
            }
            count++;
        }
    }

	/**adds a piece to players Vector*/
	public void addPiece(Piece p){army.add(p);}
	/**removes a piece from the players Vector*/
	public void killPiece(Piece p){
		army.remove(p);
		p.getCard().pieceLeaves(p);
		if(p.isArchon()){game.removePlayer(this);}else{}
	}
	/**test if the army is complete with 7 pieces*/
	public boolean isComplete(){
		boolean is_complete;
		if(army.size()<7){is_complete=false;}else{is_complete=true;}
		return is_complete;
	}
	/**gets the game in which the player takes part*/
	public Game getGame(){return game;}
	/**gets the size of the army*/
	public int armySize(){return army.size();}
	/**get all pieces*/
	public List<Piece> getPieces(){return army;}
	/**get a piece from the army*/
	public Piece getPiece(int i){return army.get(i);}
	/**set the army movable after on piece had to move because of a water square*/
	public void setArmyImmovable(boolean b){
		for(int i=0;i<army.size();i++){
			Piece p = army.get(i);
			p.setAnotherMustMove(b);
		}
	}
	/**get the players type*/
	public String getType(){return type;}
	/**get the players user*/
	public User getUser(){return user;}
	/**looks if the player has to move again in the same turn*/
	public boolean getMoveAgain(){return move_again;}
	/**determines if the player has to move again*/
	public void setMoveAgain(boolean b){move_again = b;}
	/**this method checks if all pieces are blocked*/
	public boolean allBlocked(){
		boolean areblocked = true;
		for(int i=0;i<armySize();i++){
			Piece p = (Piece)getPiece(i);
			if(!(p.anotherMustMove()||p.blocked())){areblocked = false;}else{}
		}
		return areblocked;
	}
	/**deblocks all pieces of the players army*/
	public void deblockAll(){
		for(int i=0;i<armySize();i++){
			Piece p = (Piece)getPiece(i);
			p.deblock();
		}
	}
	/**looks if the player can pass his turn*/
	public boolean getCanPass(){return can_pass;}
	/**determines if the player can pass his turn*/
	public void setCanPass(boolean b){can_pass = b;}
	/**looks if this player just commited suicide*/
	public boolean commitedSuicide(){return suicide;}
	/**sets the player to have commited suicide*/
	public void setSuicide(){suicide = true;}
	/**looks if the player had supplies*/
	public boolean hadSupplies(){return supplies;}
	/**determines if the player had supplies*/
	public void setSupplies(boolean b){supplies = b;}
    /**reads the selected piece*/
    public int getMoves(){return moves;}
    /**reads the selected piece*/
    public void setMoves(int m){moves=m;}
}//end Player
