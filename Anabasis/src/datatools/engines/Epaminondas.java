package datatools.engines;

import gametools.Card;
import gametools.Game;
import gametools.Piece;
import gametools.Player;
import gametools.cards.Grain;
import gametools.cards.Mountain;
import gametools.cards.Water;

import java.util.List;

import datatools.Way;

public class Epaminondas extends Perikles {

	private static final long serialVersionUID = 1L;
	private Player enemy;

	public Epaminondas(int rating) {
		super("Epaminondas", rating);
	}

	@Override
	protected void evaluateWays(List<Way> ways) {
		super.evaluateWays(ways);
		Player currentPlayer = ways.get(0).getPiece().getOwner();
		if (currentPlayer.armySize() > 1) {
			setEnemy(currentPlayer.getGame());
			evaluateApproach(ways);
		}
	}

	private void evaluateApproach(List<Way> ways) {
		for (Way way : ways) {
			if(!way.getPiece().isArchon()){
				Card target = way.getTarget();
				Card enemySquare = enemy.getPiece(0).getCard();
				if (targetIsCloser(target, way.getPiece().getCard(), enemySquare)
						&& !(target instanceof Mountain)) {
					if (hasCover(way.getPiece(), target)) {
						way.setValue(way.getValue() * 5);
						if (isOneStepAway(target, enemySquare)) {
							way.setValue(way.getValue() * 20);
						}
					}
				}
			}
		}
	}

	private boolean targetIsCloser(Card target, Card card, Card enemySquare) {
		int distanceTarget = getMaxDistance(target, enemySquare);
		int distanceCard = getMaxDistance(card, enemySquare);
		return distanceTarget < distanceCard;
	}

	private int getMaxDistance(Card card, Card enemySquare) {
		int distanceX = Math.abs(card.getX() - enemySquare.getX());
		int distanceY = Math.abs(card.getY() - enemySquare.getY());
		if (distanceX > distanceY) {
			return distanceX;
		} else {
			return distanceY;
		}
	}

	private boolean isOneStepAway(Card target, Card enemySquare) {
		List<Card> neighbors = getCompleteNeighbors(target);
		for (Card neighbor : neighbors) {
			if (neighbor == enemySquare) {
				return true;
			}
		}
		return false;
	}

	private boolean hasCover(Piece piece, Card card) {
		List<Card> neighbors = getCompleteNeighbors(card);
		return isCovered(neighbors, piece);
	}

	private List<Card> getCompleteNeighbors(Card card) {
		List<Card> neighbors = card.getNeighbors();
		int index = 0;
		while (index < neighbors.size()) {
			Card neighbor = neighbors.get(index);
			addFarNeighbors(neighbor, neighbors);
			index++;
		}
		index = 0;
		while (index < neighbors.size()) {
			Card neighbor = neighbors.get(index);
			if (neighbor instanceof Water && neighbor.isTurned()) {
				neighbors.remove(index);
				index--;
			}
			index++;
		}
		return neighbors;
	}

	private void addFarNeighbors(Card neighbor, List<Card> neighbors) {
		if ((neighbor instanceof Water || neighbor instanceof Grain) && neighbor.isTurned()) {
			List<Card> nextNeighbors = neighbor.getNeighbors();
			for (Card nextNeighbor : nextNeighbors) {
				if (!neighbors.contains(nextNeighbor)) {
					neighbors.add(nextNeighbor);
				}
			}
		}
	}

	private boolean isCovered(List<Card> cards, Piece piece) {
		int attackers = 0;
		int defenders = 0;
		Player defender = piece.getOwner();
		for (Card card : cards) {
			Player ownerOne = null;
			Piece pieceOne = card.getPiece();
			if (pieceOne != null) {
				ownerOne = pieceOne.getOwner();
			}
			Player ownerTwo = null;
			Piece pieceTwo = card.getPieceTwo();
			if (pieceTwo != null) {
				ownerTwo = pieceTwo.getOwner();
			}
			if (ownerOne != null && ownerOne != defender) {
				attackers++;
			}
			if (ownerTwo != null && ownerTwo != defender) {
				attackers++;
			}
			if (ownerOne == defender && piece != pieceOne) {
				defenders++;
			}
			if (ownerTwo == defender && piece != pieceTwo) {
				defenders++;
			}
		}
		return defenders >= attackers;
	}

	private void setEnemy(Game game) {
		if (enemy == null) {
			setNewEnemy(game);
		} else {
			if (!game.inGame(enemy)) {
				enemy = null;
				setNewEnemy(game);
			}
		}
	}

	private void setNewEnemy(Game game) {
		for (int i = 1; i < game.getPlayers().size(); i++) {
			Player player = game.getPlayer(i);
			if (enemy != null) {
				if (enemy.getUser().getRating() < player.getUser().getRating()) {
					enemy = player;
				}
			} else {
				enemy = player;
			}
		}
	}

}
