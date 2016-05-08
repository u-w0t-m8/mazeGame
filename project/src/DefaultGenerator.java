import java.awt.Image;

public class DefaultGenerator implements GridGenerator {

    public DefaultGenerator() {
        // empty
    }
    
    @Override
    public Grid generate(int sx, int sy, Difficulty diff) {
        Tile[][] terrain = new Tile[sx][sy];
        Image imgBlank = ImageCache.getImage("tile_blank");
        Image imgWall = ImageCache.getImage("tile_wall");
        for (int y = 0; y < sy; y++){
            for (int x = 0; x < sx; x++){
                if (x == 0 || x == sx || y == 0 || y == sy){
                    terrain[x][y] = new Tile(true, imgWall);
                } else {
                    terrain[x][y] = new Tile(false, imgBlank);
                }
            }
        }
        Grid g = new Grid(terrain);
        return g;
    }
    
}
