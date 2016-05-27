import java.awt.Rectangle;

public class Menu{
	
	private int selected;
	private int difficulty;
	private Rectangle[] mouseBoxes = new Rectangle[6];

	/**
	 * Constructor for menu
	 * set the selected difficulty to whatever input has passed in.
	 */
	public Menu() {	
		selected = 0;
		difficulty = 0;
		for (int i = 0; i < mouseBoxes.length; i++){
			mouseBoxes[i] = new Rectangle(-1, -1, 0, 0);
		}
	}
	
	/**
	 * Get the value of selected 
	 * 0 = Start
	 * 1 = multiplayer
	 * 2 = Instructions
	 * 3 = quit
	 * @return int
	 */
	public int getSelected() {
		return selected;
	}

	/**
	 * Change the selected value if key pressed is up
	 */
	public void up(){
		if ( (selected > 0)  && (selected <= 3)){
			selected--;
		}
	}
	
	/**
	 * Change the selected value if key pressed is down
	 */
	public void down(){
		if ( (selected >= 0)  && (selected < 3)){
			selected++;
		}
	}

	/**
	 * Sets currently selected box
	 * @param selected 
	 */
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	/**Sets box with rectangle coordinates
	 * 
	 * @param index index in array
	 * @param x1 starting x
	 * @param y1 starting y
	 * @param x2 width
	 * @param y2 height
	 */
	public void setMouseArea(int index, int x1, int y1, int x2, int y2){
		mouseBoxes[index].setFrameFromDiagonal(x1, y1, x2, y2);
	}

	/**Gets current box selected from mouse coordinates
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 * @return selected box
	 */
	public int getItemAtScreenPosition(int x, int y){
	    for (int i = 0; i < mouseBoxes.length; i++){
	    	if (mouseBoxes[i].contains(x, y)){
	    		return i;
	    	}
	    }
	    return -1;
	}
	
	/**
	 * Decreases difficulty or loops to Hard 
	 */
	public void left() {
		if(selected == 0){
				difficulty--;

			if(difficulty == -1)
				difficulty = 2;
		}
	}

	/**
	 * increase difficulty or loops to easy
	 */
	public void right() {
		if(selected == 0){
				difficulty++;
				if(difficulty == 3)
					difficulty = 0;
		}
	} 
	
	/**
	 * gets current difficulty
	 * @return difficulty
	 */
	public int getDifficulty(){
		return difficulty;
	}
	
}
