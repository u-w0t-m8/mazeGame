import java.awt.Image;
import java.util.Collection;

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
    private LivingEntity player;
    private int sizex;
    private int sizey;
    
    public Grid(){
        generate(50,50);
        player = new PlayerEntity();
        player.setPos(1, 1);
    }
    
    private void generate(int sx, int sy){
        tileSpace = new Tile[sx][sy];
        Image imgBlank = ImageCache.getImage("tile_blank");
        Image imgWall = ImageCache.getImage("tile_wall");
        for (int y = 0; y < sy; y++){
            for (int x = 0; x < sx; x++){
                if (x == 0 || x == sx-1 || y == 0 || y == sy-1){
                    tileSpace[x][y] = new Tile(true, imgWall);
                } else {
                    tileSpace[x][y] = new Tile(false, imgBlank);
                }
            }
        }
    }
    
    public boolean checkCollision(){
    	for(Entity e : entList){
    		if((player.getIntermediateX() + 0.5 < e.getX() || player.getIntermediateX() - 0.5 > e.getX()) && 
    				(player.getIntermediateY() + 0.5 < e.getY() || player.getIntermediateY() - 0.5 > e.getY())){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Update the Grid and its entities.
     */
    public void update(){
    	
    	if(checkCollision()){
    		//end game
    	}
    	
        for (Entity ent: entList){
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
    
    public int getPlayerX(){
    	return player.getX();
    }
    
    public int getPlayerY(){
    	return player.getY();	
    }
    
    public void setPlayerInput(int x, int y){
    	// DO STUFF
    	// X will be 1 if its moving to the right, -1 if left, 0 no movement
    	// Y will be 1 if its moving downwards, -1 if upwards, 0 no movement
    }
    
    public void print(){
    	for(int i = 0; i < tileSpace.length; i++)
    	{
    	    for(int j = 0; j < tileSpace[i].length; j++)
    	    {
    	        if(tileSpace[i][j].getIsWall())
    	    		System.out.print("X");
    	        else
    	        	System.out.print("O");
    	    }
    	    System.out.println();
    	}
    }

}
