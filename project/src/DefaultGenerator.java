import java.awt.Image;

public class DefaultGenerator implements GridGenerator {

    public DefaultGenerator() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public Grid generate(int sx, int sy, Difficulty diff) {
        Tile[][] terrain = new Tile[sx][sy];
        Image imgBlank = ImageCache.getImage("tile_blank");
        Image imgWall = ImageCache.getImage("tile_wall");
        for (int y = 0; y < sy; y++){
            for (int x = 0; x < sx; x++){
                if (x == 0 || x == sx || y == 0 || y == sy){
                    terrain[x][y].setIsWall(true);
                    terrain[x][y].setImage(imgBlank);
                } else {
                    terrain[x][y].setIsWall(false);
                    terrain[x][y].setImage(imgWall);
                }
            }
        }
        for (int x = 0; x < sx; x++){
            terrain[x][0].isWall = true;
            terrain[x][sy].isWall = true;
        }
        for (int y = 0; y < sy; y++){
            terrain[0][y].isWall = true;
            terrain[sx][y].isWall = true;
        }
        return new Grid(terrain);
    }
    
}
