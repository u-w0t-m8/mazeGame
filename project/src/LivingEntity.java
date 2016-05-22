
/**
 * A basic extension of Entity to allow physical movement (and maybe some
 * alive/dead mechanic if needed) with some intelligence component.
 * <p>
 * Not really certain how this should work yet. Could let subclasses 'think' my
 * manipulating a velocity vector, while we do the movement code here. Not
 * having smooth movement yet makes it tricky.
 * <p>
 * Reactive entities should extend this and plan moves in think(). In the future
 * if we have different hostile entities with similar AI, it might make sense to
 * use a common AI interface they can share. For now this can go directly in
 * think().
 *
 */
public abstract class LivingEntity extends Entity {

    protected float velx; // velocity of the player in the x direction
    protected float vely; // velocity of the player in the y direction
    private int counter = 0; // Used to count the number of intermediate states between discrete positions in the grid

    public abstract void think(Grid grid);
    
    @Override
    public void update(Grid grid) {
    	
    	if(counter >0){ // If the entity is already moving
    		if(counter == 13){ // Check if a full cycle has been made
    			velx = 0;
    			vely = 0;
    			if(velx != 0){ // If the entity is moving along the x axis
    				//Check if there is a wall in that direction
    				if((int)(posx+0.1) != (int)posx){ 
        				posx = (int) Math.ceil(posx);
        			}
        			else {
        				posx = (int) Math.floor(posx);
        			}
    			}
    			else { // If the entity is moving along the y axis
    				//Check if there is a wall in that direction
    				if((int)(posy+0.1) != (int)posy){
        				posy = (int) Math.ceil(posy);
        			}
        			else {
        				posy = (int) Math.floor(posy);
        			}
    			}
    			counter = 0; // Restart counter
    		}
    		else { // Keep moving the entity
    			counter++; // continue the counter
    			posx += velx/13;
	    		posy += vely/13;
    		}
    	}
    	else { //Else poll for a new direction
    		think(grid);
    		if(velx != 0 || vely != 0){
    			if(velx != 0 && vely !=0 ){
    				vely = 0;
    			}
    			// Check if there is a wall in the direction of movement
    			if(!grid.getTile((int)(posx+velx+0.1), (int)(posy+vely+0.1)).getIsWall()){
    	    		posx += velx/13;
    	    		posy += vely/13;
    	    		counter++; // Start counter
    	    	}
    		}
    	}

    }
    
    /**
     * Sets the velocity of player in the x direction
     * @precondition - vx must be {-1,0,1}
     * @postcondition - the players velocity will be updated to the given velocity
     * @param vx - velocity of player in the y direction
     */
    protected void setVelX(float vx){
        velx = vx;
    }
    
    /**
     * Sets the velocity of player in the y direction
     * * @precondition - vy must be {-1,0,1}
     * @postcondition - the players velocity will be updated to the given velocity
     * @param vy - velocity of player in the y direction
     */
    protected void setVelY(float vy){
        vely = vy;
    }
    
    /**
     * @return - velocity of the player in the x direction
     */
    public int getVelx(){
    	return (int)velx;
    }
    
    /**
     * @return - velocity of the player in the y direction
     */
    public int getVely(){
    	return (int)vely;
    }

}
