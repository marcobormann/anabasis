package control;

import datatools.Engine;
import gametools.Piece;
import gametools.Player;
import gametools.Game;

public class Playing extends Thread {

	private volatile Game game;
	private volatile boolean hasMoved;

	public Playing(Game game) {
		this.setGame(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void run() {
		while (game.getMatch().isRunning()) {
			Player player = game.getPlayer(0);
			if (!player.getUser().isHuman()) {
				Engine engine = (Engine) player.getUser();
				new EngineHandler().makeMove(game, engine);
				if (game.numberOfPlayers() < 2) {
					game.getMatch().end(game.getPlayer(0).getUser().getId());
					System.out.println("news: "+game.getMatch().toString());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.next();
			} else {
				hasMoved = false;
				int time = 600;
				while (time > 0 && hasMoved == false) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					time--;
					game.setTime(time);
				}
				if (time == 0) {
					player.setSuicide();
					Piece tokill = (Piece) player.getPiece(0);
					player.killPiece(tokill);
					game.next();
				}
			}
		}
	}

	public void move() {
		hasMoved = true;
		if (game.numberOfPlayers() < 2 && !game.getMatch().hasEnded()) {
			game.getMatch().end(game.getPlayer(0).getUser().getId());
			System.out.println("news: "+game.getMatch().toString());
		}
	}

}
