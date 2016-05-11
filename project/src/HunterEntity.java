import java.util.PriorityQueue;

/**
 * Placeholder hostile entity. Could probably use a cooler name.
 * 
 */
public class HunterEntity extends LivingEntity {

	int[] x = {0, 1, 0, -1};
	int[] y = {-1, 0, 1, 0};
	
	public HunterEntity() {
		setSprite("enemy");
		// TODO Auto-generated constructor stub
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
    	
        int playerX = grid.getPlayerX();
        int playerY = grid.getPlayerY();
        
        PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());
        queue.add(new State(-1, 0, posx, posy, playerX, playerY+1, null));
        
        while(queue.size() > 0){
        	State nextState = queue.poll();
        	
        	if(!visited[nextState.getY()][nextState.getX()]){
        		visited[nextState.getY()][nextState.getX()] = true;
        		if(nextState.isGoal()){
            		
            		if(nextState.getPreviousState() == null){
            			//System.out.println("Already standing on player");
            		}
            		else {
            			while(nextState.getPreviousState().getPreviousState() != null){
                			nextState = nextState.getPreviousState();
                		}
                		velx = x[nextState.getMove()];
                		vely = y[nextState.getMove()];
            		}
            		break;
            	}
            	else {
            		for(int i = 0; i < 4; ++i){
            			if(nextState.getX() + x[i] < grid.getSizeX()-1 && nextState.getX() + x[i] > 2 &&
            					nextState.getY() + y[i] < grid.getSizeY()-1 && nextState.getY() + y[i] > 2 &&
            					!grid.getTile(nextState.getX() + x[i],  nextState.getY() + y[i]).getIsWall()){ // Check if it is not a wall
            				queue.add(new State(i, nextState.getDistanceTraveled()+1, 
            						nextState.getX() + x[i], nextState.getY() + y[i], playerX, playerY, nextState));
            				

            			}
            		}
            	}
        	}
        }
    }
}
