
public class Menu{
	
	private int selected;

	/**
	 * Constructor for menu
	 * set the selected difficulty to whatever input has passed in.
	 */
	public Menu() {	
		selected = 0;
	}
	
	/**
	 * Get the value of selected 
	 * 0 = easy
	 * 1 = medium
	 * 2 = hard
	 * @return int
	 */
	public int getSelected() {
		return selected;
	}

	/**
	 * Change the selected value if key pressed is up
	 */
	public void up(){
		if ( (selected >= 0)  && (selected<= 2)){
			selected++;
		}
	}
	
	/**
	 * Change the selected value if key pressed is down
	 */
	public void down(){
		if ( (selected >= 0)  && (selected<= 2)){
			selected--;
		}
	}
<<<<<<< HEAD
/*
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
*/
=======

>>>>>>> branch 'master' of git@gitlab.com:HamishT/2911-project.git
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
}
