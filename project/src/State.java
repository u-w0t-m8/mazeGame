public class State {
	
	private State previousState; // 
	private int currX; // x coordinate of current state
	private int currY; // y coordinate of current state
	private int playerX; // x coordinate of the player
	private int playerY; // y coordinate of the player
	private int distanceTraveled; // distance traveled to reach current state
	private int heuristic;
	private int move; // Direction moved to reach current state from previous state
	
	public State(int move, int distanceTraveled, int currX, int currY, int playerX, int playerY, State previousState){
		this.distanceTraveled = distanceTraveled;
		this.currX = currX;
		this.currY = currY;
		this.playerX = playerX;
		this.playerY = playerY;
		this.previousState = previousState;
		this.move = move;
		setHeuristic();
	}
	
	/**
	 * @return - Direction moved to reach current state from previous state
	 */
	public int getMove(){
		return move;
	}
	
	/**
	 * @return - f cost of current state
	 */
	public int getCost(){
		return distanceTraveled + heuristic;
	}
	
	/**
	 * @return - distance traveled to reach current state
	 */
	public int getDistanceTraveled(){
		return distanceTraveled;
	}
	
	/**
	 * @return - x coordinate of the player
	 */
	public int getX(){
		return currX;
	}
	
	/**
	 * @return - y coordinate of the player
	 */
	public int getY(){
		return currY;
	}
	
	/**
	 * @return - whether state coordinates are the same as the player coordinates
	 */
	public boolean isGoal(){
		if(currX == playerX && currY == playerY){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * @return - previous state
	 */
	public State getPreviousState(){
		return previousState;
	}
	
	/**
	 * Sets the heuristic of the current state given its fields using Manhattan distance
	 */
	public void setHeuristic(){
		heuristic = 0;
		
		int diffx = playerX - currX;
		int diffy = playerY - currY;
		
		int manDist = Math.abs(diffx) + Math.abs(diffy);
		
		heuristic = manDist;
		
	}
	
}
