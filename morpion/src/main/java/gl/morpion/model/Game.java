package gl.morpion.model;

import java.util.*;

public class Game {

	private GameBoard gameBoard;
	List<Player> players;

	public Game(GameBoard board,List<Player> players){
		this.gameBoard = board;
		this.players = new ArrayList<>();

		this.players.addAll(players);
	}

	public boolean checkVictory() {
		// TODO - implement Game.checkVictory
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param board
	 * @param players
	 */
	public void playTurn(GameBoard board, Player[] players) {
		// TODO - implement Game.playTurn
		throw new UnsupportedOperationException();
	}

}