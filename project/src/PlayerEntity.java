
/**
 * Special implementation of LivingEntity whose only thinking is to respond to
 * user input. Also unique in that Grid contains a single instance with a direct
 * reference.
 * 
 */
public class PlayerEntity extends LivingEntity {
	
	public PlayerEntity(){
<<<<<<< HEAD
	    setSprite("player");
=======
	    setSprite("player");
>>>>>>> branch 'master' of git@gitlab.com:HamishT/2911-project.git
	}
    
    @Override
    public void think(Grid grid) {
        // just act on user input: e.g. set velocity to match user request
    }


}
