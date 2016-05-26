import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * The physical space in which the game takes place. Tiles make up its physical
 * terrain and entities interact within it. Tiles themselves contain lists of
 * entities local to that tile for collision checking.
 * <p>
 * Each new 'level' of the game is a new Grid. Almost no game state carries over
 * levels.
 * <p>
 * How exactly instantiation of this class works is TBD till we decide on
 * generation. Engine will probably call on a generator which will create a
 * blank Grid and fill it in.
 * 
 */
public class Grid {
	
	private final int SIZE = 31; 
	
    private Collection<Entity> entList; // List of entities in the game
    private Tile[][] tileSpace; // 2D array of tiles forming the grid
    private PlayerEntity player; // player one object
    private PlayerEntity player2; // player two object
    private int sizex; // width of the grid
    private int sizey; // height of the game
    
    private int coinsLeft; // coins left in the game
    private int coinsCollected; // coins collected by player one
    private int coinsCollectedTwo; // coins collected by player two
    private int gameEnd; // which player has lost the game
    
    private boolean[][] visited = new boolean[SIZE][SIZE]; // visited array used for DFS
    //Used for adding the four adjacent tiles when performing DFS
    private int[] x = { 0, 2, 0, -2 };
    private int[] y = { -2, 0, 2, 0 };

    public Grid(Difficulty diff) {
        sizex = SIZE;
        sizey = SIZE;
        entList = new ArrayList<Entity>();
        generate(sizex, sizey);
        player = new PlayerEntity(1);
        player2 = null;
        
        //Sets the player spawn locations depending on single player and multiplayer 
        if(diff == Difficulty.MULTIPLAYER){
        	player.setPos(sizex - 3, 2);
        	player2 = new PlayerEntity(2);
            player2.setPos(2, 2);
        }
        else {
        	player.setPos(2, 2);
        }
        
        gameEnd = -1;
        coinsCollected = 0;
        coinsCollectedTwo = 0;
        coinsLeft = sizex/3+1;
        Random rand = new Random();
        
        // Place appropriate number of coins in the game depending on the size
        for (int i = 0; i < sizex/3+1; ++i) {
            entList.add(new Token());
            int x = rand.nextInt(sizex - 1) + 1;
            int y = rand.nextInt(sizey - 1) + 1;
            while (this.getTile(x, y).getIsWall()) {
                x = rand.nextInt(sizex - 1) + 1;
                y = rand.nextInt(sizey - 1) + 1;
            }
            ((ArrayList<Entity>) entList).get(i).setPos(x, y);
        }

        // Find out the difficulty and set number of hunters depending on difficulty
        int j = 0;
        switch (diff) {
        case EASY:
            j = 1;
            break;
        case NORMAL:
            j = 2;
            break;
        case HARD:
            j = 3;
            break;
    	case MULTIPLAYER:
    		j = 1;
    		break;
    	}
        
        // Spawn the number of hunters set above
        if(j >= 1){
        	entList.add(new HunterEntity(0));
        	if(player2 != null){
        		((ArrayList<Entity>) entList).get(0+(int)(sizex/3)+1).setPos(sizex/2-1, sizey-3);
        	}
        	else {
        		((ArrayList<Entity>) entList).get(0+(int)(sizex/3)+1).setPos(sizex-3, sizey-3);
        	}
        }
        if(j >= 2){
        	entList.add(new HunterEntity(1));
        	((ArrayList<Entity>) entList).get(1+(int)(sizex/3)+1).setPos(sizex-3, 2);
        }
        if(j >= 3){
        	entList.add(new HunterEntity(2));
        	((ArrayList<Entity>) entList).get(2+(int)(sizex/3)+1).setPos(2, sizey-3);
        }
    }

    /**
     * Creates loops in the map, allowing escape routes for the players
     */
    private void openMaze() {
        Random rand = new Random();
        Image imgBlank = ImageCache.getImage("tile_blank");
        
        // Open up the outer layer of the grid
        for(int i = 2; i < sizey - 2; ++i){
        	for(int j = 2; j < sizex - 2; j += sizex - 5){
        		tileSpace[j][i] = new Tile(false, imgBlank);
        	}
        	for(int j = 2; j < sizex - 2; j += sizex - 5){
        		tileSpace[i][j] = new Tile(false, imgBlank);
        	}
        }
        
        // Iterates through entire tile space, and randomly opens up walls if and only if
        // two adjacent sides are walls, and the two other are not walls
        for (int i = 1; i < sizey - 1; ++i) {
            for (int j = 1; j < sizex - 1; ++j) {
                if (tileSpace[i][j].getIsWall() && 
                		((!tileSpace[i - 1][j].getIsWall() && !tileSpace[i + 1][j].getIsWall() && tileSpace[i][j-1].getIsWall() && tileSpace[i][j+1].getIsWall()) || 
                				(!tileSpace[i][j-1].getIsWall() && !tileSpace[i][j+1].getIsWall() && tileSpace[i-1][j].getIsWall() && tileSpace[i+1][j].getIsWall()))) {
                    int d = rand.nextInt(4);
                    if (d == 0) {
                        tileSpace[i][j] = new Tile(false, imgBlank);
                    }
                }
            }
        }
    }

    /**
     * Performs a depth first search on the grid to create paths
     */
    private void DFS() {
    	//Initialise visited 2D array
        for (int i = 0; i < sizex - 2; i++) {
            for (int j = 0; j < sizey - 2; ++j) {
                visited[i][j] = false;
            }
        }

        //DFS initialisation
        Random rand = new Random();
        Image imgBlank = ImageCache.getImage("tile_blank");
        Queue<Integer> queueCol = new LinkedList<Integer>();
        Queue<Integer> queueRow = new LinkedList<Integer>();
        queueCol.add(20);
        queueRow.add(20);

        //While there are still tiles to visit
        while (queueRow.size() > 0) {
            int row = queueRow.poll();
            int col = queueCol.poll();
            //While all adjacent directions tiles haven't been visited
            while (col > 2 && col < sizex - 3 && row > 2 && row < sizey - 3
                    && (!visited[row + 2][col] || !visited[row - 2][col] || !visited[row][col + 2] || !visited[row][col - 2])) {
                queueCol.add(col);
                queueRow.add(row);
                
                //Find the direction that hasn't been visited 
                int d = rand.nextInt(4);
                while (visited[row + y[d]][col + x[d]]) {
                    d = rand.nextInt(4);
                }
                
                //Open up one and two walls in the direction and set them as visited
                visited[row + (y[d]) / 2][col + (x[d]) / 2] = true;
                visited[row + y[d]][col + x[d]] = true;
                tileSpace[row + (y[d]) / 2][col + (x[d]) / 2] = new Tile(false, imgBlank);
                tileSpace[row + y[d]][col + x[d]] = new Tile(false, imgBlank);
                col += x[d];
                row += y[d];
            }
        }
        openMaze();
    }

    /**
     * Generates a grid with argument sizes and fills it with walls
     * @param sx - width of the grid
     * @param sy - height of the grid
     */
    private void generate(int sx, int sy) {
        tileSpace = new Tile[sx][sy];
        Image imgWall = ImageCache.getImage("tile_wall");
        for (int y = 0; y < sy; y++) {
            for (int x = 0; x < sx; x++) {
                tileSpace[x][y] = new Tile(true, imgWall);
            }
        }
        DFS();
    }

    /**
     * Checks whether the players have collided with other entities in the game
     */
    public void checkCollision() {
    	
    	for (Entity e : entList) { // For all entites in game
    		//Check if player one has collided with entity
    		if ((e.getX() <= player.getX() + 0.5 && e.getX() >= player.getX() - 0.5)
                    && (e.getY() <= player.getY() + 0.5 && e.getY() >= player.getY() - 0.5)) {
    			// If player one collided with a coin, remove from entity list and increment player one coin count
    			if (e instanceof Token) {
    				coinsCollected++;
    				coinsLeft--;
    				entList.remove(e);
    				break;
    			}
    			else {
                    gameEnd = 1; // set player one as lost
                }
    		}
    		//Check if player one has collided with entity
    		else if (player2 != null && ((e.getX() <= player2.getX() + 0.5 && e.getX() >= player2.getX() - 0.5)
                    && (e.getY() <= player2.getY() + 0.5 && e.getY() >= player2.getY() - 0.5))) {
    			// If player two collided with a coin, remove from entity list and increment player two coin count
    			if (e instanceof Token) { 
    				coinsCollectedTwo++;
    				coinsLeft--;
    				entList.remove(e);
    				break;
    			}
    			else {
                	gameEnd = 2; // set player two as lost
                }
    		}
    	}
    	//If there are no more coins left, end game
    	if(coinsLeft == 0){
    		gameEnd = 0;
    	}
    }

    /**
     * Update the Grid and its entities.
     */
    public void update() {
    	
        checkCollision();

        player.update(this);
        if(player2 != null){
        	player2.update(this);
        }

        for (Entity ent : entList) {
        	ent.update(this);
        }
        
    }

    /**
     * @return - list of entities currently in the game
     */
    public Collection<Entity> getEntities() {
        return entList;
    }

    /**
     * @param x - x coordinate of the tile
     * @param y - y coordinate of the tile
     * @return - the tile to be return at (x,y)
     */
    public Tile getTile(int x, int y) {
        return tileSpace[x][y];
    }

    /**
     * @return - height of the grid
     */
    public int getSizeX() {
        return sizex;
    }

    /**
     * @return - width of the grid
     */
    public int getSizeY() {
        return sizey;
    }

    /**
     * @return - player one object
     */
    public PlayerEntity getPlayer() {
        return player;
    }
    
    /**
     * @return - player two object
     */
    public PlayerEntity getPlayer2() {
        return player2;
    }
    
    /**
     * @return - number of coins player one has collected
     */
    public int getCoinsCollected(){
    	return this.coinsCollected;
    }
    
    /**
     * @return - number of coins player two has collected
     */
    public int getCoinsCollectedTwo(){
    	return this.coinsCollectedTwo;
    }
    
    /**
     * @return - the coins left in the game
     */
    public int getCoinsLeft(){
    	return this.coinsLeft;
    }
    
    /**
     * @return - the player who lost
     */
    public int getGameEnd(){
    	return this.gameEnd;
    }

    /**
     * @return - whether the game is multiplayer or not
     */
    public boolean getIsMulti(){
    	if(player2 != null){
    		return true;
    	}
    	return false;
    }
    /**
     * Updates player entity's keyboard inputs. See PlayerEntity
     * {@link PlayerEntity#updateInputs(int, int, int, int) method}.
     */
    public void updatePlayerInput(int up, int down, int left, int right) {
        player.updateInputs(up, down, left, right);
    }
    
    /**
     * Updates player entity's keyboard inputs. See PlayerEntity
     * {@link PlayerEntity#updateInputs(int, int, int, int) method}.
     */
    public void updatePlayerInputTwo(int up, int down, int left, int right) {
        player2.updateInputs(up, down, left, right);

    }
}
