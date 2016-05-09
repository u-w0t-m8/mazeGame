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
        int playerX = grid.getPlayerX();
        int playerY = grid.getPlayerY();
        
        PriorityQueue<State> queue = new PriorityQueue<State>(1, new StateComparator());
        queue.add(new State(-1, 0, posx, posy, playerX, playerY, null));
        
        while(queue.size() > 0){
        	State nextState = queue.poll();
        	
        	if(nextState.isGoal()){
        		while(nextState.getPreviousState().getPreviousState() != null){
        			nextState = nextState.getPreviousState();
        		}
        		velx = x[nextState.getMove()];
        		vely = y[nextState.getMove()];
        		break;
        	}
        	else {
        		for(int i = 0; i < 4; ++i){
        			if(nextState.getX() + x[i] < 49 && nextState.getX() + x[i] > 0 &&
        					nextState.getY() + y[i] < 49 && nextState.getY() + y[i] > 0 &&
        					!grid.getTile(nextState.getX() + x[i],  nextState.getY() + y[i]).getIsWall()){ // Check if it is not a wall
        				queue.add(new State(i, nextState.getDistanceTraveled()+1, 
        						nextState.getX() + x[i], nextState.getY() + y[i], playerX, playerY, nextState));

        			}
        		}
        	}
        }
    }
}
