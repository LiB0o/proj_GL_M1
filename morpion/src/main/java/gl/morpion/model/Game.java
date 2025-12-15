package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

/**
 * <h1>class Game</h1>
 * Core game logic class for tic-tac-toe.
 * Manages game state, victory conditions, player turns, and move history.
 * Supports variable board sizes and configurable win conditions.
 * <h2>Elements of Game</h2>
 */
public class Game {
	/**
	 * Default number of aligned symbols required to win
	 */
    private static int DEFAULT_MAX_SYMBOL_ALIGN = 5;

	/**
	 * Current number of aligned symbols required to win this game
	 */
	private int MaxNumberSymbolAlign = DEFAULT_MAX_SYMBOL_ALIGN;

	/**
	 * Map of board positions to symbols currently placed
	 */
	private HashMap<Pair<Integer,Integer>,Symbol> usedCase;

	/**
	 * The game board
	 */
	private GameBoard gameBoard;

	/**
	 * List of players in the game
	 */
	List<Player> players;

	/**
	 * Whether the game has ended
	 */
	private boolean end=false;

	/**
	 * Current active player and player references
	 */
	private Player currentPlayer, p1, p2;

	/**
	 * Stack storing move history for undo functionality
	 */
	private Stack<Move> moveHistory = new Stack<>();

	/**
	 * <h2>Functions of Game</h2>
	 */

	/**
	 * <h3>Game</h3>
	 * Creates a new game instance with specified board and players.
	 *
	 * @param board the game board
	 * @param p1 first player
	 * @param p2 second player
	 * @param currentPlayer the player who starts the game
	 */
	public Game(GameBoard board, Player p1, Player p2, Player currentPlayer){
		this.p1 = p1;
		this.p2 = p2;
		this.gameBoard = board;
		this.players = new ArrayList<>();
		this.usedCase = new HashMap<>();
		this.currentPlayer = currentPlayer;
        this.MaxNumberSymbolAlign = DEFAULT_MAX_SYMBOL_ALIGN;
	}

	/**
	 * <h3>addPlayer</h3>
	 * Adds a player to the game's player list.
	 *
	 * @param player the player to add
	 */
	public void addPlayer(Player player){
		this.players.add(player);
	}
	/**
	 * <h3>checkClassicVictory</h3>
	 * Tests all positions on the board to check if there is a winning alignment.
	 * Checks horizontal, vertical, and diagonal alignments for each position.
	 *
	 * @param limit how many symbols are needed aligned to win
	 * @return a Pair with boolean (true if victory) and the winner's Symbol (if victory detected)
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
	 * <h3>checkDiagonalUpLeft_DownRight</h3>
	 * Checks for a winning diagonal alignment from upper-left to lower-right direction.
	 * Tests both forward and backward from the given position.
	 *
	 * @param key position to check (first Integer is row, second is column)
	 * @param limit how many aligned symbols needed to win
	 * @return true if winning alignment found, false otherwise
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
    public boolean playTurn(int x, int y) {

        // Case déjà occupée → on ne joue PAS
        if (!this.gameBoard.isEmptyCase(x, y)) {
            System.out.println("Case déjà occupée !");
            return false; // ❌ coup refusé
        }

        // Sauvegarder l'état avant le coup pour l'undo
        Move move = new Move(x, y, currentPlayer, this.end,
                            players.get(0).getPoints(),
                            players.get(1).getPoints());
        moveHistory.push(move);

        // Case libre → jouer le coup
        this.gameBoard.placeSymbol(currentPlayer.getSymbol(), x, y);

        // Vérifier la victoire
        Pair<Boolean, Symbol> victory = this.checkClassicVictory(this.MaxNumberSymbolAlign);
        if (victory.getKey() && currentPlayer.getSymbol() == victory.getValue()) {
            currentPlayer.addPoint();
            this.end = true;
        }

        if (this.allCaseFilled()) {
            System.out.println("Toutes les cases sont remplies !");
        }

        // Si quelqu'un a des points, on considère que c'est fini
        if (players.get(0).getPoints() != 0 || players.get(1).getPoints() != 0) {
            this.end = true;
        }

        return true; // ✅ coup joué
    }
    public static int getDefaultMaxNumberSymbolAlign() {
        return DEFAULT_MAX_SYMBOL_ALIGN;
    }

    public static void setDefaultMaxNumberSymbolAlign(int value) {
        if (value < 3) value = 3;
        if (value > 8) value = 8;
        DEFAULT_MAX_SYMBOL_ALIGN = value;
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



	public HashMap<Pair<Integer, Integer>, Symbol> getUsedCase() {
		return usedCase;
	}

	/**
	 * Undo the last move played
	 * @return true if undo was successful, false if no move to undo
	 */
	public boolean undo() {
		if (moveHistory.isEmpty()) {
			return false;
		}

		Move lastMove = moveHistory.pop();

		// Restaurer la case vide
		gameBoard.setSymbolAt(lastMove.getX(), lastMove.getY(), null);

		// Restaurer l'état du jeu
		this.currentPlayer = lastMove.getPlayerBefore();
		this.end = lastMove.isEndBefore();

		// Restaurer les points
		players.get(0).setPoints(lastMove.getP1PointsBefore());
		players.get(1).setPoints(lastMove.getP2PointsBefore());

		return true;
	}

	/**
	 * Check if undo is available
	 * @return true if there are moves to undo
	 */
	public boolean canUndo() {
		return !moveHistory.isEmpty();
	}
}