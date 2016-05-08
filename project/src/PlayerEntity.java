
/**
 * Special implementation of LivingEntity whose only thinking is to respond to
 * user input. Also unique in that Grid contains a single instance with a direct
 * reference.
 * 
 */
public class PlayerEntity extends LivingEntity {
	
	public PlayerEntity(){
	    setSprite("player");
	}
    
    @Override
    public void think(Grid grid) {
        // just act on user input: e.g. set velocity to match user request
    }


}
