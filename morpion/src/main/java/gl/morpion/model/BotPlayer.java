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
	 * @param name name of the bot
	 * @param point how many point does the bot have
	 * @param coef the bot level
	 * @param symbol the symbol the bot will be using to play
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
	 * @param usableCases the playable cases
	 */
	public void setBotBoard(List<Pair<Integer, Integer>> usableCases){
		for(Pair<Integer,Integer> p : usableCases){
			this.boardView.put(p,1); //Set all cases to 1
		}
	}

	/**
	 * @return the full_coeff used for the Bot
	 */
	public float getLevel(){
		return this.full_coef;
	}

	/**
	 * change the level of the bot
	 * @param coef the new coef of the bot
	 */
	public void setLevel(float coef){
		this.full_coef = coef;
	}

	/**
	 * This function will ONLY calculate the vertical value of the chosen position in a float format
	 * This value does not represent the entire value of the position.
	 * At minimum the value would be of 1
	 *
	 * @param position the position that will be tested
	 * @return the vertical value of the position
	 */
	private float verticalValueOfCase(Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = (position.getValue()-(this.win_condition+1)); i < position.getValue()+1; i++){
			int nb_bot_symbol = 0; //number of time when the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time when the symbol of the enemy is present

			Pair<Integer, Integer> neighbour = new Pair<>(position.getKey(), i);
			if(this.boardView.containsKey(neighbour)){ //If the case is present

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

	/**
	 * This function will ONLY calculate the horizontal value of the chosen position in a float format
	 * This value does not represent the entire value of the position.
	 * At minimum the value would be of 1
	 *
	 * @param position the position that will be tested
	 * @return the horizontal value of the position
	 */
	private float horizontalValueOfCase(Pair<Integer, Integer> position){
		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = (position.getKey()-(this.win_condition+1)); i < position.getKey()+1; i++){
			int nb_bot_symbol = 0; //number of time when the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time when the symbol of the enemy is present

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


	//Calcul diagonal bas vers haut d'une case

	private float diagonalDownToUpValueOfCase(Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case



		if(value == 0.0f){ //mostly for the start of the game
			value = 1.0f;
		}
		return value;
	}



	//Calcul diagonal haut vers bas d'une case

	private float diagonalUpToDownValueOfCase(Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case



		if(value == 0.0f){ //mostly for the start of the game
			value = 1.0f;
		}
		return value;
	}


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