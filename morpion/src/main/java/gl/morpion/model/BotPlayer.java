package gl.morpion.model;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class BotPlayer extends Player {

	private float full_coef;

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

	/**
	 * Create a Bot to play with
	 *
	 * @param name
	 * @param point
	 * @param coef
	 * @param symbol
	 * @param limit the winning condition
	 */
	public BotPlayer(String name,
					 int point,
					 float coef,
					 Symbol symbol,
					 int limit) {
		super(name, point,symbol);

		this.full_coef = coef;

		this.win_condition = limit;
		this.boardView = new HashMap<Pair<Integer,Integer>,Integer>(); //Init with function setBotBoard
	}

	/**
	 * Generate what the Bot will use to visualise where every symbol are placed
	 * This procedure just initialize the Board
	 * @param usableCases
	 */
	public void setBotBoard(List<Pair<Integer, Integer>> usableCases){
		for(Pair<Integer,Integer> p : usableCases){
			this.boardView.put(p,1); //Set all cases to 1
		}
	}

	public float getLevel(){
		return this.full_coef;
	}


	public void setLevel(float coef){
		this.full_coef = coef;
	}

	//Calcul vertical d'une case

	private float verticalValueOfCase(Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = (position.getValue()-(this.win_condition+1)); i < position.getValue()+1; i++){
			int nb_bot_symbol = 0; //number of time where the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time where the symbol of the enemy is present
			if(this.boardView.containsKey(position)){ //If the case is present

				for(int j = 0; j <= this.win_condition; j++){
					Pair<Integer, Integer> testCase = new Pair<>(position.getKey(),i+j);
					if(this.boardView.get(testCase) == -1){ //If the case has a symbol from the adverser
						nb_adverse_symbol += 1;
					}
					if(this.boardView.get(testCase) == 0){ //If the case has a symbol from the bot
						nb_bot_symbol += 1;
					}

					/*if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
						j = this.win_condition*2; //to stop the for
					}*/
				}
				if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
					value = value;
				}
				else{
					if(nb_bot_symbol !=0){

						float add = 3.0f;

						for(int compt = 0; compt<nb_bot_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else{
						float add = 2.0f;

						for(int compt = 0; compt<nb_adverse_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
				}

				
			}
		}

		if(value == 0.0f){ //mostly for the beginning of the game
			value = 1.0f;
		}
		return value;
	}

	//Calcul horizontal d'une case
	private float horizontalValueOfCase(Pair<Integer, Integer> position){
		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = (position.getKey()-(this.win_condition+1)); i < position.getKey()+1; i++){
			int nb_bot_symbol = 0; //number of time where the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time where the symbol of the enemy is present

			if(this.boardView.containsKey(position)) { //If the case is present

				for(int j = 0; j <= this.win_condition; j++){

					Pair<Integer, Integer> testCase = new Pair<>(i+j, position.getValue());

					if(this.boardView.get(testCase) == -1){ //If the case has a symbol from the adverser
						nb_adverse_symbol += 1;
					}
					if(this.boardView.get(testCase) == 0){ //If the case has a symbol from the bot
						nb_bot_symbol += 1;
					}
				}

				if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
					value = value;
				}
				else{
					if(nb_bot_symbol !=0){

						float add = 3.0f;
						for(int compt = 0; compt<nb_bot_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else{
						float add = 2.0f;
						for(int compt = 0; compt<nb_adverse_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
				}

			}


		}

		if(value == 0.0f){ //mostly for the start of the game
			value = 1.0f;
		}

		return value;
	}


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