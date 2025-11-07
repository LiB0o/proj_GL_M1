package gl.morpion.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class BotPlayer extends Player {

	//private float level;
	private float coef_attack; // Used to calculate the best move offensively
	private float coef_defence; //Used to calculate the best move defensively

	public int win_condition;

	/*
	* All cases on the board (even with a symbol)
	* Each position (Pair<Integer, Integer>) get a value depending of the possible move on it
	* If the case is occupied by the symbol of the Bot the value is equal to 0
	* If the case is occupied by the symbol of the Adverser the value is equal to -1
	* Before calculation, every case equal 1
	* */
	public HashMap<Pair<Integer, Integer>,Integer> boardView;

	//private GameBoard board; // May be needed to know with symbol is on every case is there is more than 2 symbols

	////////////////////////////////////////////////////////////////////////////////////////////

	public BotPlayer(String name,
					 int point,
					 float attack,
					 float defence,
					 Symbol symbol,
					 int limit) {
		super(name, point,symbol);

		this.coef_defence = defence;
		this.coef_attack = attack;

		this.win_condition = limit;
		this.boardView = new HashMap<Pair<Integer,Integer>,Integer>(); //Init with function setBotBoard
	}


	public void setBotBoard(List<Pair<Integer, Integer>> usableCases){
		for(Pair<Integer,Integer> p : usableCases){
			this.boardView.put(p,1); //Set all cases to 1
		}
	}

	public Pair<Float,Float> getLevel() {
		return new Pair<>(coef_attack, coef_defence);
	}

	public void setLevel(float attack, float defence) {
		this.coef_attack = attack;
		this.coef_defence = defence;
	}

	//Calcul vertical d'une case


	//Calcul horizontal d'une case


	//Calcul diagonal d'une case


	//Calcul de tout le plateau


	//Change symbol if Bot put a symbol

	public void symbolPutByBot(Pair<Integer,Integer> position){
		this.boardView.replace(position,0);
	}

	//Change symbol if Adverser put a symbol

	public void symbolPutByPlayer(Pair<Integer, Integer> position){
		this.boardView.replace(position,-1);
	}

	//Reset case
	public void resetValueCase(Pair<Integer,Integer> position){
		this.boardView.replace(position,1);
	}

	//Recalcul des cases impactés par

}