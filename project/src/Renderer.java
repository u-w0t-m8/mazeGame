import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

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
	
    Image mazeBackground;
    JFrame mFrame;
    BufferStrategy bufferStrategy = null;
    Graphics frameGraphics;
    
    public Renderer(Canvas c) {
    	bufferStrategy = c.getBufferStrategy();
    }
    
    /**
     * Start a new frame for display. After drawing, finishFrame() must be
     * called to display the completed frame.
     */
    public void startFrame() {
        frameGraphics = bufferStrategy.getDrawGraphics();
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
        // draw background image
        for (Entity ent : grid.getEntities()) {
            // draw entity onto canvas
        }
    }

    public void createPreRender(Grid grid) {
        // make mazeBackground a new image
        Graphics g = mazeBackground.getGraphics();
        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Grid.Tile tile = grid.getTile(x, y);
                // draw tile onto background using g
            }
        }
    }

    public void destroyPreRender() {
        mazeBackground = null;
    }

}
