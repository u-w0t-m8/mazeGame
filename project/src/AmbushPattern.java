/**
 * This pattern returns a position that is the edge of the map where the player is headed towards
 * as the goal position of the A* search to ambush the player
 */

public class AmbushPattern implements Patterns {
	
	private int goalX; //x coordinate of the goal of the A* search
	private int goalY; //y coordinate of the goal of the A* search

	public AmbushPattern(Grid grid, PlayerEntity player){
		if(grid.getPlayer().getVelx() == 1){
			goalX = (int) (grid.getSizeX()-3);
			goalY = (int) (player.getY()+0.1);
		}
		else if(grid.getPlayer().getVelx() == -1){
			goalX = 2;
			goalY = (int) (player.getY()+0.1);
		}
		else if(grid.getPlayer().getVely() == 1){
			goalX = (int) (player.getX()+0.1);
			goalY = (int) (grid.getSizeY()-3);
		}
		else if(grid.getPlayer().getVely() == -1){
			goalX = (int) (player.getX()+0.1);
			goalY = 2;
		}
		else {
			goalX = (int) (player.getX()+0.1);
			goalY = (int) (player.getY()+0.1);
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
