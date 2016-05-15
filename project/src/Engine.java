import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

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
    private static final int DEFAULT_XRES = 800;
    private static final int DEFAULT_YRES = 500;

    private Menu mMenu;
    private EndState mEndState;
    private Grid currentGrid = null;
    private Renderer mRenderer;
    private JFrame mFrame;
    private Canvas mCanvas;
    // private Input mInput;

    private boolean isRunning = false;
    private GameState state = GameState.MAIN_MENU;
    

    public Engine() {
        mMenu = new Menu();
        mEndState = new EndState();
        mFrame = new JFrame();
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mCanvas = new Canvas();
        mCanvas.setPreferredSize(new Dimension(DEFAULT_XRES, DEFAULT_YRES));
        mFrame.add(mCanvas);
        mFrame.pack();
        mCanvas.createBufferStrategy(2);
        mRenderer = new Renderer(mCanvas);
        mFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = mFrame.getContentPane().getWidth();
                int h = mFrame.getContentPane().getHeight();
                mCanvas.setSize(w, h);
                mRenderer.setResolution(w, h);
            }
        });
        // STUFF RELATED TO INPUT
        mCanvas.addKeyListener(inputListener);
        mCanvas.setFocusable(true);
        // c.getFocusListeners();
    }

    public void startEngine() {
        // TODO: set windows to visible
        isRunning = true;
        loopThread.start();
        mFrame.setVisible(true);
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
                   //if(currentGrid.getGameEnd()){
                   //   isRunning = false;
                   //}
                    	update();
                    	render();
                    //}
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
        switch (state) {
        case MAIN_MENU: {
            break;
        }
        case IN_GAME: {
            currentGrid.update();
            if(currentGrid.getGameEnd()){
            	endLevel();
            }
            break;
        }
        case GAME_OVER: {
        	break;
        }
        }
    }

    /**
     * As above, render the state of the game. It is up to Engine to decide what
     * has to be drawn (i.e. which menus, whether to draw maze etc).
     */
    private void render() {
        do {
            mRenderer.startFrame();
            switch (state) {
            case MAIN_MENU:
                mRenderer.drawMenu(mMenu);
                break;
            case IN_GAME:
                mRenderer.drawGrid(currentGrid);
                break;
            case GAME_OVER: 
            	mRenderer.drawEndState(mEndState);
            	break;
            }
        } while (!mRenderer.finishFrame());
        // if finishFrame() fails the render has to restart
    }

    void startNewLevel(Difficulty diff) {
        currentGrid = new Grid(diff);
        mRenderer.createPreRender(currentGrid);
        state = GameState.IN_GAME;
    }

    void endLevel() {
        currentGrid = null;
        mRenderer.destroyPreRender();
        state = GameState.GAME_OVER;
    }

    private KeyAdapter inputListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            switch (state) {
            case IN_GAME: {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    currentGrid.updatePlayerInput(1, 0, 0, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    currentGrid.updatePlayerInput(0, 1, 0 ,0);
                    break;
                case KeyEvent.VK_LEFT:
                    currentGrid.updatePlayerInput(0, 0, 1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    currentGrid.updatePlayerInput(0, 0, 0, 1);
                }
                break;
            }
            case MAIN_MENU: {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    mMenu.up();
                    break;
                case KeyEvent.VK_DOWN:
                    mMenu.down();
                    break;
                case KeyEvent.VK_LEFT:
                	mMenu.left();
                	break;
                case KeyEvent.VK_RIGHT:
                	mMenu.right();
                	break;
                case KeyEvent.VK_ENTER:
                    Difficulty diff = Difficulty.values()[mMenu.getDifficulty()];
                    startNewLevel(diff);
                    // if mMenu.getSelected == 1 
                    // display instructions page
                    if(mMenu.getSelected() == 2){
                    	System.exit(0);
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                }
                break;
            }
            case GAME_OVER:{
            	switch(e.getKeyCode()){
            	case KeyEvent.VK_UP:
                    mEndState.up();
                    break;
                case KeyEvent.VK_DOWN:
                    mEndState.down();
                    break;
                case KeyEvent.VK_ENTER:
                	if(mEndState.getSelected() == 0){
                		state = GameState.MAIN_MENU;
                	}else{
                		System.exit(0);
                	}
                	break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                }
            	}
            }
            }

        @Override
        public void keyReleased(KeyEvent e) {
            if (state == GameState.IN_GAME) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    currentGrid.updatePlayerInput(-1, 0, 0, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    currentGrid.updatePlayerInput(0, -1, 0, 0);
                    break;
                case KeyEvent.VK_LEFT:
                    currentGrid.updatePlayerInput(0, 0, -1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    currentGrid.updatePlayerInput(0, 0, 0, -1);
                }
            }
        }
    };

}
