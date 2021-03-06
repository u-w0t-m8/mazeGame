import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;

/**
 * The entry point of the program. Its main responsibilities of this class are
 * to manage the current game state (i.e. mostly Grid management) and call
 * update and render at the required rate.
 * <p>
 * Its other job is to pass user input to the appropriate class (either Grid for
 * player control in-game or Menu for GUI control when not in game or paused).
 * 
 */
public class Engine {

    public static void main(String[] args) {
        Engine eng = new Engine();
        eng.startEngine();
    }

    public static final int TARGET_FRAME_RATE = 75;
    private static final int DEFAULT_XRES = 800;
    private static final int DEFAULT_YRES = 500;

    private Menu mMenu;
    private EndState mEndState;
    private Grid currentGrid = null;
    private Renderer mRenderer;
    private JFrame mFrame;
    private Canvas mCanvas;
    // private Input mInput;
    private int coinCount;
    private int coinCount2;
    private int coinsLeft;
    private int gameEndMode;
    private boolean isMultiplayer;

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
        mCanvas.addKeyListener(keyListener);
        mCanvas.addMouseListener(mouseListener);
        mCanvas.addMouseMotionListener(mouseMotionListener);
        mCanvas.setFocusable(true);
        // c.getFocusListeners();
    }

    /**
     * Start running the game engine.
     */
    public void startEngine() {
        // TODO: set windows to visible
        isRunning = true;
        LOOPTHREAD.start();
        mFrame.setVisible(true);
    }

    /**
     * Simplified sleep loop running update and render at target frame rate.
     */
    private final Thread LOOPTHREAD = new Thread() {
        // private long lastTime = System.nanoTime();
        private final long INTERVAL = (long) Math
                .floor(1000 / TARGET_FRAME_RATE);

        public void run() {
            while (isRunning) {
                // long time = System.nanoTime();
                // System.out.println((time-lastTime)/1000000);
                // lastTime = time;
                update();
                render();
                try {
                    Thread.sleep(INTERVAL);
                } catch (InterruptedException e) {
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
        case MAIN_MENU:
            break;
        case IN_GAME:
            currentGrid.update();
            if (currentGrid.getGameEnd() >= 0) {
                endLevel();
            }
            break;
        case GAME_OVER:
            break;
        case INSTRUCTION:
            break;
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
                mRenderer.drawEndState(mEndState, isMultiplayer, gameEndMode,
                        coinCount, coinCount2, coinsLeft);
                break;
            case INSTRUCTION:
                mRenderer.drawInstructions();
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
        coinCount = currentGrid.getCoinsCollected();
        coinCount2 = currentGrid.getCoinsCollectedTwo();
        coinsLeft = currentGrid.getCoinsLeft();
        gameEndMode = currentGrid.getGameEnd();

        if (currentGrid.getIsMulti()) {
            isMultiplayer = true;
        } else {
            isMultiplayer = false;
        }

        currentGrid = null;
        mRenderer.destroyPreRender();
        state = GameState.GAME_OVER;
    }

    /**
     * Listens to key presses made by the user Depending on the state of the
     * game, it determines the functionality of the keys
     */
    private KeyAdapter keyListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            switch (state) {
            case IN_GAME: {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    currentGrid.updatePlayerInput(1, 0, 0, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    currentGrid.updatePlayerInput(0, 1, 0, 0);
                    break;
                case KeyEvent.VK_LEFT:
                    currentGrid.updatePlayerInput(0, 0, 1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    currentGrid.updatePlayerInput(0, 0, 0, 1);
                    break;
                case KeyEvent.VK_ESCAPE:
                    state = GameState.MAIN_MENU;
                    break;
                // FOR PLAYER 2
                case KeyEvent.VK_W:
                    currentGrid.updatePlayerInputTwo(1, 0, 0, 0);
                    break;
                case KeyEvent.VK_S:
                    currentGrid.updatePlayerInputTwo(0, 1, 0, 0);
                    break;
                case KeyEvent.VK_A:
                    currentGrid.updatePlayerInputTwo(0, 0, 1, 0);
                    break;
                case KeyEvent.VK_D:
                    currentGrid.updatePlayerInputTwo(0, 0, 0, 1);
                    break;
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
                    // TODO: maybe make this a separate method (see Mouse click)
                    switch (mMenu.getSelected()) {
                    case (0):
                        startNewLevel(
                                Difficulty.values()[mMenu.getDifficulty()]);
                        break;
                    case (1):
                        startNewLevel(Difficulty.MULTIPLAYER);
                        break;
                    case (2):
                        state = GameState.INSTRUCTION;
                        break;
                    case (3):
                        System.exit(0);
                        break;
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                }
                break;
            }
            case GAME_OVER: {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    mEndState.up();
                    break;
                case KeyEvent.VK_DOWN:
                    mEndState.down();
                    break;
                case KeyEvent.VK_ENTER:
                    if (mEndState.getSelected() == 0) {
                        state = GameState.MAIN_MENU;
                    } else {
                        System.exit(0);
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                }
            }
            case INSTRUCTION:
                switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    state = GameState.MAIN_MENU;
                    break;
                case KeyEvent.VK_ESCAPE:
                    state = GameState.MAIN_MENU;
                    break;
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
                    break;
                // FOR PLAYER 2
                case KeyEvent.VK_W:
                    currentGrid.updatePlayerInputTwo(-1, 0, 0, 0);
                    break;
                case KeyEvent.VK_S:
                    currentGrid.updatePlayerInputTwo(0, -1, 0, 0);
                    break;
                case KeyEvent.VK_A:
                    currentGrid.updatePlayerInputTwo(0, 0, -1, 0);
                    break;
                case KeyEvent.VK_D:
                    currentGrid.updatePlayerInputTwo(0, 0, 0, -1);
                    break;
                }
            }
        }
    };

    /**
     * Performs similar function to keyListener but for mouse events.
     */
    private MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent event) {

            switch (state) {
            case MAIN_MENU:
                switch (mMenu.getSelected()) {
                case (0):
                    int index = mMenu.getItemAtScreenPosition(event.getX(),
                            event.getY());
                    switch (index) {
                    case (0):
                        mMenu.left();
                        break;
                    case (1):
                        startNewLevel(
                                Difficulty.values()[mMenu.getDifficulty()]);
                        break;
                    case (2):
                        mMenu.right();
                        break;
                    }
                    break;
                case (1):
                    startNewLevel(Difficulty.MULTIPLAYER);
                    break;
                case (2):
                    state = GameState.INSTRUCTION;
                    break;
                case (3):
                    System.exit(0);
                    break;
                }
                break;
            case GAME_OVER:
                switch (mEndState.getSelected()) {
                case (0):
                    state = GameState.MAIN_MENU;
                    break;
                case (1):
                    System.exit(0);
                    break;
                }
                break;
            case INSTRUCTION:
                state = GameState.MAIN_MENU;
                break;
            default:
                break;
            }

        }
    };

    private MouseInputAdapter mouseMotionListener = new MouseInputAdapter() {
        @Override
        public void mouseMoved(MouseEvent event) {
            switch (state) {
            case MAIN_MENU:
                int index = mMenu.getItemAtScreenPosition(event.getX(),
                        event.getY());
                switch (index) {
                case 0:
                    mMenu.setSelected(0);
                    break;
                case 1:
                    mMenu.setSelected(0);
                    break;
                case 2:
                    mMenu.setSelected(0);
                    break;
                case 3:
                    mMenu.setSelected(1);
                    break;
                case 4:
                    mMenu.setSelected(2);
                    break;
                case 5:
                    mMenu.setSelected(3);
                }
                break;
            case GAME_OVER:
                int index1 = mEndState.getItemAtScreenPosition(event.getX(),
                        event.getY());
                mEndState.setSelected(index1);
                break;
            case INSTRUCTION:

                break;
            default:
                break;
            }

        }
    };

}
