import java.awt.Image;

public class Tile {
    
    private boolean isWall; // Whether the tile is a wall or not
    private Image tileImg; // Sprite image

    public Tile(boolean wall, Image img){
        isWall = wall;
        tileImg = img; 
    }
    
    /**
     * Sets the state of the tile
     * @postcondition - the tile's boolean value will be changed to the parameter
     * @param wall - whether the tile is a wall or not
     */
    public void setIsWall(boolean wall){
        isWall = wall;
    }
    
    /**
     * @return - whether the tile is a wall or not
     */
    public boolean getIsWall(){
        return isWall;
    }
    
    /**
     * Sets the sprite of the tile
     * @postcondition - the tiles's sprite will be changed to the parameter
     * @param img - image of the sprite to be set to the tile
     */
    public void setImage(Image img){
        tileImg = img;
    }
    
    /**
     * @return - the sprite image
     */
    public Image getImage(){
        return tileImg;
    }
    
}
