
public class PinPattern implements Patterns {
	
	private int goalX;
	private int goalY;
	
	public PinPattern(Grid grid){
		for(int i = 0; i < 5; ++i){
			goalX = (int) (grid.getPlayer().getVelx()*5 + grid.getPlayerX()+0.1 - i);
			goalY = (int) (grid.getPlayer().getVely()*5 + grid.getPlayerY()+0.1 - i);
			if(goalX < grid.getSizeX()-2 && goalX > 1 && goalY < grid.getSizeY()-2 && goalY > 1){
				break;
			}
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
