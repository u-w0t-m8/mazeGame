
/**
 * The most basic interactive element contained in the Grid. Anything that has
 * dynamic behaviour or interacts with other entities in the grid must extend
 * this.
 * <p>
 * The simplest Entity has a position in the Grid and a Sprite for the Renderer
 * to draw, so those are the minimum fields needed.
 * 
 */
public abstract class Entity {

	protected float posx; // Current x coordinate of the entity
	protected float posy; // Current y coordinate of the entity

	private Sprite mSprite; // Sprite image used to render the entity

	abstract public void update(Grid grid);
	
	/**
	 * Sets the sprite of the entity
	 * @precondition - imgPath must point to a valid image 
	 * @postcondition - sprite will be set as the the image at the parameter location
	 * @param imgPath - path of the image
	 */
	protected void setSprite(String imgPath){
	    mSprite = new Sprite(imgPath);
	}
	
	/**
	 * @return - sprite image of the entity
	 */
	public Sprite getSprite() {
		return mSprite;
	}
	
	/**
	 * Sets the x and y coordinates of the entity
	 * @postcondition - the coordinates of the entity will be set to the parameters
	 * @param x - x coordinate of the entity to be set
	 * @param y - y coordinate of the entity to be set
	 */
	public void setPos(int x, int y){
	    posx = x;
	    posy = y;
	}
	
	/**
	 * @return - y coordinate of the entity
	 */
	public float getX(){
		return posx;
	}
	
	/**
	 * @return - x coordinate of the entity
	 */
	public float getY(){
		return posy;
	}
}
