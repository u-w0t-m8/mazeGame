import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Statically accessed image loader with buffering. Does not uncache any images
 * (and it will probably never need to).
 * 
 * Could be part of Renderer but it seems right to separate it.
 * 
 */
public class ImageCache {

    static HashMap<String, Image> cache = new HashMap<String, Image>();

    public static Image getImage(String path) {
        Image img = cache.get(path);
        if (img == null) {
            String fullPath = "asset2/"+path+".png";
            img = loadImage(fullPath);
            cache.put(path, img);
        }
        return img;
    }

    private static Image loadImage(String path) {
        BufferedImage result = null;
        try {
            File imgFile = new File(path);
            result = ImageIO.read(imgFile);
        } catch (IOException e) {
            //TODO: do something meaningful (will crash Renderer atm)
            e.printStackTrace();
        }
        return result;
    }

}
