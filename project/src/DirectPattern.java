
public class DirectPattern implements Patterns {
	
	private int goalX;
	private int goalY;
	
	public DirectPattern(Grid grid){
		goalX = (int) (grid.getPlayerX()+0.1);
		goalY = (int) (grid.getPlayerY()+0.1);
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
