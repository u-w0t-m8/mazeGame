import java.util.PriorityQueue;

/**
 * Placeholder hostile entity. Could probably use a cooler name.
 * 
 */
public class HunterEntity extends LivingEntity {

	int[] x = {0, 1, 0, -1};
	int[] y = {1, 0, -1, 0};
	
	/**
	 * Performs A* search with straight line distance squared
	 * @param grid - grid of the map
	 */
    @Override
    public void think(Grid grid) {
        int playerX = grid.getPlayerX();
        int playerY = grid.getPlayerY();
        
        State state = new State(-1, 0, posx, posy, playerX, playerY, null);
        PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());
        
        while(queue.size() > 0){
        	State nextState = queue.poll();
        	
        	if(nextState.isGoal()){
        		while(nextState.getPreviousState().getPreviousState() != null){
        			nextState = nextState.getPreviousState();
        		}
        		velx = x[nextState.getMove()];
        		break;
        	}
        	else {
        		for(int i = 0; i < 4; ++i){
        			if(grid.getTile(nextState.getX() + x[i],  nextState.getY() + y[i]).getIsWall()){ // Check if it is not a wall
        				queue.add(new State(i, nextState.getDistanceTraveled(), 
        						nextState.getX() + x[i], nextState.getY() + y[i], playerX, playerY, nextState));
        			}
        		}
        	}
        }
    }
}
