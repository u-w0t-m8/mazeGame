import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;

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
 * All draw calls should take place between startRender() and finishRender() to
 * allow frame buffering (the game will look really glitchy and odd otherwise).
 * 
 */
public class Renderer {

    Image mazeBackground;
    // Obtaining this depends on Window/JFrame, which we don't have yet.
    BufferStrategy bufferStrategy = null;
    Graphics frameGraphics;

    public void startRender() {
        frameGraphics = bufferStrategy.getDrawGraphics();
    }

    public void finishRender() {
        frameGraphics.dispose();
        bufferStrategy.show();
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
