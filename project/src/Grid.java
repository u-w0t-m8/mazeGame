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

    private Collection<Entity> entList;
    private Tile[][] tileSpace;
    private PlayerEntity player;
    private int sizex;
    private int sizey;
    private boolean gameEnd;
    
    private int coinsLeft;
    private int coinsCollected;
    
    boolean[][] visited = new boolean[48][48];
    int[] x = { 0, 2, 0, -2 };
    int[] y = { -2, 0, 2, 0 };

    public Grid(Difficulty diff) {
        sizex = 31;
        sizey = 31;
        entList = new ArrayList<Entity>();
        generate(sizex, sizey);
        player = new PlayerEntity();
        gameEnd = false;
        coinsCollected = 0;
        coinsLeft = sizex/2;
        Random rand = new Random();

        player.setPos(2, 2);
        
     // Place coin
        for (int i = 0; i < sizex/2; ++i) {
            entList.add(new Token());
            int x = rand.nextInt(sizex - 1) + 1;
            int y = rand.nextInt(sizey - 1) + 1;
            while (this.getTile(x, y).getIsWall()) {
                x = rand.nextInt(sizex - 1) + 1;
                y = rand.nextInt(sizey - 1) + 1;
            }
            ((ArrayList<Entity>) entList).get(i).setPos(x, y);
        }

        // Place hunter in random position
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
        }
        if(j >= 1){
        	entList.add(new HunterEntity());
        	((ArrayList<Entity>) entList).get(0+(int)(sizex/2)).setPos(sizex-3, sizey-3);
        }
        if(j >= 2){
        	entList.add(new HunterEntity());
        	((ArrayList<Entity>) entList).get(1+(int)(sizex/2)).setPos(sizex-3, 2);
        }
        if(j >= 3){
        	entList.add(new HunterEntity());
        	((ArrayList<Entity>) entList).get(2+(int)(sizex/2)).setPos(2, sizey-3);
        }

    }

    private void openMaze() {
        Random rand = new Random();
        Image imgBlank = ImageCache.getImage("tile_blank");
        
        for(int i = 2; i < sizey - 2; ++i){
        	for(int j = 2; j < sizex - 2; j += sizex - 5){
        		tileSpace[j][i] = new Tile(false, imgBlank);
        	}
        	for(int j = 2; j < sizex - 2; j += sizex - 5){
        		tileSpace[i][j] = new Tile(false, imgBlank);
        	}
        }
        
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

    private void DFS() {
        for (int i = 0; i < sizex - 2; i++) {
            for (int j = 0; j < sizey - 2; ++j) {
                visited[i][j] = false;
            }
        }

        Random rand = new Random();
        Image imgBlank = ImageCache.getImage("tile_blank");
        Queue<Integer> queueCol = new LinkedList<Integer>();
        Queue<Integer> queueRow = new LinkedList<Integer>();

        queueCol.add(20);
        queueRow.add(20);

        while (queueRow.size() > 0) {
            int row = queueRow.poll();
            int col = queueCol.poll();
            while (col > 2 && col < sizex - 3 && row > 2 && row < sizey - 3
                    && (!visited[row + 2][col] || !visited[row - 2][col]
                            || !visited[row][col + 2]
                            || !visited[row][col - 2])) {
                queueCol.add(col);
                queueRow.add(row);
                int d = rand.nextInt(4);
                while (visited[row + y[d]][col + x[d]]) {
                    d = rand.nextInt(4);
                }
                visited[row + (y[d]) / 2][col + (x[d]) / 2] = true;
                visited[row + y[d]][col + x[d]] = true;
                tileSpace[row + (y[d]) / 2][col + (x[d]) / 2] = new Tile(false,
                        imgBlank);
                tileSpace[row + y[d]][col + x[d]] = new Tile(false, imgBlank);
                col += x[d];
                row += y[d];
            }
        }
        openMaze();
    }

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

    public boolean checkCollision() {
        for (Entity e : entList) {
            if ((e.getX() <= player.getX() + 0.5
                    && e.getX() >= player.getX() - 0.5)
                    && (e.getY() <= player.getY() + 0.5
                            && e.getY() >= player.getY() - 0.5)) {
                if (e instanceof Token) {
                	coinsCollected++;
                	coinsLeft--;
                	entList.remove(e);
                } else {
                	gameEnd = true;
                }
                return true;
            }
        }
        return false;
    }
   /* 
    public boolean coinsLeft(){
    	for(Entity e : entList){
    		if(e instanceof Token){
    			return true;
    		}
    	}
    	return false;
    }*/

    /**
     * Update the Grid and its entities.
     */
    public void update() {
    	
        checkCollision();

        player.update(this);

        for (Entity ent : entList) {
        	ent.update(this);
        }
        
    }

    public void addEntity(Entity ent) {
        entList.add(ent);
    }

    public Collection<Entity> getEntities() {
        return entList;
    }

    public Tile getTile(int x, int y) {
        return tileSpace[x][y];
    }

    public int getSizeX() {
        return sizex;
    }

    public int getSizeY() {
        return sizey;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public float getPlayerX() {
        return player.getX();
    }

    public float getPlayerY() {
        return player.getY();
    }
    
    public int getCoinsCollected(){
    	return this.coinsCollected;
    }
    
    public int getCoinsLeft(){
    	return coinsLeft;
    }
    
    public boolean getGameEnd(){
    	return gameEnd;
    }

    /**
     * Updates player entity's keyboard inputs. See PlayerEntity
     * {@link PlayerEntity#updateInputs(int, int, int, int) method}.
     */
    public void updatePlayerInput(int up, int down, int left, int right) {
        player.updateInputs(up, down, left, right);
    }

}
