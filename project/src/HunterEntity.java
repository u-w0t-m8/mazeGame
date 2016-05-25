import java.util.PriorityQueue;

public class HunterEntity extends LivingEntity {

	private int pattern; // Strategy pattern ID used for this hunter
	// Used for adding all four adjacent coordinates in A* search
	private int[] x = {0, 1, 0, -1};
	private int[] y = {-1, 0, 1, 0};
	
	public HunterEntity(int pattern) {
		setSprite("enemy");
		this.pattern = pattern;
	}
	
	/**
	 * Performs A* search with straight line distance squared
	 * @param grid - grid of the map
	 */
    @Override
    public void think(Grid grid) {
    	
    	boolean[][] visited = new boolean[grid.getSizeY()][grid.getSizeX()];
    	for(int i = 0; i < grid.getSizeY(); ++i){
    		for(int j = 0; j < grid.getSizeX(); ++j){
    			visited[i][j] = false;
    		}
    	}
    	PlayerEntity p = grid.getPlayer();
    	if(grid.getPlayer2() != null){
    		int diffx = Math.abs((int) (posx - grid.getPlayer().getX()));
        	int diffy = Math.abs((int) (posx - grid.getPlayer().getY()));
        	int diffx2 = Math.abs((int) (posx - grid.getPlayer2().getX()));
        	int diffy2 = Math.abs((int) (posx - grid.getPlayer2().getY()));
        	
        	if(diffx + diffy > diffx2 + diffy2){
        		p = grid.getPlayer2();
        	}
    	}
    	
    	//Select the strategy pattern corresponding to ID passed in through the constructor 
    	Patterns patt;
    	if(pattern == 0){
    		patt = new DirectPattern(grid, p);
    	}
    	else if(pattern == 1){
    		patt = new PinPattern(grid, p);
    	}
    	else {
    		patt = new AmbushPattern(grid, p);
    	}
    	
    	//Set the goal state of the A* as the coordinates returned by the Strategy Pattern
        int goalX = patt.getX();
        int goalY = patt.getY();
        
        //Initialise priority queue and add initial state
        PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());
        queue.add(new State(-1, 0, (int)(posx+0.1), (int)(posy+0.1), goalX, goalY+1, null));
        
        
        while(queue.size() > 0){
        	State nextState = queue.poll();
        	
        	//If the state has not been visited yet
        	if(!visited[nextState.getY()][nextState.getX()]){
        		visited[nextState.getY()][nextState.getX()] = true;
        		
        		if(nextState.isGoal()){
            		if(nextState.getPreviousState() == null){
            			velx = 0;
            			vely = -1;
            		}
            		else {
            			//Find the first move to the player
            			while(nextState.getPreviousState().getPreviousState() != null){
                			nextState = nextState.getPreviousState();
                		}
            			//Get adjacent coordinate
                		velx = x[nextState.getMove()];
                		vely = y[nextState.getMove()];
            		}
            		break;
            	}
        		// Else add all adjacent coordinates to the priority queue
            	else {
            		for(int i = 0; i < 4; ++i){
            			//Check if the coordinates are not out of bounds, and also is not a wall
            			if(nextState.getX() + x[i] < grid.getSizeX()-1 && nextState.getX() + x[i] > 1 &&
            					nextState.getY() + y[i] < grid.getSizeY()-1 && nextState.getY() + y[i] > 1 &&
            					!grid.getTile(nextState.getX() + x[i],  nextState.getY() + y[i]).getIsWall()){ 
            				
            				//Add state
            				queue.add(new State(i, nextState.getDistanceTraveled()+1, 
            						nextState.getX() + x[i], nextState.getY() + y[i], goalX, goalY, nextState));
            				

            			}
            		}
            	}
        	}
        }
    }
}
