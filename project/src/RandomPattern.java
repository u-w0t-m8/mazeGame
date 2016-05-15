import java.util.Random;

public class RandomPattern implements Patterns {
	
	private int goalX;
	private int goalY;

	public RandomPattern(Grid grid){
		Random rand = new Random();
		goalX = rand.nextInt(grid.getSizeX() - 1) + 1;
        goalY = rand.nextInt(grid.getSizeY() - 1) + 1;
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
