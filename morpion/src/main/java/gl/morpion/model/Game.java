package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

public class Game {
	private int MaxNumberSymbolAlign = 5;
	private HashMap<Pair<Integer,Integer>,Symbol> usedCase;
	private GameBoard gameBoard;
	List<Player> players;
	private boolean end=false;
	private Player currentPlayer, p1, p2;

	/**
	 * Create the instance of Game
	 * @param board of type GameBoard
	 */
	public Game(GameBoard board, Player p1, Player p2, Player currentPlayer){
		this.p1 = p1;
		this.p2 = p2;
		this.gameBoard = board;
		this.players = new ArrayList<>();
		this.usedCase = new HashMap<>();
		this.currentPlayer = currentPlayer;
	}
	public void addPlayer(Player player){
		this.players.add(player);
	}
	/**
	 * test every possibility for a case to check if there is a win
	 * @param limit: how many symbols are needed next to each other to win
	 * @return a pair with a boolean and the symbol of the winner (if boolean == true)
	 */
	public Pair<Boolean,Symbol> checkClassicVictory(int limit) {
		boolean victory = false;
		Symbol winner = null;
		Pair<Boolean,Symbol> winnerPair = new Pair<>(false,null);

		//TODO: get the HashMap<> from the GameBoard fonction
		this.usedCase = gameBoard.getSymbolInPair();

		for(Pair<Integer,Integer>key : this.usedCase.keySet()){
			victory = victory || (checkColumn(key,limit)||
					checkDiagonalUpLeft_DownRight(key,limit)||
					checkDiagonalUpRight_DownLeft(key,limit)||
					checkLine(key,limit));
			if(victory){
				//winner = usedCase.get(key);
				winnerPair =  new Pair<>(true,usedCase.get(key));
				//System.out.println(winner.getTypeOfSymbol());
				victory = false;
			}
		}
		return winnerPair;
	}

	/**
	 * check if you win with on a diagonal from up left to down right
	 * @param key position of case, first Integer is x/line and the second Integer is y/column
	 * @param limit how many symbols are needed next to each other to win
	 * @return boolean
	 */
	private boolean checkDiagonalUpLeft_DownRight(Pair<Integer,Integer>key,int limit){
		boolean result = false;
		boolean folowed = true;
		int verif = 1; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=1;i<limit;i++){ //behind (left)
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()-i, key.getValue()-i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()-i, key.getValue()-i))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}

			folowed = true;
			for(int i=1;i<limit;i++){ //forward
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()+i, key.getValue()+i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()+i, key.getValue()+i))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}
		}

		if(verif >= limit){ //if we win
			result = true;
		}

		return result;
	}

	/**
	 * check if you win with on a diagonal from down left to up right
	 * @param key position of case, first Integer is x/line and the second Integer is y/column
	 * @param limit how many symbols are needed next to each other to win
	 * @return boolean
	 */

	private boolean checkDiagonalUpRight_DownLeft(Pair<Integer,Integer>key,int limit){
		boolean result = false;
		boolean folowed = true;
		int verif = 1; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=1;i<limit;i++){ //behind (left)
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()+i, key.getValue()-i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()+i, key.getValue()-i))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}

			folowed = true;
			for(int i=1;i<limit;i++){ //forward (right)
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()-i, key.getValue()+i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()-i, key.getValue()+i))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}
		}

		if(verif >= limit){ //if we win
			result = true;
		}

		return result;
	}

	/**
	 * check if you win with on a column
	 *
	 * @param key position of case, first Integer is x/line and the second Integer is y/column
	 * @param limit how many symbols are needed next to each other to win
	 * @return boolean
	 */
	private boolean checkColumn(Pair<Integer,Integer> key ,int limit){
		boolean result = false;
		boolean folowed = true;
		int verif = 1; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=1;i<limit;i++){ //behind
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey(), key.getValue()-i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey(), key.getValue()-i))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}

			folowed = true;
			for(int i=1;i<limit;i++){ //forward
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey(), key.getValue()+i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey(), key.getValue()+i))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}
		}

		if(verif >= limit){ //if we win
			result = true;
		}

		return result;
	}

	/**
	 * check if you win with on a line
	 * @param key position of case, first Integer is x/line and the second Integer is y/column
	 * @param limit how many symbols are needed next to each other to win
	 * @return boolean
	 */
	private boolean checkLine(Pair<Integer,Integer>key,int limit){
		boolean result = false;
		boolean folowed = true;
		int verif = 1; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=1;i<limit;i++){ //behind
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()-i, key.getValue()))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()-i, key.getValue()))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}

			folowed = true;
			for(int i=1;i<limit;i++){ //forward
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()+i, key.getValue()))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()+i, key.getValue()))){ //if it have the same symbol
						verif += 1;
					}
					else{
						folowed = false;
					}
				}
				else{
					folowed = false;
				}

				if(folowed == false){
					i = limit;
				}
			}
		}

		if(verif >= limit){ //if we win
			result = true;
		}

		return result;
	}

	public void swap(){
		currentPlayer = currentPlayer == players.get(0) ?
				players.get(1) :
				players.get(0);
	}

	/**
	 *
	 * @return : detect whether there is a draw
	 */
	public Boolean allCaseFilled(){//TODO: probleme
		int rows = this.gameBoard.getRow();
		int cols = this.gameBoard.getColumn();
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				if(this.gameBoard.isEmptyCase(i, j)){
					return false;
				}
			}
		}
		return true;
	}


	/**
	 * @param x: row
	 * @param y : col
	 * @return :
	 */
	public void playTurn(int x, int y) {
		// Vérifier si la case est vide
		if (this.gameBoard.isEmptyCase(x, y)) {
			// Placer le symbole sur la case (x, y)
			this.gameBoard.placeSymbol(currentPlayer.getSymbol(), x, y);
			// check the victory
			Pair<Boolean, Symbol> victory =  this.checkClassicVictory(this.MaxNumberSymbolAlign);
			if(victory.getKey() && currentPlayer.getSymbol() == victory.getValue()){
				System.out.println(victory.getValue());
				currentPlayer.addPoint();
			}
			if(!allCaseFilled()){
				System.out.println("ça reste encore des cases à remplir");
			}
			if(this.allCaseFilled()){
				System.out.println("Toute les case sont remplie donc pas de victoire!!!!");
			}
		} else {
			System.out.println("Case déjà occupée !");
		}
		if( players.getFirst().getPoints()!=0 || players.getLast().getPoints()!=0){
			this.end=true;
		}

	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public boolean getEnd() {
		return end;
	}
//Force Change

}