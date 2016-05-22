import java.util.Random;

public class RandomPattern implements Patterns {
	
	private int goalX;
	private int goalY;

	public RandomPattern(Grid grid){
		if(grid.getPlayer().getVelx() == 1){
			goalX = (int) (grid.getSizeX()-3);
			goalY = (int) (grid.getPlayerY()+0.1);
		}
		else if(grid.getPlayer().getVelx() == -1){
			goalX = 2;
			goalY = (int) (grid.getPlayerY()+0.1);
		}
		else if(grid.getPlayer().getVely() == 1){
			goalX = (int) (grid.getPlayerX()+0.1);
			goalY = (int) (grid.getSizeY()-3);
		}
		else if(grid.getPlayer().getVely() == -1){
			goalX = (int) (grid.getPlayerX()+0.1);
			goalY = 2;
		}
		else {
			goalX = (int) (grid.getPlayerX()+0.1);
			goalY = (int) (grid.getPlayerY()+0.1);
		}
	}
	
	@Override
	public int getX() {
		return goalX;
	}

	@Override
	public int getY() {
		return goalY;
	}
	
}
