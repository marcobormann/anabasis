package control;

import datatools.Move;
import datatools.Movelist;
import datatools.SuperEngine;
import gametools.Player;
import datatools.Engine;
import gametools.Game;

public class EngineHandler {

	public void makeMove(Game game, Engine engine) {
		try {
			if(engine instanceof SuperEngine){
				((SuperEngine)engine).move();
			} else {
				Player player = game.getPlayer(0);
				Movelist moves = new Movelist(player);
				moves = engine.evaluate(moves);
				Move move = moves.pickMove();
				if (move == null) {// take the first move by default
					move = moves.getMove(0);
				}
				move.getPiece().move(move.getTo().getX(), move.getTo().getY());
			}
		} catch (Exception e) {
			engine.getPlayer().commitedSuicide();
			e.printStackTrace();
		}
	}
}
