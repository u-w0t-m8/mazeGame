
public class Menu{
	
	private int selected;
	private int difficulty;

	/**
	 * Constructor for menu
	 * set the selected difficulty to whatever input has passed in.
	 */
	public Menu() {	
		selected = 0;
		difficulty = 0;
	}
	
	/**
	 * Get the value of selected 
	 * 0 = Difficulty
	 * 1 = Instructions
	 * 2 = Quit
	 * @return int
	 */
	public int getSelected() {
		return selected;
	}

	/**
	 * Change the selected value if key pressed is up
	 */
	public void up(){
		if ( (selected > 0)  && (selected <= 2)){
			selected--;
		}
	}
	
	/**
	 * Change the selected value if key pressed is down
	 */
	public void down(){
		if ( (selected >= 0)  && (selected < 2)){
			selected++;
		}
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public void left() {
		if(selected == 0){
			//if((difficulty > 0) && (difficulty <= 2)){
				difficulty--;
			//}
			if(difficulty == -1){
				difficulty = 2;
			}
		}
	}
	
	
	public void right() {
		if(selected == 0){
			//if((difficulty >= 0) && (difficulty < 2)){
				difficulty++;
			//}
			if(difficulty == 3){
				difficulty = 0;
			}
		}
	} 
	
	public int getDifficulty(){
		return difficulty;
	}
}
