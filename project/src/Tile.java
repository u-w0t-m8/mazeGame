import java.awt.Image;
import java.util.Collection;

public class Tile {
    
    private boolean isWall;
    private Image tileImg;
    private Collection<Entity> localList = null; // probably an ArrayList

    public Tile(boolean wall, Image img){
        isWall = wall;
        tileImg = img; 
    }
    
    public void setIsWall(boolean wall){
        isWall = wall;
    }
    
    public boolean getIsWall(){
        return isWall;
    }
    
    public void setImage(Image img){
        tileImg = img;
    }
    
    public Image getImage(){
        return tileImg;
    }
    
}
