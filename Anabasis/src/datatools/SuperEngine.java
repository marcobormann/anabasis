package datatools;

public class SuperEngine extends Engine {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SuperEngine(String name, int rating) {
		super(name, rating);
	}

	@Override
	public Move evaluate(Move m) {
		return null;
	}
	
	public void move(){};

}
