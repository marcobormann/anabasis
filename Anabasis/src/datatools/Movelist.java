package datatools;

import gametools.Card;
import gametools.Game;
import gametools.Piece;
import gametools.Player;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

/**class to store a list of moves*/
public class Movelist {
    /**the Vector containing the moves*/
    private Vector<Move> moves = new Vector<Move>();

    /**a constructor getting all the moves for a given player in a given Game*/
    public Movelist(Player p){
        Game game = p.getGame();
        for(int i=0;i<p.armySize();i++){
            Piece piece = p.getPiece(i);
            Card from = piece.getCard();
            for(int d=1;d<9;d++){
           		int x = from.getX();
              	int y = from.getY();
                switch(d)
                {
                    case 1: x--;y--;break;//SW
                    case 2:     y--;break;//W
                    case 3: x++;y--;break;//NW
                    case 4: x++;    break;//N
                    case 5: x++;y++;break;//NE
                    case 6:     y++;break;//E
                    case 7: x--;y++;break;//SE
                    case 8: x--;    break;//S
                }
                if((x<8)&&(x>=0)&&(y<8)&&(y>=0)){
                    Card to = game.getCard(x,y);
                    if((!piece.anotherMustMove())&&(!piece.blocked())&&(to.accessPossible(piece))){
                        Move m = new Move(piece, from, to);
                        moves.add(m);
                    }
                    else{}//no move possible
                }
                else{}//direction leaves the squares
            }//end direction
        }//end piece
    }

    /**access to a Move*/
    public Move getMove(int n){return (Move)moves.elementAt(n);}

    /**read the size of the list*/
    public int size(){return moves.size();}

    /**method to pick a move*/
    public Move pickMove(){
        Random rnd = new Random();
        int total = 0;
        for(Enumeration<Move> E = moves.elements(); E.hasMoreElements() ;){
            Move m = (Move)E.nextElement();
            total += m.getProb();
        }
        if(total==0){return null;}
        int prob = rnd.nextInt(total)-1;
        total = 0;
        for(Enumeration<Move> E = moves.elements(); E.hasMoreElements() ;){
            Move m = (Move)E.nextElement();
            total += m.getProb();
            if(prob<=total){return m;}
        }
        return null;
    }
}
