package datatools;

import gametools.Card;
import gametools.Piece;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class Way implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Card> way;
	private Piece piece;
	private int value = 0;

	public Way(List<Card> way, Piece piece){
		this.way = way;
		this.piece = piece;
	}
	public Way(Way way){
		this.way = way.getWay();
		this.piece = way.getPiece();
		this.value = way.getValue();
	}
	public Way(){
		way = new Vector<Card>();
	}
	
	public List<Card> getWay() {
		return way;
	}
	public void setWay(List<Card> way) {
		this.way = way;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	public int size(){
		return way.size();
	}
	public void add(Card card){
		way.add(card);
	}

	public Card getTarget(){
		if(way.size()>0){
			return way.get(way.size()-1);
		} else {
			return piece.getCard();
		}
	}
	@Override
	public String toString() {
		String w = "";
		for(Card card : way){
			w=w+card.toString();
		}
		return "Way [piece=" + piece.getCard().toString() + ",value = "+value+", way=" + w + "]";
	}
	
	
}
