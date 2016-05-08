
public class State {
	
	private State previousState;
	private int currX;
	private int currY;
	private int playerX;
	private int playerY;
	private int distanceTraveled;
	private int heuristic;
	
	public State(int distanceTraveled, int currX, int currY, int playerX, int playerY, State previousState){
		this.distanceTraveled = distanceTraveled;
		this.currX = currX;
		this.currY = currY;
		this.playerX = playerX;
		this.playerY = playerY;
		this.previousState = previousState;
		
		setHeuristic();
		
	}
	
	public int getCost(){
		return distanceTraveled + heuristic;
	}
	
	public boolean isGoal(){
		return false;
	}
	
	public State getPreviousState(){
		return previousState;
	}
	
	public void setHeuristic(){
		heuristic = 0;
		
		int diffx = playerX - currX;
		int diffy = playerY - currY;
		
		int manDist = Math.abs(diffx) + Math.abs(diffy);
		
		heuristic = manDist * manDist;
		
	}
	
}
