
public class EndState {
	private int selected;
	
	public EndState(){
		selected = 0;
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
}
