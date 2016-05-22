/**
 * This pattern returns a position that is five steps head of the direction
 * the player is heading towards using the current location of the player and the 
 * velocity vector of the player.
 */
public class PinPattern implements Patterns {
	
	private int goalX; //x coordinate of the goal of the A* search
	private int goalY; //y coordinate of the goal of the A* search
	
	public PinPattern(Grid grid){
		for(int i = 0; i < 5; ++i){
			goalX = (int) (grid.getPlayer().getVelx()*5 + grid.getPlayerX()+0.1 - i);
			goalY = (int) (grid.getPlayer().getVely()*5 + grid.getPlayerY()+0.1 - i);
			if(goalX < grid.getSizeX()-2 && goalX > 1 && goalY < grid.getSizeY()-2 && goalY > 1){
				break;
			}
		}
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
