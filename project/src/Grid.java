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
    private Entity player;
    private int sizex;
    private int sizey;

    public class Tile {
        boolean isWall;
        Image tileImg;
        Collection<Entity> localList = null; // probably an ArrayList
    }
    
    public Grid(Tile[][] terrain){
    	this.tileSpace = terrain;
    }
    
    /**
     * Update the Grid and its entities.
     */
    public void update(){
        for (Entity ent: entList){
        	ent.update(this);
        }
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

}
