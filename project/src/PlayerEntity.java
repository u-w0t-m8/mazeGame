
/**
 * Special implementation of LivingEntity whose only thinking is to respond to
 * user input. Also unique in that Grid contains a single instance with a direct
 * reference.
 * 
 */
public class PlayerEntity extends LivingEntity {
	
	int points = 0;
	
	public PlayerEntity(){

	    setSprite("player");

	}
    
    @Override
    public void think(Grid grid) {

    }


}
