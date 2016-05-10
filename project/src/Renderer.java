import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Responsible for drawing maze terrain and its entities (and menus,
 * eventually).
 * <p>
 * Should probably be responsible for handling windows and canvas too (no
 * discussion on that yet).
 * <p>
 * As Engine definitively knows the game state, Renderer exposes various draw
 * methods for Engine to choose from as appropriate (e.g. drawing the main menu,
 * or the game grid, or the grid with a pause menu overlay).
 * <p>
 * All draw calls should take place between startFrame() and finishFrame() to
 * allow frame buffering (the game will look really glitchy and odd otherwise).
 * 
 */
public class Renderer {

    // these should be at least as big as the largest expected screen height
    private static final int PRERENDER_X = 1000;
    private static final int PRERENDER_Y = 1000;

    // canvas background color
    private static final Color BG_COLOR = Color.BLACK;
    
    // colors for menu items
    private static final Color MENU_DEFAULT_FILL = Color.BLACK;
    private static final Color MENU_DEFAULT_CONTENT = Color.WHITE;
    private static final Color MENU_SELECTED_FILL = Color.WHITE;
    private static final Color MENU_SELECTED_CONTENT = Color.BLACK;
    
    // whether to wipe the canvas clean on each frame (recommended)
    private static final boolean CLEAN_FRAME = true;

    private final Font stringFont = new Font("SansSerif", Font.PLAIN, 36);
    private Image mazeBackground;
    private BufferStrategy bufferStrategy = null;
    private Graphics frameGraphics;

    private int resX, resY;

    public Renderer(Canvas c) {
        bufferStrategy = c.getBufferStrategy();
        c.setBackground(BG_COLOR);
    }

    public void setResolution(int x, int y) {
        resX = x;
        resY = y;
    }

    /**
     * Start a new frame for display. After drawing, finishFrame() must be
     * called to display the completed frame.
     */
    public void startFrame() {
        frameGraphics = bufferStrategy.getDrawGraphics();
        if (CLEAN_FRAME) {
            frameGraphics.clearRect(0, 0, resX, resY);
        }
        frameGraphics.setColor(Color.BLACK);
    }

    /**
     * Tries to display the completed frame and free drawing resources. The draw
     * buffers used are volatile and can be lost during rendering, destroying
     * render progress. If this happens, the method returns false and the caller
     * should restart the render.
     * 
     * @return True if the frame render was successful.
     */
    public boolean finishFrame() {
        frameGraphics.dispose();
        if (bufferStrategy.contentsRestored()) {
            return false;
        }
        bufferStrategy.show();
        if (bufferStrategy.contentsLost()) {
            return false;
        }
        return true;
    }

    public void drawGrid(Grid grid) {
        final int S = 1000;
        final int TX = S / grid.getSizeX();
        final int TY = S / grid.getSizeY();
        final double MARGIN = 0.05;
        Graphics2D g = getTransformedGraphics(MARGIN, S, S);
        g.drawImage(mazeBackground, 0, 0, S, S, null);
        for (Entity ent : grid.getEntities()) {
            g.drawImage(ent.getSprite().getCurrentImage(), ent.getX() * TX,
                    ent.getY() * TY, TX, TY, null);
        }
        PlayerEntity p = grid.getPlayer();
        g.drawImage(p.getSprite().getCurrentImage(), p.getX() * TX,
                p.getY() * TY, TX, TY, null);
    }

    /**
     * Draw Menu initializes the graphics for the menu interface, using the
     * method within menu to know which 'difficulty' it is currently on
     * 
     * @param m menu
     */

    public void drawMenu(Menu m) {
        // S can be anything: it gets transformed into the full screen
        final int S = 1000;
        final double MARGIN = 0.05;
        Graphics2D g = getTransformedGraphics(MARGIN, S, S);
        g.setColor(MENU_DEFAULT_CONTENT);
        g.drawRect(0, 0, S, S / 2);
        // frameGraphics.drawImage(ImageCache.getImage("wobcke"), 0, 0, 0, 500,
        // null);
        g.setFont(stringFont);
        drawStringCentred(g, "Maze Game", S / 2, S / 4);

        String[] strings = new String[] { "EASY", "MEDIUM", "HARD" };

        // renders the 3 menu buttons in order
        for (int i = 0; i < 3; i++) {
            // shift each successive button's Y coords downwards
            int buttonY = S * (3 + i) / 6;
            int textY = S * (2 * i + 7) / 12;
            // decide colors for item
            Color fill, content;
            if (i == m.getSelected()) {
                fill = MENU_SELECTED_FILL;
                content = MENU_SELECTED_CONTENT;
            } else {
                fill = MENU_DEFAULT_FILL;
                content = MENU_DEFAULT_CONTENT;
            }
            // fill and draw rectangle then text
            g.setColor(fill);
            g.fillRect(0, buttonY, S, S / 6);
            g.setColor(content);
            g.drawRect(0, buttonY, S, S / 6);
            drawStringCentred(g, strings[i], S / 2, textY);
        }
    }

    private void drawStringCentred(Graphics g, String s, int x, int y) {
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
        int dx = (int) bounds.getCenterX();
        int dy = (int) bounds.getCenterY();
        g.drawString(s, x - dx, y - dy);
    }

    public void createPreRender(Grid grid) {
        mazeBackground = new BufferedImage(PRERENDER_X, PRERENDER_Y,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = mazeBackground.getGraphics();
        final int TX = PRERENDER_X / grid.getSizeX();
        final int TY = PRERENDER_Y / grid.getSizeY();
        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);
                Image img = tile.getImage();
                g.drawImage(img, TX * x, TY * y, TX, TY, null);
            }
        }
    }

    public void destroyPreRender() {
        mazeBackground = null;
    }

    /**
     * Provides a resolution-independent graphics object that maps virtual
     * coordinates to a square area in the centre of the canvas.
     * <p>
     * Copies the existing frameGraphics object, and then translates and scales
     * it so that the coordinates (0..x, 0..y) map to the largest possible
     * square area at the centre of the canvas, with the specified margin from
     * the edge.
     * <p>
     * For example, drawing a rectangle from (0,0) to (50,50) with a graphics
     * object transformed with arguments (0.05, 100, 100) will draw a square
     * from the middle of the canvas extending up and left, with a 5% margin
     * from the top.
     * <p>
     * We can adjust this to allow for non-square aspect ratios if need be.
     * 
     * @param margin the percentage of the space that should be margin
     * @param x the horizontal size of the virtual coordinate space
     * @param y the vertical size of the virtual coordinate space
     * 
     * @return the transformed copy of frameGraphics
     */
    private Graphics2D getTransformedGraphics(double margin, int x, int y) {
        Graphics2D g2d = (Graphics2D) frameGraphics.create();
        if (resX > resY) {
            g2d.translate((resX - resY) / 2d, 0);
            g2d.scale(resY / (double) x, resY / (double) y);
        } else {
            g2d.translate(0, (resY - resX) / 2d);
            g2d.scale(resX / (double) x, resX / (double) y);
        }
        if (margin > 0) {
            g2d.translate(margin * x, margin * y);
            g2d.scale(1d - 2 * margin, 1d - 2 * margin);
        }
        return g2d;
    }

}
