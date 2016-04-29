
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

    private static final int TARGET_FRAME_RATE = 75;

    private Input mInput;
    private Grid currentGrid = null;
    private Renderer mRenderer;
    private boolean isRunning = false;

    public Engine() {
        mInput = new Input();
        mRenderer = new Renderer();
    }

    public void startEngine() {
        // TODO: set windows to visible
        isRunning = true;
        loopThread.start();
    }

    private final Thread loopThread = new Thread() {
        /*
         * There are lots of viable ways to do a regular timed loop. This one
         * checks every half-millisecond whether the time since last run
         * exceeded our target interval.
         */
        private long lastTime = System.nanoTime();
        private final long INTERVAL = (long) (Math.pow(10, 9)
                / TARGET_FRAME_RATE);
        private final int SLEEP_NANOS = (int) (Math.pow(10, 6) / 2);

        public void run() {
            while (isRunning) {
                long time = System.nanoTime();
                if (time - lastTime < INTERVAL) {
                    try {
                        Thread.sleep(0, SLEEP_NANOS);
                    } catch (InterruptedException e) {
                    }
                } else {
                    lastTime = time;
                    update();
                    render();
                }
            }
        };
    };

    /**
     * Update the game. Mainly just delegate to Grid to update. The main logic
     * is deciding what to update (e.g. if there's no running game or it's
     * paused, obviously don't update the Grid).
     */
    private void update() {
        // TODO
    }

    /**
     * As above, render the state of the game. It is up to Engine to decide what
     * has to be drawn (i.e. which menus, whether to draw maze etc).
     */
    private void render() {
        do {
            mRenderer.startFrame();
            // TODO
        } while (!mRenderer.finishFrame());
        // if finishFrame() fails the render has to restart
    }

}
