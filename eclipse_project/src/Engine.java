
/**
 * The entry point of the program. Its main responsibilities of this class are
 * to manage the current game state (i.e. mostly Grid management) and call
 * update and render at the required rate.
 * <p>
 * Its other job is to pass user input to the appropriate class (either Grid for
 * player control in-game or Menu for GUI control when not in game or paused).
 * <p>
 * See Input for discussion of how engine-input interface could work.
 * 
 */
public class Engine {

    public static void main(String[] args) {
        Engine eng = new Engine();
        eng.startEngine();
    }
    
    /*
     * Not trying any complicated class integration yet.
     */
    
    private Input mInput;
    private Grid currentGrid = null;
    private Renderer mRenderer;
    
    public void startEngine(){
        // start running thread, show windows etc.
    }

}
