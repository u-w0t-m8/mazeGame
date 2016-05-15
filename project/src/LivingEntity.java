
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

    // really need to solve smooth movement
    protected float velx;
    protected float vely;
    private int counter = 0;

    public abstract void think(Grid grid);
    
    @Override
    public void update(Grid grid) {
    	
    	if(counter >0){ //if counting already
    		if(counter == 25){
    			velx = 0;
    			vely = 0;
    			if(velx != 0){
    				if((int)(posx+0.1) != (int)posx){
        				posx = (int) Math.ceil(posx);
        			}
        			else {
        				posx = (int) Math.floor(posx);
        			}
    			}
    			else {
    				if((int)(posy+0.1) != (int)posy){
        				posy = (int) Math.ceil(posy);
        			}
        			else {
        				posy = (int) Math.floor(posy);
        			}
    			}
    			
    			counter = 0;
    		}
    		else {
    			counter++;
    			posx += velx/25;
	    		posy += vely/25;
    		}
    	}
    	else { //Else wait
    		think(grid);
    		if(velx != 0 || vely != 0){
    			if(velx != 0 && vely !=0 ){
    				vely = 0;
    			}
    			if(!grid.getTile((int)(posx+velx+0.1), (int)(posy+vely+0.1)).getIsWall()){
    	    		posx += velx/25;
    	    		posy += vely/25;
    	    		counter++;
    	    	}
    		}
    	}

    }
    
    protected void setVelX(float vx){
    	//System.out.println("Velocity x:" + vx);
        velx = vx;
    }
    
    protected void setVelY(float vy){
    	//System.out.println("Velocity y:" + vy);
        vely = vy;
    }
    
    public int getVelx(){
    	return (int)velx;
    }
    
    public int getVely(){
    	return (int)vely;
    }

}
