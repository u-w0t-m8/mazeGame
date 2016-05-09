
public class State {
	
	private State previousState;
	private int currX;
	private int currY;
	private int playerX;
	private int playerY;
	private int distanceTraveled;
	private int heuristic;
	private int move;
	
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
	
	public int getMove(){
		return move;
	}
	
	public int getCost(){
		return distanceTraveled + heuristic;
	}
	
	public int getDistanceTraveled(){
		return distanceTraveled;
	}
	
	public int getX(){
		return currX;
	}
	
	public int getY(){
		return currY;
	}
	
	public boolean isGoal(){
		if(currX == playerX && currY == playerY){
			return true;
		}else {
			return false;
		}
	}
	
	public State getPreviousState(){
		return previousState;
	}
	
	public void setHeuristic(){
		heuristic = 0;
		
		
		int diffx = playerX - currX;
		int diffy = playerY - currY;
		
		int manDist = Math.abs(diffx) + Math.abs(diffy);
		
		heuristic = manDist*manDist;
		
	}
	
}
