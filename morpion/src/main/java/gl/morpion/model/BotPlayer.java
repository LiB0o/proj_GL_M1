package gl.morpion.model;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;


/**
 * <h1>class BotPlayer</h1>
 * <h2>Elements of BotPlayer</h2>
 */

public class BotPlayer extends Player {
    public static final float EASY_LEVEL   = 2.0f;
    public static final float NORMAL_LEVEL = 3.0f;
    public static final float HARD_LEVEL   = 3.5f;

	/**
	 * <h3>full_coef</h3>
	 *
	 * Represent the coefficient the bot will use to calculate the importance of every case on the board
	 */
	private float full_coef;

	/**
	 * <h3>win_condition</h3>
	 *
	 * How many symbols align are needed to win
	 */

	public int win_condition;
    // Niveau par défaut utilisé pour créer les bots
    private static float currentDefaultLevel = NORMAL_LEVEL;
	
	/**
	 * <h3>boardView</h3>
	 *
	 * Represent all cases on the board (even with a symbol)
	 * Each position (Pair<Integer, Integer>) get a value depending of the possible move on it :
	 * - If the case is occupied by the symbol of the Bot the value is equal to 0
	 * - If the case is occupied by the symbol of the Adverser the value is equal to -1
	 * - Before calculation / When initialised, every case equal 1
	 * - If it is different from -1, 0 ot 1 then it is the value of the case for the bot
	*/
	public HashMap<Pair<Integer, Integer>,Float> boardView;

	//private GameBoard board; // May be needed to know with symbol is on every case is there is more than 2 symbols

	////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * <h2>Functions of BotPlayer</h2>
	 */




	/**
	 * <h3>BotPlayer</h3>
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
					 int limit,
					 List<Pair<Integer, Integer>> usableCases) {
		super(name, point,symbol);

		this.full_coef = coef;

		this.win_condition = limit;
		this.boardView = new HashMap<Pair<Integer,Integer>,Float>(); //Init with function setBotBoard
		setBotBoard(usableCases);
		computeAllValues();
	}

	/**
	 * <h3>setBotBoard</h3>
	 * This procedure just initialize the Board and is just called by the constructor BotPlayer
	 * @param usableCases the playable cases
	 */
	public void setBotBoard(List<Pair<Integer, Integer>> usableCases){
		for(Pair<Integer,Integer> p : usableCases){
			this.boardView.put(p,1.0f); //Set all cases to 1
		}
	}

	/**
	 * <h3>getLevel</h3>
	 * @return the coefficient used for the Bot
	 */
	public float getLevel(){
		return this.full_coef;
	}

	/**
	 * <h3>setLevel</h3>
	 * change the coefficient of the bot
	 * @param coef the new coefficient of the bot
	 */
	public void setLevel(float coef){
		this.full_coef = coef;
	}

	/**
	 * <h3>private verticalValueOfCase</h3>
	 * This function will ONLY calculate the vertical value of the chosen position in a float format
	 * This value does not represent the entire value of the position.
	 * At "worst" the value will be at 0.0 (closed line)
	 *
	 * @param position the position that will be tested
	 * @return the vertical value of the position
	 */
	private float verticalValueOfCase(@NotNull Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = ((position.getValue()-this.win_condition)+1); i < position.getValue()+1; i++){
			int nb_bot_symbol = 0; //number of time when the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time when the symbol of the enemy is present

			Pair<Integer, Integer> neighbour = new Pair<>(position.getKey(), i);

			if(this.boardView.containsKey(neighbour)){ //If the case is present
				//System.out.println("Case"+neighbour.getKey()+","+neighbour.getValue());
				for(int j = 0; j < this.win_condition; j++){
					Pair<Integer, Integer> testCase = new Pair<>(neighbour.getKey(),i+j);

					if(this.boardView.containsKey(testCase)){
						if(this.boardView.get(testCase) == -1.0f){ //If the case has a symbol from the adverser
							nb_adverse_symbol += 1;
						}
						if(this.boardView.get(testCase) == 0.0f){ //If the case has a symbol from the bot
							nb_bot_symbol += 1;
						}
					}
					else{
						nb_adverse_symbol += 1;
						nb_bot_symbol += 1;
					}
				}
				if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
					value = value;
				}
				else{
					if(nb_bot_symbol !=0){
						System.out.println("Vertical symbol bot:"+nb_bot_symbol);
						float add = 3.0f;

						for(int compt = 1; compt<nb_bot_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else if(nb_adverse_symbol !=0){
						float add = 2.0f;

						for(int compt = 1; compt<nb_adverse_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else{// empty line
						value += 1.0f;
					}
				}

				
			}
		}


		return value;
	}

	/**
	 * <h3>private horizontalValueOfCase</h3>
	 * This function will ONLY calculate the horizontal value of the chosen position in a float format
	 * This value does not represent the entire value of the position.
	 * At "worst" the value will be at 0.0 (closed line)
	 *
	 * @param position the position that will be tested
	 * @return the horizontal value of the position
	 */
	private float horizontalValueOfCase(@NotNull Pair<Integer, Integer> position){
		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = (position.getKey()-(this.win_condition+1)); i < position.getKey()+1; i++){
			int nb_bot_symbol = 0; //number of time when the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time when the symbol of the enemy is present

			Pair<Integer, Integer> neighbour = new Pair<>(i, position.getValue());
			if(this.boardView.containsKey(neighbour)) { //If the case is present

				for(int j = 0; j < this.win_condition; j++){

					Pair<Integer, Integer> testCase = new Pair<>(i+j, position.getValue());

					if(this.boardView.containsKey(testCase)){
						if(this.boardView.get(testCase) == -1.0f){ //If the case has a symbol from the adverser
							nb_adverse_symbol += 1;
						}
						if(this.boardView.get(testCase) == 0.0f){ //If the case has a symbol from the bot
							nb_bot_symbol += 1;
						}
					}
					else{
						nb_adverse_symbol += 1;
						nb_bot_symbol += 1;
					}
				}

				if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
					//System.out.println("Horizontal : Je suis fermé ou trop petit");
					value = value;
				}
				else{
					if(nb_bot_symbol !=0){

						float add = 3.0f;
						for(int compt = 1; compt<nb_bot_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else if(nb_adverse_symbol !=0){
						float add = 2.0f;
						for(int compt = 1; compt<nb_adverse_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else{ // empty line
						value +=1.0f;
					}
				}

			}


		}



		return value;
	}


	/**
	 * <h3>private diagonalDownToUpValueOfCase</h3>
	 * This function will ONLY calculate half of the diagonal value of the chosen position in a float format
	 * This value does not represent the entire value of the position.
	 * At "worst" the value will be at 0.0 (closed line)
	 *
	 * @param position the position that will be tested
	 * @return the first half of the diagonal value of the position
	 */
	private float diagonalDownToUpValueOfCase(Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = 0 ; i< (this.win_condition)-1; i++){

			int nb_bot_symbol = 0; //number of time when the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time when the symbol of the enemy is present

			/**
			 * How it is calculated :
			 * Sence of the reading : The diagonal is from the left down to the right up
			 * Left to right: 0 = left, i = right
			 * 		x = [initial position.x - (winning condition-1)] + variation i
			 * Down to up: 0 = up, i = down
			 * 		y = [initial position.y + (winning condition-1)] - variation i
			 */

			Pair<Integer, Integer> neighbour = new Pair<>(
					((position.getKey()-this.win_condition)+1)+i,
					((position.getValue()+this.win_condition)-1)-i
			);

			if(this.boardView.containsKey(neighbour)){

				for(int j = 0; j <= this.win_condition; j++){

					Pair<Integer, Integer> testCase = new Pair<>(neighbour.getKey()+j, neighbour.getValue()-j);

					if(this.boardView.containsKey(testCase)){
						if(this.boardView.get(testCase) == -1.0f){ //If the case has a symbol from the adverser
							nb_adverse_symbol += 1;
						}
						if(this.boardView.get(testCase) == 0.0f){ //If the case has a symbol from the bot
							nb_bot_symbol += 1;
						}
					}
					else{
						nb_adverse_symbol += 1;
						nb_bot_symbol += 1;
					}

				}

				if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
					value = value;
				}
				else{
					if(nb_bot_symbol !=0){

						float add = 3.0f;
						for(int compt = 1; compt<nb_bot_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else if(nb_adverse_symbol != 0){
						float add = 2.0f;
						for(int compt = 1; compt<nb_adverse_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else{ //empty line
						value +=1.0f;
					}
				}


			}

		}


		return value;
	}



	/**
	 * <h3>private diagonalUpToDownValueOfCase</h3>
	 * This function will ONLY calculate half of the diagonal value of the chosen position in a float format
	 * This value does not represent the entire value of the position.
	 * At "worst" the value will be at 0.0 (closed line)
	 *
	 * @param position the position that will be tested
	 * @return the second half of the diagonal value of the position
	 */

	private float diagonalUpToDownValueOfCase(Pair<Integer, Integer> position){

		float value = 0.0f; //cannot be 1.0f for calculing the value of the case

		//Check the possibilities before and after the chosen point
		for(int i = 0 ; i< this.win_condition; i++){

			int nb_bot_symbol = 0; //number of time when the symbol of the bot is present
			int nb_adverse_symbol = 0; //number of time when the symbol of the enemy is present

			/**
			 * How it is calculated :
			 * Sence of the reading : The diagonal is from the left up to the right down
			 * Left to right: 0 = left, i = right
			 * 		x = [initial position.x - (winning condition-1)] + variation i
			 * Up to down: 0 = up, i = down
			 * 		y = [initial position.y - (winning condition-1)] + variation i
			 */

			Pair<Integer, Integer> neighbour = new Pair<>(
					((position.getKey()-this.win_condition)+1)+i,
					((position.getValue()-this.win_condition)+1)+i
			);

			//System.out.println("position: "+position+" neighbour "+neighbour);

			if(this.boardView.containsKey(neighbour)){

				for(int j = 0; j < this.win_condition; j++){

					Pair<Integer, Integer> testCase = new Pair<>(neighbour.getKey()+j, neighbour.getValue()+j);
					//System.out.println("position :"+(neighbour.getKey()+j)+","+(neighbour.getValue()+j));

					if(this.boardView.containsKey(testCase)){
						if(this.boardView.get(testCase) == -1.0f){ //If the case has a symbol from the adverser
							nb_adverse_symbol += 1;
						}
						if(this.boardView.get(testCase) == 0.0f){ //If the case has a symbol from the bot
							nb_bot_symbol += 1;
						}
					}
					else{
						nb_adverse_symbol += 1;
						nb_bot_symbol += 1;
					}
				}

				if(nb_bot_symbol != 0 && nb_adverse_symbol !=0){ //if closed add nothing to value and check the next posibility
					value = value;
					//System.out.println("Diagonal ligne fermé "+value);
				}
				else{
					if(nb_bot_symbol !=0){
						//System.out.println("Diagonal symbol bot:"+nb_bot_symbol);
						float add = 3.0f;
						for(int compt = 1; compt<nb_bot_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else if(nb_adverse_symbol != 0){
						//System.out.println("Diagonal symbol player:"+nb_adverse_symbol);
						float add = 2.0f;
						for(int compt = 1; compt<nb_adverse_symbol; compt++){
							add = add * this.full_coef;
						}

						value = value + add;
					}
					else{
						//System.out.println("Ligne vide");
						value +=1.0f;
					}
				}


			}

		}

		return value;
	}


	/**
	 * <h3>totalValueofCase</h3>
	 *
	 * This function will calculate the full value of the chosen position in a float format
	 * using the four functions above.
	 *
	 * @param position the position that will be tested
	 * @return the complete value of the position
	 */

	public float totalValueofCase(Pair<Integer, Integer>position){
		float value = horizontalValueOfCase(position)+
						verticalValueOfCase(position)+
						diagonalUpToDownValueOfCase(position)+
						diagonalDownToUpValueOfCase(position);

		if(value == 0.0f){ //mostly for the start of the game
			value = 1.0f;
		}

		return value;
	}

	/**
	 * <h3>computeAllValues</h3>
	 *
	 * Calculate the value of every case on the board.
	 * Warning: will destroy the old values of board that represent the placed symbols, use this function only at the start.
	 */
	public void computeAllValues(){

		for(Pair<Integer,Integer>coordinates : this.boardView.keySet()){ //go through every positions
			this.boardView.replace(coordinates,totalValueofCase(coordinates)); //put new value
		}
	}

	/**
	 * <h3>recomputeNeighbour</h3>
	 *
	 * Recalculate the value of every case impacted by the modified case but not the modified case itself.
	 *
	 * @param position case that got modify
	 */
	public void recomputeNeighbour(@NotNull Pair<Integer, Integer> position){

		//Modify horizontal neighbours
		for(int i = (position.getKey()-(this.win_condition-1)); i<position.getKey()+this.win_condition;i++){
			Pair<Integer, Integer> key = new Pair<>(i, position.getValue());
			if(this.boardView.containsKey(key) &&
					!key.equals(position) && (
							boardView.get(key) != 0.0f
							&& boardView.get(key) != -1.0f
			)){//If we are on the board and are not the origin and don't have a symbol on them
				this.boardView.replace(key,totalValueofCase(key));
			}
		}

		//Modify vertical neighbours
		for(int i = (position.getValue()-(this.win_condition-1)); i<position.getValue()+this.win_condition;i++){
			Pair<Integer, Integer> key = new Pair<>(position.getKey(),i);
			if(this.boardView.containsKey(key) &&
					!key.equals(position) &&(
					boardView.get(key) != 0.0f && boardView.get(key) != -1.0f
			)){//If we are on the board and are not the origin
				this.boardView.replace(key,totalValueofCase(key));
			}
		}
		//Every diagonal start from the left

		//Add first diagonal neighbours (DownToUp)
		for(int i = 0; i<(this.win_condition*2);i++){
			Pair<Integer, Integer> key = new Pair<>(
					(position.getKey()-(this.win_condition-1))+i,
					(position.getValue()+(this.win_condition-1))-i
			);
			if(this.boardView.containsKey(key)
				&& !key.equals(position) &&(
					boardView.get(key) != 0.0f && boardView.get(key) != -1.0f
				)
			){//If we are on the board and not the origin
				this.boardView.replace(key,totalValueofCase(key));
			}
		}

		//Add second diagonal neighbours (UpToDown)
		for(int i = 0; i<(this.win_condition*2);i++){
			Pair<Integer, Integer> key = new Pair<>(
					(position.getKey()-(this.win_condition-1))+i,
					(position.getValue()-(this.win_condition-1))+i
			);
			if(this.boardView.containsKey(key) &&
					!key.equals(position) &&(
					boardView.get(key) != 0.0f && boardView.get(key) != -1.0f
				)
			){//If we are on the board and not the origin
				this.boardView.replace(key,totalValueofCase(key));
			}
		}

	}


	/**
	 * <h3>symbolPutByBot</h3>
	 *
	 * Change the value to the case to 0.0 (aka the bot symbol)
	 *
	 * @param position case that get modify
	 */

	public void symbolPutByBot(Pair<Integer,Integer> position){
		Float old = this.boardView.get(position);
		this.boardView.replace(position,old,0.0f);
		recomputeNeighbour(position);
	}

	/**
	 * <h3>symbolPutByPlayer</h3>
	 *
	 * Change the value to the case to -1.0 (aka the adverser symbol)
	 *
	 * @param position case that get modify
	 */

	public void symbolPutByPlayer(Pair<Integer, Integer> position){
		this.boardView.replace(position,-1.0f);
		recomputeNeighbour(position);
	}

	/**
	 * <h3>resetValueCase</h3>
	 *
	 * Change the value to the case to 1.0 (aka the adverser symbol)
	 * Warning: does not recompute the value of the case yet
	 *
	 * @param position case that get modify
	 */
	public void resetValueCase(Pair<Integer,Integer> position){
		this.boardView.replace(position,1.0f);
		recomputeNeighbour(position);
	}

	/**
	 * <h3>getMaxValue</h3>
	 * @return the position were the bot will play
	 * Warning: if there is more than one case with the max value, the function will return the first case it had crossed
	 */

	public Pair<Integer, Integer> getMaxValue(){
		Pair<Integer, Integer> maxVal = new Pair<>(-1,-1);
		float max = 0.0f;

		for(Pair<Integer,Integer>key : this.boardView.keySet()){
			if(max < this.boardView.get(key)){
				max = this.boardView.get(key);
				maxVal = key;
			}
		}

		return maxVal;
   }
    public static float getCurrentDefaultLevel() {
        return currentDefaultLevel;
    }

    /**
     * Change le niveau global de difficulté de l'IA.
     * Les prochains BotPlayer créés utiliseront ce level.
     * @param difficultyKey "EASY", "NORMAL" ou "HARD"
     */
    public static void changeLevel(String difficultyKey) {
        switch (difficultyKey) {
            case "EASY":
                currentDefaultLevel = EASY_LEVEL;
                break;
            case "HARD":
                currentDefaultLevel = HARD_LEVEL;
                break;
            default:
                currentDefaultLevel = NORMAL_LEVEL;
                break;
        }
        System.out.println("[BotPlayer] Difficulty set to " + difficultyKey +
                " (coef=" + currentDefaultLevel + ")");
    }

}