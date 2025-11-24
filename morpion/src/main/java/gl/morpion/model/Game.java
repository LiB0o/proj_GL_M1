package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

/**
 * <h1>class Game</h1>
 * <h2>Elements of Game</h2>
 */


public class Game {

	/**
	 * <h3>MaxNumberSymbolAlign</h3>
	 * How many symbols align are needed to win
	 */
	private int MaxNumberSymbolAlign = 5;

	/**
	 * <h3>usedCase</h3>
	 * Cases already played on with their position and symbol
	 */
	private HashMap<Pair<Integer,Integer>,Symbol> usedCase;

	/**
	 * <h3>gameBoard</h3>
	 * The board played on
	 */
	private GameBoard gameBoard;

	/**
	 * <h3>players</h3>
	 * List of every player
	 */
	List<Player> players;


	private boolean end=false;

	private Player currentPlayer, p1, p2;

	/**
	 * <h3>Game</h3>
	 *
	 * Create the game
	 *
	 * @param board the board
	 * @param p1
	 * @param p2
	 * @param currentPlayer
	 */
	public Game(GameBoard board, Player p1, Player p2, Player currentPlayer){
		this.p1 = p1;
		this.p2 = p2;
		this.gameBoard = board;
		this.players = new ArrayList<>();
		this.usedCase = new HashMap<>();
		this.currentPlayer = currentPlayer;
	}

	/**
	 * <h3>addPlayer</h3>
	 * Add a player in the list players
	 * @param player the new player
	 */
	public void addPlayer(Player player){
		this.players.add(player);
	}

	/**
	 * <h3>checkClassicVictory</h3>
	 *
	 * Test every possibility for a win using the four functions below.
	 *
	 * @param limit the winning condition (how many symbols align to win)
	 * @return a boolean and a symbol (if a win: true,symbol; if no win: false, null)
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
	 * <h3>private checkDiagonalUpLeft_DownRight</h3>
	 *
	 * check if you win with on a diagonal from up left to down right
	 * @param key position of case, first Integer is x/line and the second Integer is y/column
	 * @param limit how many symbols are needed next to each other to win
	 * @return true or false
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
	 * <h3>private checkDiagonalUpRight_DownLeft</h3>
	 *
	 * check if you win with on a diagonal from down left to up right
	 * @param key position of case, first Integer is x/line and the second Integer is y/column
	 * @param limit how many symbols are needed next to each other to win
	 * @return true or false
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
	 * <h3>private checkColumn</h3>
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
	 * <h3>private checkLine</h3>
	 *
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

	//ask Abdou
	public void swap(){
		currentPlayer = currentPlayer == players.get(0) ?
				players.get(1) :
				players.get(0);
	}

	/**
	 * <h3>allCaseFilled</h3>
	 *
	 * detect whether there is a draw
	 * @return true if it detects a draw
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
	 * <h3>playTurn</h3>
	 *
	 * Manage a turn
	 *
	 * @param x : column
	 * @param y : row
	 * @return false if the turn need to reroll (no move) or true if need to stop/next turn
	 */
    public boolean playTurn(int x, int y) {

        // Case already occupied → DON'T PLAY
        if (!this.gameBoard.isEmptyCase(x, y)) {
            System.out.println("Case has already a symbol !");
            return false; // ❌ refuse move
        }

        // Free case → play the move
        this.gameBoard.placeSymbol(currentPlayer.getSymbol(), x, y);

        // Check victory
        Pair<Boolean, Symbol> victory = this.checkClassicVictory(this.MaxNumberSymbolAlign);
        if (victory.getKey() && currentPlayer.getSymbol() == victory.getValue()) {
            currentPlayer.addPoint();
            this.end = true;
        }

        if (this.allCaseFilled()) {
            System.out.println("All cases are full !");
        }

        // If someone have points, it is over
        if (players.get(0).getPoints() != 0 || players.get(1).getPoints() != 0) {
            this.end = true;
        }

        return true; // ✅ move played
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