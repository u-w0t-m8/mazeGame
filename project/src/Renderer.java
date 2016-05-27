import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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
	private static final double GRID_MARGIN = 0.025;
	// these should be at least as big as the largest expected screen height
	private static final int GRID_PRERENDER_X = 1000;
	private static final int GRID_PRERENDER_Y = 1000;

	// canvas background color
	private static final Color BG_COLOR = Color.LIGHT_GRAY;

	// menu config
	private static final double MENU_MARGIN = 0.025;
	private static final Color MENU_DEFAULT_FILL = new Color(0xBEBEBE);
	private static final Color MENU_DEFAULT_CONTENT = Color.WHITE;
	private static final Color MENU_SELECTED_FILL = new Color(0x89C4C0);
	private static final Color MENU_SELECTED_CONTENT = new Color(0x4B989C);

	// whether to wipe the canvas clean on each frame (recommended)
	private static final boolean CLEAN_FRAME = true;

	private final Font stringFont = new Font("SansSerif", Font.PLAIN, 36);
	private Font selectedFont = new Font("Helvetica", Font.BOLD, 52);
	private Font inGameFont = new Font("SansSerif", Font.BOLD, 24);
	private Font gameOverFont = new Font("SansSerif", Font.BOLD, 72);
	private Image mazeBackground;
	private BufferStrategy bufferStrategy = null;
	private Graphics frameGraphics;

	private int resX = 800;
	private int resY = 500;

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

	/**
	 * Draws the in-game map drawing the grid, entities and side panel
	 * 
	 * @param grid - grid class object
	 */
	public void drawGrid(Grid grid) {
		final int S = 1000;
		final int TX = S / grid.getSizeX();
		final int TY = S / grid.getSizeY();
		//Offset the grid by -300 to fit i the side panel
		Graphics2D g = getTransformedGraphics(GRID_MARGIN, S - 300, S);
		g.drawImage(mazeBackground, 0, 0, S, S, null);
		//Draws all entitys to the grid
		for (Entity ent : grid.getEntities()) {
			g.drawImage(ent.getSprite().getCurrentImage(), (int) (ent.getX() * TX),
					(int) (ent.getY() * TY), TX, TY, null);
		}
		//Gets image of player one
		PlayerEntity p = grid.getPlayer();
		g.drawImage(p.getSprite().getCurrentImage(), (int) (p.getX() * TX), (int) (p.getY() * TY),
				TX, TY, null);
		
		//Gets image of player two (if exists)
		PlayerEntity p2 = null;
		if(grid.getPlayer2() != null){
			p2 = grid.getPlayer2();
			g.drawImage(p2.getSprite().getCurrentImage(), (int) (p2.getX() * TX), (int) (p2.getY() * TY),
					TX, TY, null);
		}
		
		//Draws Side panel printing the number of coins left
		//Change this for side panel color
		g.setColor(new Color(0xb3c87e));
		g.drawRect(-300, 0, 300, S - 10);
		g.fillRect(-300, 0, 300, S - 10);
		g.setColor(Color.white);
		g.setFont(inGameFont);
		if(grid.getPlayer2() == null){
			//If only one player then print image of player and coins left
			drawStringCentred(g, "Player", -150, S*6 /32);
			g.drawImage(p.getSprite().getCurrentImage(), -182, S * 7 / 32, 64, 64, null);
			drawStringCentred(g, "Coins Left", -150, S / 2);
			drawStringCentred(g, Integer.toString(grid.getCoinsLeft()), -150, S * 7 / 12);
		}else{
			//Prints image of both players, their coins collected and total coins left
			drawStringCentred(g, "Player 1", -150, S / 8);
			g.drawImage(p.getSprite().getCurrentImage(), -182, S*5/32, 64, 64, null);
			drawStringCentred(g, "Coins Collected", -150, S* 8/32);
			drawStringCentred(g, Integer.toString(grid.getCoinsCollected()), -150, S * 10 / 32);
			drawStringCentred(g, "Player 2", -150, S*13/32);
			g.drawImage(p2.getSprite().getCurrentImage(), -182, S * 14/32, 64, 64, null);
			drawStringCentred(g, "Coins Collected", -150, S* 17/32);
			drawStringCentred(g, Integer.toString(grid.getCoinsCollectedTwo()), -150, S * 19/ 32);
			drawStringCentred(g, "Total Coins Left", -150, S* 22/32);
			drawStringCentred(g, Integer.toString(grid.getCoinsLeft()), -150, S * 23 / 32);
		}
	}

	/**
	 * Draws graphics for instructions
	 */
	public void drawInstructions() {
		int SX = 1600;
		int SY = 900;
		
		Graphics2D g = getTransformedGraphics(MENU_MARGIN, SX, SY);
		g.drawImage(ImageCache.getImage("instructions"), 0, -100, SX, SY, null);
		g.setFont(stringFont);
		g.setColor(MENU_SELECTED_FILL);
		g.drawRect(0, SY * 7 / 8, SX, SY / 8);
		g.fillRect(0, SY * 7 / 8, SX, SY / 8);
		g.setColor(MENU_SELECTED_CONTENT);
		drawStringCentred(g, "< Back", SX / 2, SY * 15 / 16);
	}

	/**
	 *Draws the end state, either player has won, player has lost, multiplayer - player 1 has won or player 2 has won.
	 * Depending on whether all the coins have been collected, in singleplayer the player has won and in multiplayer the player with most coins out of the two win
	 * Otherwise if the AI has collided with a player, in singleplayer the player loses and in multiplayer the play not killed by the AI wins.
	 * 
	 * The first if statement will print out either Game over or Congratulations player has one depending on how the game has ended
	 * @param End class which controls the buttons
	 * @param multiplayer lets drawEndState know if game was multiplayer or not
	 * @param gameEndMode lets drawEndState know who won the multiplayer game
	 * @param coinsCollected how many coins collected by player1
	 * @param coinsCollected2 how many coins collected by player2
	 * @param coinsLeft is how many coins left in the grid
	 */
	public void drawEndState(EndState end,boolean multiplayer, int gameEndMode, int coinsCollected,int coinsCollected2, int coinsLeft) {
		final int SX = 1600;
		final int SY = 900;

		Graphics2D g = getTransformedGraphics(MENU_MARGIN, SX, SY);
		g.setFont(gameOverFont);
		g.setColor(MENU_DEFAULT_FILL);
		//If there are no coins left then the game is won
		if(coinsLeft == 0){
			//If player 1 has more coins than player 2 then player 1 has won
			if(multiplayer == true && coinsCollected > coinsCollected2){
				drawStringCentred(g, "CONGRATULATIONS PLAYER 1 HAS WON", SX / 2, SY / 8);
				g.drawImage(ImageCache.getImage("wonImage"), (SX/2-32), SY*3/16, 128, 128, null);
				drawStringCentred(g, "Coins Collected: " + Integer.toString(coinsCollected), SX / 2, SY * 2 / 5);
			//player 2 wins
			}else if(multiplayer == true && coinsCollected < coinsCollected2){
				drawStringCentred(g, "CONGRATULATIONS PLAYER 2 HAS WON", SX / 2, SY / 8);
				g.drawImage(ImageCache.getImage("win2"), (SX/2-32), SY*3/16, 128, 128, null);
				drawStringCentred(g, "Coins Collected: " + Integer.toString(coinsCollected2), SX / 2, SY * 2 / 5);
			}else{
			//Single player collected all coins
				drawStringCentred(g, "CONGRATULATIONS YOU HAVE WON", SX / 2, SY / 4);
			}
		}else{
			//Otherwise game over and prints the number of coins collected
			//If is multi player and player 1 has more coins collected or player 2 has been killed by AI
			if(multiplayer == true && gameEndMode == 2){
				drawStringCentred(g, "CONGRATULATIONS PLAYER 1 HAS WON", SX / 2, SY / 8);
				g.drawImage(ImageCache.getImage("wonImage"), (SX/2-32), SY*3/16, 128, 128, null);
				drawStringCentred(g, "Coins Collected: " + Integer.toString(coinsCollected), SX / 2, SY * 2 / 5);
			//If is multi player and player 2 has more coins collected or player 1 has been killed by AI
			}else if(multiplayer == true  && gameEndMode == 1){
				drawStringCentred(g, "CONGRATULATIONS PLAYER 2 HAS WON", SX / 2, SY / 8);
				g.drawImage(ImageCache.getImage("win2"), (SX/2-32), SY*3/16, 128, 128, null);
				drawStringCentred(g, "Coins Collected: " + Integer.toString(coinsCollected2), SX / 2, SY * 2 / 5);
			}else{
			//Single player loses
				drawStringCentred(g, "GAME OVER", SX / 2, SY / 8);
				g.drawImage(ImageCache.getImage("youLost"), (SX/2-32), SY*3/16, 128, 128, null);
				drawStringCentred(g, "Coins Collected: " + Integer.toString(coinsCollected), SX / 2, SY * 2 / 5);
			}
		}
		
		//Sets up coordinates for menu boxes for endstate class for mouselistener to work
		g.drawRect(0, SY * 5 / 8, SX, SY / 8);
		double[] pts = new double[] { 0, SY * 5 / 8, SX, SY* 6/ 8 };
		g.getTransform().transform(pts, 0, pts, 0, pts.length / 2);
		end.setMouseArea(0, (int) pts[0], (int) pts[1], (int) pts[2], (int) pts[3]);
		g.drawRect(0, SY * 6 / 8, SX, SY / 8);
		pts = new double[] {0, SY * 6 / 8, SX, SY*7/ 8};
		g.getTransform().transform(pts, 0, pts, 0, pts.length / 2);
		end.setMouseArea(1, (int) pts[0], (int) pts[1], (int) pts[2], (int) pts[3]);
		
		//Highlights the selected button
		if (end.getSelected() == 0) {
			//If menu is selected
			g.setFont(selectedFont);
			g.setColor(MENU_SELECTED_FILL);
			g.fillRect(0, SY * 5 / 8, SX, SY / 8);
			g.setColor(MENU_SELECTED_CONTENT);
			drawStringCentred(g, "Menu", SX / 2, SY * 11 / 16);
			g.setColor(MENU_DEFAULT_FILL);
			g.fillRect(0, SY * 6 / 8, SX, SY / 8);
			g.setColor(MENU_DEFAULT_CONTENT);
			drawStringCentred(g, "Exit", SX / 2, SY * 13 / 16);
		} else {
			//If exit is selected
			g.setFont(selectedFont);
			g.setColor(MENU_DEFAULT_FILL);
			g.fillRect(0, SY * 5 / 8, SX, SY / 8);
			g.setColor(MENU_DEFAULT_CONTENT);
			drawStringCentred(g, "Menu", SX / 2, SY * 11 / 16);
			g.setFont(selectedFont);
			g.setColor(MENU_SELECTED_FILL);
			g.fillRect(0, SY * 6 / 8, SX, SY / 8);
			g.setColor(MENU_SELECTED_CONTENT);
			drawStringCentred(g, "Exit", SX / 2, SY * 13 / 16);

		}
	}

	/**
	 * Draw Menu initializes the graphics for the menu interface, using the
	 * method within menu to know which 'difficulty' it is currently on
	 * 
	 * @param m menu object
	 */

	public void drawMenu(Menu m) {
		// SX and SY decide the aspect ratio of the menu
		final int SX = 1600;
		final int SY = 900;
		Graphics2D g = getTransformedGraphics(MENU_MARGIN, SX, SY);
		g.drawImage(ImageCache.getImage("title"), 0, 0, SX, SY / 2, null);
		g.setColor(MENU_DEFAULT_CONTENT);
		g.drawRect(0, 0, SX, SY / 2);
		g.setFont(stringFont);
		// drawStringCentred(g, "Maze Game", SX / 2, SY / 4);
		
		String[] difficulty = new String[] { "PLAY:EASY", "PLAY:MEDIUM", "PLAY:HARD"};
		String[] strings = new String[] { difficulty[m.getDifficulty()], "MULTIPLAYER", "INSTRUCTIONS", "QUIT" };
		if (m.getSelected() == 0) {
			strings[0] = difficulty[m.getDifficulty()];
		}

		// renders the 3 menu buttons in order
		for (int i = 0; i < 4; i++) {
			// shift each successive button's Y coords downwards
			final int buttonY = SY * (4 + i) / 8;
			// decide colors for item
			Color colFill, colContent;
			Font textFont;
			//If is current selected box then use selected font
			if (i == m.getSelected()) {
				colFill = MENU_SELECTED_FILL;
				colContent = MENU_SELECTED_CONTENT;
				textFont = selectedFont;
			} else {
				colFill = MENU_DEFAULT_FILL;
				colContent = MENU_DEFAULT_CONTENT;
				textFont = stringFont;
			}
			if(i==0){
				//drawing 3 buttons for play game left and right
				drawButton(m,0,g,0,buttonY,SX/8,SY/6,colFill,colContent,"<",textFont);
					
				drawButton(m,1,g,SX/8,buttonY,SX*6/8,SY/6,colFill,colContent,strings[i],textFont);
					
				drawButton(m,2,g,SX*7/8,buttonY,SX/8,SY/6,colFill,colContent,">",textFont);
			}else{
				drawButton(m, i+2, g, 0, buttonY, SX, SY / 6, colFill, colContent, strings[i], textFont);
			}
		}
	}

	/**Draw button for menu
	 * 
	 * @param m - Menu object
	 * @param mIndex - Index to be added to Rectangle array
	 * @param g - Graphics object
	 * @param x - x coordinate 
	 * @param y - y coordinate
	 * @param w - width of rectangle
	 * @param h - height of rectangle
	 * @param cf - Color of rectangle
	 * @param cc - color of content
	 * @param text - string to be drawn center of rectangle
	 * @param font - font of string to be drawn
	 */
	private void drawButton(Menu m, int mIndex, Graphics2D g, int x, int y, int w, int h, Color cf,
			Color cc, String text, Font font) {
		g.setColor(cf);
		g.fillRect(x, y, w, h);
		g.setColor(cc);
		g.drawRect(x, y, w, h);
		g.setFont(font);
		drawStringCentred(g, text, x + (w/2), y + (h / 2));

		double[] pts = new double[] { x, y, x + w, y + h };
		g.getTransform().transform(pts, 0, pts, 0, pts.length / 2);
		m.setMouseArea(mIndex, (int) pts[0], (int) pts[1], (int) pts[2], (int) pts[3]);
	}

	/**
	 * Centre the objects
	 * 
	 * @param g
	 * @param s
	 * @param x
	 * @param y
	 */
	private void drawStringCentred(Graphics g, String s, int x, int y) {
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
		int dx = (int) bounds.getCenterX();
		int dy = (int) bounds.getCenterY();
		g.drawString(s, x - dx, y - dy);
	}

	/**
	 * 
	 * @param grid
	 */
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

	/**
	 * 
	 */
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
	 * @param margin
	 *            the percentage of the space that should be margin
	 * @param x
	 *            the horizontal size of the virtual coordinate space
	 * @param y
	 *            the vertical size of the virtual coordinate space
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
