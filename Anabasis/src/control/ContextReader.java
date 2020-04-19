package control;

import gametools.Game;

import java.util.List;

import javax.servlet.ServletContext;

import datatools.User;

public class ContextReader {
	
	private ServletContext context;

	public ContextReader(ServletContext context) {
		super();
		this.context = context;
	}
	
	public Game getGame(int matchId) {
		@SuppressWarnings("unchecked")
		List<Playing> runningGames = (List<Playing>) context.getAttribute(UserControl.RUNNING);
		if(runningGames==null){return null;}
		for(Playing playing : runningGames){
			Game game = playing.getGame(); 
			if(game.getMatch().getId()==matchId){
				return game;
			}
		}
		return null;
	}

	public Playing getPlaying(User user) {
		@SuppressWarnings("unchecked")
		List<Playing> runningGames = (List<Playing>) context.getAttribute(UserControl.RUNNING);
		for(Playing playing : runningGames){
			if(playing.getGame().getMatch().has(user.getId())){
				return playing;
			}
		}
		return null;
	}
	
	public void updateGameEnds() {
		@SuppressWarnings("unchecked")
		List<Playing> runningGames = (List<Playing>) context.getAttribute(UserControl.RUNNING);
		if(runningGames==null){return;}
		int index = 0;
		while(index<runningGames.size()){
			Playing p = runningGames.get(index);
			if(p.getGame().getMatch().hasEnded()){
				runningGames.remove(index);
			} else {
				index++;
			}
		}
	}


}
