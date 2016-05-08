
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
    public float prevX = 0;
    public float prevY = 0;
    
    private double intermediateX;
    private double intermediateY;
    
    int intermediateCount = 0;

    public abstract void think(Grid grid);
    
    @Override
    public void update(Grid grid) {
    	
    	think(grid);
    	
    	if(intermediateCount == 0){
    		intermediateX = posx;
    		intermediateY = posy;
    	}
    	else if(prevX != velx || prevY != vely){
    		intermediateCount = 0;
    	}
    	else if(intermediateCount == 75){
    		intermediateCount = 0;
    		posx += velx;
    		posy += vely;
    	}
    	intermediateX += velx/75;
    	intermediateY += vely/75;
    	
    }
    
    protected void setVelX(float vx){
        velx = vx;
    }
    
    protected void setVelY(float vy){
        vely = vy;
    }

}
