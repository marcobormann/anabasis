package datatools.engines;

import gametools.Card;
import gametools.Game;
import gametools.Piece;
import gametools.cards.*;
import gametools.pieces.*;

import java.util.List;
import java.util.Vector;

import datatools.SuperEngine;
import datatools.Way;

public class Perikles extends SuperEngine {

	private static final long serialVersionUID = 1L;
	private Way selectedWay;
	private Way propulsionWay;
	
	public Perikles(int rating){super("Perikles", rating);}
	
	public Perikles(String name, int rating){super(name, rating);}

	public void move(){
		if(selectedWay==null||!accessible(selectedWay.getTarget(), selectedWay.getPiece())){
			generateSelectedWay();
		}
		if(selectedWay.getPiece().anotherMustMove()){
			if(propulsionWay==null){
				generatePropulsionWay(selectedWay);
			} else {
				if(propulsionWay.getWay().size()==0){
					generatePropulsionWay(selectedWay);
				}
			}
			if(executeMove(propulsionWay)){propulsionWay=null;}
		} else {
			propulsionWay=null;
			if(executeMove(selectedWay)){selectedWay=null;}
		}
	}

	private boolean executeMove(Way way) {
		Card target = way.getWay().get(0);
		Piece piece = way.getPiece();
		piece.move(target.getX(), target.getY());
		way.getWay().remove(0);
		if(way.getWay().size()==0){return true;}
		return false;
	}
	
	private void generatePropulsionWay(Way way) {
		List<Way> ways = getPropulsionWays(way);
		if(ways.size()>0){
			propulsionWay = ways.get(0);
		}
	}

	private List<Way> getPropulsionWays(Way way) {
		List<Way> ways = new Vector<Way>();
		List<Piece> otherPieces = getOtherActivePieces(way.getPiece());
		for(Piece p : otherPieces){
			if(!p.blocked()&&!p.anotherMustMove()){
				ways = getAllWaysForPiece(p.getCard(),ways,p,false);
			}
		}
		keepOnlyGrainTargets(ways);
		removeCrossedGrain(way, ways);
		return ways;
	}

	private void removeCrossedGrain(Way way, List<Way> ways) {
		int index = 0;
		while(index<ways.size()){
			Card card = ways.get(index).getTarget();
			for (Card c : way.getWay()) {
				if (c instanceof Grain && card == c) {
					ways.remove(index);
					index--;
				}
			}
			index++;
		}
	}

	private void keepOnlyGrainTargets(List<Way> ways) {
		int index = 0;
		while(index<ways.size()){
			Way way = ways.get(index);
			Card card = way.getTarget();
			if(card instanceof Grain && card.isTurned() && getGrainNumber(way)==0){
				index++;
			} else {
				ways.remove(index);
			}
		}
	}

	private void generateSelectedWay() {
		List<Way> ways = getAllWays();
		evaluateWays(ways);
		evaluateNecessities(ways);
		selectedWay = selectWay(ways);
	}

	private List<Way> getAllWays() {
		List<Way> ways = new Vector<Way>();
		for(Piece piece : getPlayer().getPieces()){
			if(!piece.blocked()&&!piece.anotherMustMove()){
				ways = getAllWaysForPiece(piece.getCard(),ways,piece,true);
			}
		}
		return ways;
	}

	private int getGrainNumber(Way way) {
		int count = 0;
		int index = 0;
		while(index<way.getWay().size()-1){
			Card card = way.getWay().get(index);
			if(card instanceof Grain && card.isTurned() && card!=way.getTarget()){count++;}
			index++;
		}
		return count;
	}

	private int getPropulsion(Way way) {
		List<Way> ways = getPropulsionWays(way);
		for(Way w : ways){
			Card card = w.getTarget();
			if(hasWaterNeighbor(card)){
				return 100;
			}
		}
		return ways.size();
	}

	private boolean hasWaterNeighbor(Card card) {
		for(Card c : card.getNeighbors()){
			if(c.isTurned() && c instanceof Water){return true;}
		}
		return false;
	}

	private List<Piece> getOtherActivePieces(Piece piece) {
		List<Piece> otherPieces = new Vector<Piece>();
		for(Piece p : getPlayer().getPieces()){
			if(p!=piece&&!p.blocked()&&!p.anotherMustMove()){
				otherPieces.add(p);
			}
		}
		return otherPieces;
	}

	private List<Way> getAllWaysForPiece(Card card, List<Way> ways, Piece piece, boolean checkPropulsion) {
		List<Card> neighbors = card.getNeighbors();
		for(Card neighborCard : neighbors){
			List<Card> newWay = new Vector<Card>();
			newWay.add(neighborCard);
			ways = getAllWaysForNeighbor(newWay, ways, piece, checkPropulsion);
		}
		return ways;
	}

	private List<Way> getAllWaysForNeighbor(List<Card> newWay, List<Way> ways, Piece piece, boolean checkPropulsion) {
		Card card = newWay.get(newWay.size()-1);
		if(passedCard(card, newWay)){return ways;}
		if(card instanceof Water && card.isTurned()){
			ways = branch(newWay, ways, piece, checkPropulsion);
		} else {
			if(!accessible(card,piece)){return ways;}
			Way way = new Way(newWay,piece);
			if(checkPropulsion){
				int propulsion = getPropulsion(way);
				if(propulsion<getGrainNumber(way)){return ways;}
			}
			Way bestWay = targetExists(way,piece,ways);
			if(bestWay!=way){return ways;}
			ways.add(way);
			if(card instanceof Grain && card.isTurned()){
				ways = branch(newWay, ways, piece, checkPropulsion);
			}
		}
		return ways;
	}

	private Way targetExists(Way newWay, Piece piece, List<Way> ways) {
		int index = 0;
		while(index<ways.size()){
			Way way = ways.get(index);
			if(newWay.getTarget()==way.getTarget()&&piece==way.getPiece()){
				if(getGrainNumber(way)>getGrainNumber(newWay)){
					ways.remove(index);
					return newWay;
				}
				if(getGrainNumber(way)<getGrainNumber(newWay)){
					return way;
				}
				if(way.getWay().size()>newWay.getWay().size()){
					ways.remove(index);
					return newWay;
				} else {
					return way;
				}
			} 
			index++;
		}
		return newWay;
	}

	private boolean accessible(Card card, Piece piece) {
		if(card instanceof Forest && card.isTurned()){
			if(card.getPieceTwo()!=null){
				if(card.getPieceTwo()==piece){return true;}
				if(card.getPieceTwo().getOwner()==getPlayer()&&card.getPiece().getOwner()==getPlayer()){
					return false;
				}
			}
		} else {
			if(card.getPiece()!=null){
				if(card.getPiece()==piece){return true;}
				if(card.getPiece().getOwner()==getPlayer()){
					return false;
				}
			}
		}
		return true;
	}

	private List<Way> branch(List<Card> newWay, List<Way> ways, Piece piece, boolean checkPropulsion) {
		Card card = newWay.get(newWay.size()-1);
		List<Card> neighbors = card.getNeighbors();
		for(Card neighbor : neighbors){
			List<Card> branch = copy(newWay);
			branch.add(neighbor);
			ways = getAllWaysForNeighbor(branch,ways,piece,checkPropulsion);
		}
		return ways;
	}

	private List<Card> copy(List<Card> newWay) {
		List<Card> copy = new Vector<Card>();
		copy.addAll(newWay);
		return copy;
	}

	private boolean passedCard(Card card, List<Card> newWay) {
		int count = 0;
		for(Card step : newWay){
			if(step==card){count++;}
		}
		return count>1;
	}

	protected void evaluateWays(List<Way> ways) {
		for(Way way : ways){
			evaluateWay(way);
		}
	}

	private void evaluateWay(Way way) {
		way.setValue(100);
		way.setValue(evaluateCard(way));
		way.setValue(evaluateEnemy(way));
		way.setValue(evaluateDanger(way));
	}

	private int evaluateDanger(Way way) {
		Card card = way.getTarget();
		List<Card> neighbors = card.getNeighbors();
		for(Card c : neighbors){
			if(way.getPiece() instanceof Archon){
				if(checkDangerForArchon(way, c)){return 1;}
			} else {
				if(checkDangerForHoplite(way, c)){return 1;}
			}
		}
		return way.getValue();
	}

	private boolean checkDangerForHoplite(Way way, Card c) {
		if(c.getPiece()!=null){
			if(c.getPiece().getOwner()!=getPlayer()
					&&!c.getPiece().blocked()
					&&!c.getPiece().anotherMustMove()){
				if(way.getTarget().getPiece()!=null){
					if(way.getTarget().getPiece().getOwner() == getPlayer()){
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			}
			if(c.getPieceTwo()!=null){
				if(c.getPieceTwo().getOwner()!=getPlayer()
						&&!c.getPieceTwo().anotherMustMove()){
					if (way.getTarget().getPiece() != null) {
						if (way.getTarget().getPiece().getOwner() == getPlayer()) {
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean checkDangerForArchon(Way way, Card c) {
		if(c.getPiece()!=null){
			if(c.getPiece().getOwner()!=getPlayer()
					&&!c.getPiece().blocked()
					&&!c.getPiece().anotherMustMove()){
				if(way.getTarget().getPiece() instanceof Archon){
					if(way.getTarget().getPiece().getOwner() != c.getPiece().getOwner()){
						return true;
					} else {
						return false;
					}
				} else {
					return true;
				}
			}
			if(c.getPieceTwo()!=null){
				if(c.getPieceTwo().getOwner()!=getPlayer()
						&&!c.getPieceTwo().anotherMustMove()){
					if(way.getTarget().getPiece() instanceof Archon){
						if(way.getTarget().getPiece().getOwner() != c.getPiece().getOwner()){
							return true;
						} else {
							return false;
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	private int evaluateEnemy(Way way) {
		Card card = way.getTarget();
		Piece piece = card.getPiece();
		if(piece!=null){
			if(card.getPieceTwo()!=null){
				if(card.getPieceTwo() instanceof Archon){piece = card.getPieceTwo();}
			}
			if(piece.getOwner()!=getPlayer()){
				if(piece instanceof Archon){
					return 1000000;
				} else {
					return 50000;
				}
			}
		}
		return way.getValue();
	}

	private int evaluateCard(Way way) {
		Card card = way.getTarget();
		if(!card.isTurned()){
			if(way.getPiece() instanceof Archon){
				if(uncoveredBarbarians()<3){
					return 1;
				}
			} else {
				if(uncoveredBarbarians()==3){
					return 500;
				}
			}
		} else {
			if(card instanceof Barbarians){return 1;}
			if(card instanceof Mountain){return 20;}	
			if(card instanceof Forest && way.getPiece() instanceof Hoplite){return 150;}
			if(card instanceof Town 
					&& way.getPiece() instanceof Archon 
					&& card.getOwner() != getPlayer()){
				return 50000;
			}
		}
		return 100;
	}

	private int uncoveredBarbarians() {
        Game game = getPlayer().getGame();
        int barbarians = 0;
        for(int y=0;y<8;y++){
            for(int x=0;x<8;x++){
                if((game.getCard(x,y) instanceof Barbarians)&&(game.getCard(x,y).isTurned())){barbarians++;}
            }
        }
		return barbarians;
	}

	private void evaluateNecessities(List<Way> ways) {
		String attackingType = archonUnderAttack(ways);
		if(!attackingType.equals("")){
			for(Way way : ways){
				if(!(way.getPiece() instanceof Archon)){
					Piece archon = way.getTarget().getPiece();
					if(archon instanceof Archon){
						String type = archon.getOwner().getType();
						if(!attackingType.equals(type)){
							way.setValue(1);
						}
					} else {
						way.setValue(1);
					}
				}
			}
		}
	}

	private String archonUnderAttack(List<Way> ways){
		for(Way way : ways){
			if(way.getPiece() instanceof Archon){
				Card c = way.getTarget();
				if(c.getPiece()!=null){
					if(c.getPiece().getOwner()!=getPlayer()){
						return c.getPiece().getOwner().getType();
					} else {
						if(c.getPieceTwo()!=null){
							if(c.getPieceTwo().getOwner()!=getPlayer()){
								return c.getPieceTwo().getOwner().getType();
							}
						}
					}
				}
			}
		}
		return "";
	}
	
	private Way selectWay(List<Way> evaluatedWays) {
		double sum = 0;
		for(Way way : evaluatedWays){
			sum+=way.getValue();
		}
		int select = (int)(Math.random()*sum);
		int count = 0;
		for(Way way : evaluatedWays){
			count+=way.getValue();
			if(count>select){return way;}
		}
		return null;
	}

}
