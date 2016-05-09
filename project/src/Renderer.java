import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

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

    private static final int PRERENDER_X = 500;
    private static final int PRERENDER_Y = 500;
    private static final int SIZE_X = 50;
    private static final int SIZE_Y = 50;
    private static final int TILE_X = PRERENDER_X / SIZE_X;
    private static final int TILE_Y = PRERENDER_Y / SIZE_Y;
    
    private Font stringFont = new Font( "SansSerif", Font.PLAIN, 18 );

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
        frameGraphics.drawImage(mazeBackground, 0, 0, null);
        for (Entity ent : grid.getEntities()) {
            frameGraphics.drawImage(ent.getSprite().getCurrentImage(),
                    ent.getX() * TILE_X, ent.getY() * TILE_Y, TILE_X, TILE_Y,
                    null);
        }
        PlayerEntity p = grid.getPlayer();
        frameGraphics.drawImage(p.getSprite().getCurrentImage(),
                p.getX() * TILE_X, p.getY() * TILE_Y, TILE_X, TILE_Y, null);
    }

    public void drawMenu(Menu m){
    	frameGraphics.clearRect(0, 0, PRERENDER_X, PRERENDER_Y/2);
    	frameGraphics.clearRect(0, PRERENDER_Y/2, PRERENDER_X, PRERENDER_Y/6);
    	frameGraphics.clearRect(0, PRERENDER_Y*4/6, PRERENDER_X, PRERENDER_Y/6);
    	frameGraphics.clearRect(0, PRERENDER_Y*5/6, PRERENDER_X, PRERENDER_Y/6);

    	frameGraphics.drawRect(0, 0, PRERENDER_X, PRERENDER_Y/2);
    	frameGraphics.drawImage(ImageCache.getImage("wobcke"), 0 , 0, null);
		frameGraphics.setFont(stringFont);
		frameGraphics.drawString("Maze Game", PRERENDER_X/2, PRERENDER_Y/4);

		frameGraphics.drawRect(0, PRERENDER_Y/2, PRERENDER_X, PRERENDER_Y/6);
		frameGraphics.drawString("EASY", PRERENDER_X/2, PRERENDER_Y*7/12);

		frameGraphics.drawRect(0, PRERENDER_Y*4/6, PRERENDER_X, PRERENDER_Y/6);
		frameGraphics.drawString("MEDIUM", PRERENDER_X/2, PRERENDER_Y*9/12);

		frameGraphics.drawRect(0, PRERENDER_Y*5/6, PRERENDER_X, PRERENDER_Y/6);
		frameGraphics.drawString("HARD", PRERENDER_X/2, PRERENDER_Y*11/12);
    	//frameGraphics.drawImage(ImageCache.getImage("asset/wobcke.jpg"), 0 , 0, null);
    	
    	switch(m.getSelected()){
    		case(0):			
    			frameGraphics.setColor(Color.DARK_GRAY);
				frameGraphics.fillRect(0, PRERENDER_Y/2, PRERENDER_X, PRERENDER_Y/6);
    			frameGraphics.setColor(Color.WHITE);
				frameGraphics.drawString("EASY", PRERENDER_X/2, PRERENDER_Y*7/12);
    			break;
    		case(1):
    			frameGraphics.setColor(Color.DARK_GRAY);
				frameGraphics.fillRect(0, PRERENDER_Y*4/6, PRERENDER_X, PRERENDER_Y/6);
    			frameGraphics.setColor(Color.WHITE);
    			frameGraphics.drawString("MEDIUM", PRERENDER_X/2, PRERENDER_Y*9/12);
    			break;
    		case(2):	
    			frameGraphics.setColor(Color.DARK_GRAY);
				frameGraphics.fillRect(0, PRERENDER_Y*5/6, PRERENDER_X, PRERENDER_Y/6);
    			frameGraphics.setColor(Color.WHITE);
    			frameGraphics.drawString("HARD", PRERENDER_X/2, PRERENDER_Y*11/12);
    			break;
    	}
    }

    public void createPreRender(Grid grid) {
        mazeBackground = new BufferedImage(PRERENDER_X, PRERENDER_Y,
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = mazeBackground.getGraphics();
        for (int y = 0; y < grid.getSizeY(); y++) {
            for (int x = 0; x < grid.getSizeX(); x++) {
                Tile tile = grid.getTile(x, y);
                Image img = tile.getImage();
                g.drawImage(img, TILE_X * x, TILE_Y * y, TILE_X, TILE_Y, null);
            }
        }
    }

    public void destroyPreRender() {
        mazeBackground = null;
    }

}
