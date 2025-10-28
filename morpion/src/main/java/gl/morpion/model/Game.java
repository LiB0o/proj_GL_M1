package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

public class Game {

	private HashMap<Pair<Integer,Integer>,Symbol> usedCase;
	private GameBoard gameBoard;
	List<Player> players;

	/**
	 * Create the instance of Game
	 * @param board of type GameBoard
	 * @param players as a List<Player>
	 */
	public Game(GameBoard board,List<Player> players){
		this.gameBoard = board;
		this.players = new ArrayList<>();

		this.players.addAll(players);
		this.usedCase = new HashMap<>();
	}

	/**
	 * test every possibility for a case to check if there is a win
	 * @param limit how many symbols are needed next to each other to win
	 * @return a pair with a boolean and the symbol of the winner (if boolean == true)
	 */
	public Pair<Boolean,Symbol> checkClassicVictory(int limit) {
		boolean victory = false;
		Symbol winner = null;

		//TODO: get the HashMap<> from the GameBoard fonction
		this.usedCase = gameBoard.getSymbolInPair();

		for(Pair<Integer,Integer>key : this.usedCase.keySet()){
			victory = victory || (checkColumn(key,limit)||
					checkDiagonalUpLeft_DownRight(key,limit)||
					checkDiagonalUpRight_DownLeft(key,limit)||
					checkLine(key,limit));
			if(victory){
				winner = usedCase.get(key);
			}
		}

		return new Pair<Boolean,Symbol>(victory,winner);
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
		int verif = 0; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=0;i<limit;i++){ //behind (left)
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
			for(int i=0;i<limit;i++){ //forward
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
		int verif = 0; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=0;i<limit;i++){ //behind (left)
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
			for(int i=0;i<limit;i++){ //forward (right)
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
		int verif = 0; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=0;i<limit;i++){ //behind
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey(), key.getValue()-i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey(), key.getValue()-i))){ //if it have the same symbol
						verif += 1;
						System.out.println("checkColumn: je suis position"+(key.getKey())+","+(key.getValue()-i)+" augmente verif de 1 \n");
					}
					else{
						folowed = false;
						System.out.println("checkColumn: je suis position"+(key.getKey())+","+(key.getValue()-i)+" je n ai pas le bon symbol \n");
					}
				}
				else{
					folowed = false;
					System.out.println("checkColumn: je suis position"+(key.getKey())+","+(key.getValue()-i)+" je nai pas de symbol \n");
				}

				if(folowed == false){
					i = limit;
				}
			}

			folowed = true;
			for(int i=0;i<limit;i++){ //forward
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey(), key.getValue()+i))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey(), key.getValue()+i))){ //if it have the same symbol
						verif += 1;
						System.out.println("checkColumn: je suis position"+(key.getKey())+","+(key.getValue()+i)+" augmente verif de 1 \n");
					}
					else{
						folowed = false;
						System.out.println("checkColumn: je suis position"+(key.getKey())+","+(key.getValue()+i)+" je n ai pas le bon symbol \n");
					}
				}
				else{
					folowed = false;
					System.out.println("checkColumn: je suis position"+(key.getKey())+","+(key.getValue()+i)+" je nai pas de symbol \n");
				}

				if(folowed == false){
					i = limit;
				}
			}
		}

		System.out.println("verif est a :"+verif+"\n");
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
		int verif = 0; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=0;i<limit;i++){ //behind
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()-i, key.getValue()))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()-i, key.getValue()))){ //if it have the same symbol

						System.out.println("checkLine: je suis position"+(key.getKey()-i)+","+key.getValue()+" augmente verif de 1 \n");
						verif += 1;
					}
					else{
						folowed = false;
						System.out.println("checkLine: je suis position"+(key.getKey()-i)+","+key.getValue()+" je suis pas du bon symbol \n");
					}
				}
				else{
					folowed = false;
					System.out.println("checkLine: je suis position"+(key.getKey()-i)+","+key.getValue()+" je n ai pas de symbol \n");
				}

				if(folowed == false){
					i = limit;
				}
			}

			folowed = true;

			for(int i=0;i<limit;i++){ //forward
				if(usedCase.containsKey(
						new Pair<Integer,Integer>(key.getKey()+i, key.getValue()))){ //if the case exist
					if(testedSymbol == usedCase.get(
							new Pair<Integer,Integer>(key.getKey()+i, key.getValue()))){ //if it have the same symbol
						verif += 1;
						System.out.println("checkLine: je suis position"+(key.getKey()+i)+","+key.getValue()+" augmente verif de 1 \n");
					}
					else{
						folowed = false;
						System.out.println("checkLine: je suis position"+(key.getKey()+i)+","+key.getValue()+" je suis pas du bon symbol \n");
					}
				}
				else{
					folowed = false;
					System.out.println("checkLine: je suis position"+(key.getKey()+i)+","+key.getValue()+" je n ai pas de symbol \n");
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
	 * 
	 * @param board
	 * @param players
	 */
	/*public void playTurn(GameBoard board, Player[] players, int limit) {
		if(board != null && players != null){
			int i = 0;
			while(this.checkClassicVictory(limit) && i < 100){
				for(Player p : players){
					//Je fais quoi ?
				}
			}
		}
	}*/

}