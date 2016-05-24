import java.awt.Rectangle;

public class Menu{
	
	private int selected;
	private int difficulty;
	private Rectangle[] mouseBoxes = new Rectangle[5];

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
	 * 1 = Instructions
	 * 2 = quit
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
	
	public void setMouseArea(int index, int x1, int y1, int x2, int y2){
		mouseBoxes[index].setFrameFromDiagonal(x1, y1, x2, y2);
	}

	public int getItemAtScreenPosition(int x, int y){
	    for (int i = 0; i < mouseBoxes.length; i++){
	    	if (mouseBoxes[i].contains(x, y)){
	    		return i;
	    	}
	    }
	    return selected;
	}
	    
	public void left() {
		if(selected == 0){
				difficulty--;

			if(difficulty == -1)
				difficulty = 3;
		}
	}

	public void right() {
		if(selected == 0){
				difficulty++;
				if(difficulty == 4)
					difficulty = 0;
		}
	} 
	
	public int getDifficulty(){
		return difficulty;
	}
	
}
