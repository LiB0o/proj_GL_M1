package gl.morpion.model;

import javafx.util.Pair;

import java.util.*;

public class Game {

	private int turn=0;
    private HashMap<Pair<Integer,Integer>,Symbol> usedCase;
	private GameBoard gameBoard;
	List<Player> players;
    private boolean fin=false;

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
        //Pair<Boolean,Symbol> result = new Pair<Boolean,Symbol>(victory,winner);
        //System.out.println(result.getValue().getTypeOfSymbol());
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
		int verif = 1; // permit to check if the column with
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
		int verif = 1; // permit to check if the column with
		//Pair<Integer,Integer> x = line, y = column
		Symbol testedSymbol = usedCase.get(key); //Symbol of the case you test victory

		if(testedSymbol != null){
			for(int i=0;i<limit;i++){ //behind
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
			for(int i=0;i<limit;i++){ //forward
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
    public void playTurn(int x, int y) {
        int i = 0;

        while ( i <= gameBoard.getColumn()*gameBoard.getRow()) {
            Player currentPlayer;

            if (i % 2 == 0) { // i pair → joueur 1
                currentPlayer = players.get(0);
            } else { // i impair → joueur 2
                currentPlayer = players.get(1);
            }

            if (this.gameBoard.placeSymbol(currentPlayer.getSymbol(), x, y)) {
                this.gameBoard.symbols.get(x)[y] = currentPlayer.getSymbol();
                i++; // coup joué → on change de joueur

            }

        }
    }
    public void plaayTurn(int x, int y) {
        Player currentPlayer = players.get(turn % 2); // joueur actuel

        // Vérifier si la case est vide
        if (this.gameBoard.symbols.get(x)[y] == null) {
            // Placer le symbole dans la grille
            this.gameBoard.symbols.get(x)[y] = currentPlayer.getSymbol();
            Pair<Boolean, Symbol> victory =  this.checkClassicVictory(5);
            if(victory.getKey() && currentPlayer.getSymbol() == victory.getValue()){
                System.out.println(victory.getValue());
                currentPlayer.addPoint();
                System.out.println("Points de   " + currentPlayer.getName()+" :"+ currentPlayer.getPoints());


                System.out.println("Vicoire pour  " + currentPlayer.getName());
            }

            // Incrémenter le tour pour passer au joueur suivant
            turn++;

        } else {
            System.out.println("Case déjà occupée !");
        }
        if( players.getFirst().getPoints()!=0 || players.getLast().getPoints()!=0){
            this.fin=true;
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
    public boolean getFin() {
        return fin;
    }
//Force Change

}