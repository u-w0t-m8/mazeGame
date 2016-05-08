
/**
 * Special implementation of LivingEntity whose only thinking is to respond to
 * user input. Also unique in that Grid contains a single instance with a direct
 * reference.
 * 
 */
public class PlayerEntity extends LivingEntity {

    //simple -1,0,1 record of user input
    private int inputx;
	private int inputy;
	
	public PlayerEntity(){
	    setSprite("player.png");
	}
	
	public void setInputx(int inputx) {
		this.inputx = inputx;
	}

	public void setInputy(int inputy) {
		this.inputy = inputy;
	}
    
    @Override
    public void think(Grid grid) {
        // just act on user input: e.g. set velocity to match user request
    }


}
