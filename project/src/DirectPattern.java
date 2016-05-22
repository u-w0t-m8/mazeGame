/**
 * This pattern returns a position of the player's coordinates as the goal state of the A* search
 * to make the hunter move directly towards the player
 */
public class DirectPattern implements Patterns {
	
	private int goalX; //x coordinate of the goal of the A* search
	private int goalY; //y coordinate of the goal of the A* search
	
	public DirectPattern(Grid grid){
		goalX = (int) (grid.getPlayerX()+0.1);
		goalY = (int) (grid.getPlayerY()+0.1);
	}

	/**
	 * @return - x coordinate of the goal of the A* search
	 */
	@Override
	public int getX() {
		return goalX;
	}

	/**
	 * @return - y coordinate of the goal of the A* search
	 */
	@Override
	public int getY() {
		return goalY;
	}

}
