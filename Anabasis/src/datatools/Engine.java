package datatools;

import datatools.engines.*;
import gametools.Card;
import gametools.Game;
import gametools.Piece;
import gametools.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**the abstract class Engine collects all the common properties of game engines*/
abstract public class Engine extends User implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6575066129551526774L;
	/**defines the engines properies*/
	public Engine(String name, int rating){super(name, rating);}

    /**this method evaluates a movelist*/
	public Movelist evaluate(Movelist m){
        for(int i=0;i<m.size();i++){
            Move move = m.getMove(i);
            move = evaluate(move);
        }
        return m;
    }
    /**abstract method to evaluate*/
	public abstract Move evaluate(Move m);
	/**reads the users password which it does not have*/
	public String getPass(){return "";}
	/**checks the password which is always right*/
	public boolean checkPass(String p){return true;}
    /**see if it is a human player*/
    public boolean isHuman(){return false;}
    /**method to construct an engine object given the name of the engine*/
    public static Engine construct(int id, String name, int rating){
        Engine e = null;
    	if(name.compareTo("Kroisos")==0){
            e = new Kroisos(rating);
        }
        if(name.compareTo("Xenophon")==0){
            e = new Xenophon(rating);
        }
        if(name.compareTo("Alkibiades")==0){
            e = new Alkibiades(rating);
        }
        if(name.compareTo("Leonidas")==0){
            e = new Leonidas(rating);
        }
        if(name.compareTo("Eumenes")==0){
            e = new Eumenes(rating);
        }
        if(name.compareTo("Timoleon")==0){
            e = new Timoleon(rating);
        }
        if(name.compareTo("Lysandros")==0){
            e = new Lysandros(rating);
        }
        if(name.compareTo("Miltiades")==0){
            e = new Miltiades(rating);
        }
        if(name.compareTo("Nikias")==0){
            e = new Nikias(rating);
        }
        if(name.compareTo("Agis")==0){
            e = new Agis(rating);
        }
        if(name.compareTo("Agesilaos")==0){
            e = new Agesilaos(rating);
        }
        if(name.compareTo("Demetrios")==0){
            e = new Demetrios(rating);
        }
        if(name.compareTo("Kimon")==0){
            e = new Kimon(rating);
        }
        if(name.compareTo("Perikles")==0){
            e = new Perikles(rating);
        }
        if(name.compareTo("Epaminondas")==0){
            e = new Epaminondas(rating);
        }
        e.setId(id);
        return e;
    }
    //methods used by the engines to play the game
    /**method to test if a piece is heading for a barbarian card*/
    public static boolean toBarbarian(Move m){
        if((m.getTo().getName().charAt(0)=='b')&&(m.getTo().isTurned())){return true;}else{return false;}
    }
    /**method to test if the Archon is going to be moved befor all barbarian squares are discovered*/
    public static boolean mightBeBarbarian(Move m){
        //when te card is not turned and the Archon to be moved one has to count the uncovered barbarians
        if((m.getPiece().isArchon())&&(!m.getTo().isTurned())){
            Game game = m.getPiece().getOwner().getGame();
            int barbarians = 0;
            for(int y=0;y<8;y++){
                for(int x=0;x<8;x++){
                    if((game.getCard(x,y).getName().charAt(0)=='b')&&(game.getCard(x,y).isTurned())){barbarians++;}
                }
            }
            if(barbarians<3){return true;}
        }
        return false;
    }
    /**method to test if a piece is heading for a mountain card*/
    public static boolean avoidMountains(Move m){
        if((m.getTo().getName().charAt(0)=='m')&&(m.getTo().isTurned())){return true;}else{return false;}
    }
    /**method to test if a piece is heading for a forrest square and if it can hide there
     * this is exactly then the case, when it is a hoplite piece and none of it's own hoplites is already there*/
    public static boolean toForrest(Move m){
        if((m.getTo().getName().charAt(0)=='f')&&(m.getTo().isTurned())&&(!m.getPiece().isArchon())){
            Piece one = m.getTo().getPiece();
            Piece two = m.getTo().getPieceTwo();
            if(one!=null){
                if(one.getOwner()!=m.getPiece().getOwner()){
                    if(two==null){return true;}//first piece foreign and no second
                    if(two.getOwner()==m.getPiece().getOwner()){
                        return false;//first piece foreign, seceond own
                    }else{
                        return true;//both foreign
                    }
                }else{return false;}//first piece own
            }else{return true;}//no one there
        }else{return false;}//no forrest or covered
    }
    /**by this move the Archon conqueres a town*/
    public static boolean toTown(Move m){
        if((m.getTo().getName().charAt(0)=='t')&&(m.getTo().isTurned())&&(m.getPiece().isArchon())){
            if(m.getTo().getOwner()!=m.getPiece().getOwner()){
                return true;
            } else {return false;}//town already owned
        } else {return false;}//no town or covered or no Archon
    }
    /**by this move an opponent Archon can be taken*/
    public static boolean toArchon(Move m){
        Piece one = m.getTo().getPiece();
        Piece two = m.getTo().getPieceTwo();
        if(one!=null){
           if(one.isArchon()&&(one.getOwner()!=m.getPiece().getOwner())){return true;}
        }
        if(two!=null){
           if(two.isArchon()&&(two.getOwner()!=m.getPiece().getOwner())){return true;}
        }
        return false;
    }
    /**by this move an enemy piece can be taken*/
    public static boolean toEnemy(Move m){
        if(m.getTo().getPiece()!=null){
            if(m.getTo().getPiece().getOwner()!=m.getPiece().getOwner()){return true;}
            if(m.getTo().getPieceTwo()!=null){
                if(m.getTo().getPieceTwo().getOwner()!=m.getPiece().getOwner()){return true;}
            }
        }
        return false;
    }
    /**my Archon is under fire and this move has to be avoided(-1) or forced (1) to avoid a loss, if neither is the case it returns 0*/
    public static int protectArchon(Move m){
        if((m.getPiece().getOwner().getPiece(0).underFire())&&(m.getPiece().getOwner().getMoves()<50)){//turns the routine off in case of repetion
            //store all necessary information about the move in an object-independent form
            int x_from = m.getFrom().getX();
            int y_from = m.getFrom().getY();
            int p = 1; if(m.getPiece()==m.getFrom().getPieceTwo()){p = 2;}
            int x_to = m.getTo().getX();
            int y_to = m.getTo().getY();
            //get a testgame and make a testmove
            Game testgame = new Game(m.getPiece().getOwner().getGame());
            //get information from the games context
            Card start = testgame.getCard(x_from, y_from);
            Piece dummy = start.getPiece();
            if(p==2){dummy = start.getPieceTwo();}
            //make the move and check the result
            dummy.move(x_to,y_to);
            if(testgame.inGame(dummy.getOwner())){
                if(dummy.getOwner().getPiece(0).underFire()){
                    return -1;
                }else{
                    return 1;
                }
            }else{return -1;}
        } else {
            return 0;
        }
    }
    /**avoids to expose the Archon*/
    public static boolean exposingArchon(Move m){
        if(m.getPiece().isArchon()){
            List<Card> v = m.getTo().getNeighbors();
            for(Card c : v){
                if(c.getPiece()!=null){
                    if(c.getPiece().getOwner()!=m.getPiece().getOwner()&&!c.getPiece().blocked()){return true;}
                }
                if(c.getPieceTwo()!=null){
                    if(c.getPieceTwo().getOwner()!=m.getPiece().getOwner()){return true;}
                }
            }
        }
        return false;
    }
    /**method to see if an hoplite can be put next to an opposing hoplite*/
    public static boolean badAttack(Move m){
        if((!m.getPiece().isArchon())&&(!m.getPiece().underFire())){
            Player owner = m.getPiece().getOwner();
            List<Piece> attacks = m.getTo().getCoveringPieces(owner, false);
            List<Piece> covers = m.getTo().getCoveringPieces(owner, true);
            if(attacks.size()<covers.size()){
                for(Piece p : attacks){
                    Player attacker = p.getOwner();
                    if(attacker.armySize()>owner.armySize()){return true;}
                }
            } else {return true;}
        }
        return false;
    }
    /**method to see if there is a town to be conquered over water*/
    public static boolean toTownOverWater(Move m){
        if(m.getPiece().isArchon()){
            Vector<Vector<Card>> ways = m.getFrom().getWays();
            int count=0;
            boolean found = false;
            while((ways.size()>count)&&!found){
                Vector<Card> way = ways.elementAt(count);
                Card c = (Card)way.elementAt(way.size()-1);
                //check if it is a town, visible, not yet conquered and not under fire
                if((c.getName().charAt(0)=='t')&&(c.isTurned())&&(c.getOwner()!=m.getPiece().getOwner())&&(c.getCoveringPieces(m.getPiece().getOwner(),false).size()<1)){
                    if(c.getPiece()!=null){
                        if(c.getPiece().getOwner()!=m.getPiece().getOwner()){found=true;}
                    } else {found=true;}
                }
                if(found){//see if the card you are going to is a step on this way
                    if(m.getTo()!=(Card)way.elementAt(0)){found=false;} else {m.setCom("Demetrios");}
                }
                count++;
            }
            return found;
        }
        return false;
    }
    /**method to test if by this move the piece is on the shortest way to kill an enemy Archon*/
    public static boolean toArchonOverWater(Move m){
        Vector<Vector<Card>> ways = m.getFrom().getWays();
        int count=0;
        boolean found = false;
        while((ways.size()>count)&&!found){
            Vector<Card> way = ways.elementAt(count);
            Card c = (Card)way.elementAt(way.size()-1);
            if(c.getPiece()!=null){
                if((c.getPiece().getOwner()!=m.getPiece().getOwner())&&(c.getPiece().isArchon())){
                    found = true;
                }
            }
            if(c.getPieceTwo()!=null){
                if((c.getPieceTwo().getOwner()!=m.getPiece().getOwner())&&(c.getPieceTwo().isArchon())){
                    found = true;
                }
            }
            if(found){//see if the card you are going to is a step on this way
                if(m.getTo()!=(Card)way.elementAt(0)){found=false;}
            }
            count++;
        }
        return found;
    }

}