
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

    //need a good scheme for smooth movement eventually
	protected int posx;
	protected int posy;

	Sprite mSprite;

	abstract public void update(Grid grid);

	Sprite getSprite() {
		return mSprite;
	}
	
	public int getX(){
		return posx;
	}
	
	public int getY(){
		return posy;
	}
}
