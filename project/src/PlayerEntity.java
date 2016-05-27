/**
 * Special implementation of LivingEntity whose only thinking is to respond to
 * user input. Also unique in that Grid contains a single instance with a direct
 * reference.
 * 
 */
public class PlayerEntity extends LivingEntity {

    private int keyUp = 0;
    private int keyDown = 0;
    private int keyLeft = 0;
    private int keyRight = 0;

    public PlayerEntity(int num) {
        if(num == 1){
            setSprite("player1");

        }
        else {
            setSprite("player2");
        }
    }

    /**
     * Updates entity of changes to keyboard input if any. Key fields should be
     * 0 for no change, a negative number for released or a positive number for
     * pressed.
     * 
     * @param up state of up key
     * @param down state of down key
     * @param left state of left key
     * @param right state of right key
     */
    public void updateInputs(int up, int down, int left, int right) {
        if (up != 0)
            keyUp = (up > 0) ? 1 : 0;
        if (down != 0)
            keyDown = (down > 0) ? 1 : 0;
        if (left != 0)
            keyLeft = (left > 0) ? 1 : 0;
        if (right != 0)
            keyRight = (right > 0) ? 1 : 0;
    }

    @Override
    public void think(Grid grid) {
        setVelX(keyRight - keyLeft);
        setVelY(keyDown - keyUp);
    }

}
