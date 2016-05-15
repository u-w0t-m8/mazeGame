import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

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

    Image title = ImageCache.getImage("title");
    // maze render config
    private static final double GRID_MARGIN = 0.05;
    // these should be at least as big as the largest expected screen height
    private static final int GRID_PRERENDER_X = 1000;
    private static final int GRID_PRERENDER_Y = 1000;

    // canvas background color
    private static final Color BG_COLOR = Color.LIGHT_GRAY;

    // menu config
    private static final double MENU_MARGIN = 0.05;
    private static final Color MENU_DEFAULT_FILL = new Color(0xA9EBE6);
    private static final Color MENU_DEFAULT_CONTENT = Color.WHITE;
    private static final Color MENU_SELECTED_FILL = new Color(0x89C4C0);
    private static final Color MENU_SELECTED_CONTENT = new Color(0x4B989C);

    // whether to wipe the canvas clean on each frame (recommended)
    private static final boolean CLEAN_FRAME = true;

    private final Font STRINGFONT = new Font("SansSerif", Font.PLAIN, 36);
    private Font selectedFont = new Font("Helvetica", Font.BOLD, 52);
    private Font inGameFont = new Font("SansSerif", Font.BOLD, 24);
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
        Graphics2D g = getTransformedGraphics(GRID_MARGIN, S-300, S);
        g.drawImage(mazeBackground, 0, 0, S, S, null);
        for (Entity ent : grid.getEntities()) {
            g.drawImage(ent.getSprite().getCurrentImage(), (int)(ent.getX() * TX),
                    (int)(ent.getY() * TY), TX, TY, null);
        }
        PlayerEntity p = grid.getPlayer();
        g.drawImage(p.getSprite().getCurrentImage(), (int)(p.getX() * TX),
                (int)(p.getY() * TY), TX, TY, null);
        g.setColor(MENU_DEFAULT_FILL);
        g.drawRect(-300, 0, 300, S-10);
        g.fillRect(-300, 0, 300, S-10);
        g.setColor(Color.white);
        g.setFont(inGameFont);
        drawStringCentred(g,"Player",-150,S/4);
        g.drawImage(p.getSprite().getCurrentImage(), -182, S*9/32, 64, 64, null);
        drawStringCentred(g,"Coins Collected",-150,S/2);
        drawStringCentred(g,Integer.toString(grid.getCoinsCollected()),-150,S*7/12);

        //Graphics2D sidePanel = getTransformedGraphics(GRID_MARGIN,-300,S);
        //sidePanel.drawRect(-300, 0, 300, S);
    }
    
    public void drawEndState(){
    }

    /**
     * Draw Menu initializes the graphics for the menu interface, using the
     * method within menu to know which 'difficulty' it is currently on
     * 
     * @param m menu
     */

    public void drawMenu(Menu m) {
        // SX and SY decide the aspect ratio of the menu
        final int SX = 1600;
        final int SY = 900;
        Graphics2D g = getTransformedGraphics(MENU_MARGIN, SX, SY);
        g.drawImage(ImageCache.getImage("title"), 0, 0, SX, SY/2, null);
        g.setColor(MENU_DEFAULT_CONTENT);
        g.drawRect(0, 0, SX, SY / 2);
        g.setFont(STRINGFONT);
        //drawStringCentred(g, "Maze Game", SX / 2, SY / 4);

        String[] strings = new String[] { "EASY", "MEDIUM", "HARD" };

        // renders the 3 menu buttons in order
        for (int i = 0; i < 3; i++) {
            // shift each successive button's Y coords downwards
            final int buttonY = SY * (3 + i) / 6;
            final int textY = SY * (2 * i + 7) / 12;
            // decide colors for item
            Color colFill, colContent;
            if (i == m.getSelected()) {
                colFill = MENU_SELECTED_FILL;
                colContent = MENU_SELECTED_CONTENT;
                g.setFont(selectedFont);
            } else {
                colFill = MENU_DEFAULT_FILL;
                colContent = MENU_DEFAULT_CONTENT;
                g.setFont(STRINGFONT);
            }
            // fill and draw rectangle then text
            g.setColor(colFill);
            g.fillRect(0, buttonY, SX, SY / 6);
            g.setColor(colContent);
            g.drawRect(0, buttonY, SX, SY / 6);
            drawStringCentred(g, strings[i], SX / 2, textY);
        }
    }

    private void drawStringCentred(Graphics g, String s, int x, int y) {
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
        int dx = (int) bounds.getCenterX();
        int dy = (int) bounds.getCenterY();
        g.drawString(s, x - dx, y - dy);
    }

    public void createPreRender(Grid grid) {
        mazeBackground = new BufferedImage(GRID_PRERENDER_X, GRID_PRERENDER_Y,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = mazeBackground.getGraphics();
        final int TX = GRID_PRERENDER_X / grid.getSizeX();
        final int TY = GRID_PRERENDER_Y / grid.getSizeY();
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
     * coordinates to a rectangular area in the centre of the canvas.
     * <p>
     * Copies the existing frameGraphics object, and then translates and scales
     * it so that the coordinates (0..x, 0..y) map to the largest possible
     * rectangle at the centre of the canvas with aspect ratio x/y, and given
     * percentage margin from the edge.
     * 
     * @param margin the percentage of the space that should be margin
     * @param x the horizontal size of the virtual coordinate space
     * @param y the vertical size of the virtual coordinate space
     * 
     * @return the transformed copy of frameGraphics
     */
    private Graphics2D getTransformedGraphics(double margin, int x, int y) {
        Graphics2D g2d = (Graphics2D) frameGraphics.create();
        final double w = resX;
        final double h = resY;
        final double rx = x / w;
        final double ry = y / h;
        // decide whether to expand to the width or height of the screen
        if (ry > rx) {
            // shift right to keep designated rectangle in centre
            g2d.translate((w - (h * x / y)) / 2, 0);
            g2d.scale(1 / ry, 1 / ry);
        } else {
            // shift down to keep designated rectangle in centre
            g2d.translate(0, (h - (w * y / x)) / 2);
            g2d.scale(1 / rx, 1 / rx);
        }
        // apply the margin in a similar way if there is one
        if (margin > 0) {
            g2d.translate(margin * x, margin * y);
            g2d.scale(1d - 2 * margin, 1d - 2 * margin);
        }
        return g2d;
    }

}
