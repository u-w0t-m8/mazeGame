import java.awt.Rectangle;

public class EndState {
	private int selected;
	private Rectangle[] mouseBoxes = new Rectangle[2];
	
	public EndState(){
		selected = 0;
		for (int i = 0; i < mouseBoxes.length; i++){
			mouseBoxes[i] = new Rectangle(-1, -1, 0, 0);
		}
	}
	
	public int getSelected(){
		return selected;
	}
	
	/**
	 * Change the selected value if key pressed is up
	 */
	public void up(){
		if ( (selected > 0)  && (selected <= 1)){
			selected--;
		}
	}
	
	/**
	 * Change the selected value if key pressed is down
	 */
	public void down(){
		if ( (selected >= 0)  && (selected < 1)){
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
}
