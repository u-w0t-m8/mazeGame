import java.awt.Image;

/**
 * Represents a series of images with a state to simplify Entity animations.
 * <p>
 * Since we don't use animations yet and haven't talked about their design, for
 * now this points to a single image.
 * 
 */
public class Sprite {

    public Image mImage;

    public Sprite(String imgPath) {
        mImage = ImageCache.getImage(imgPath);
    }

    public Image getCurrentImage() {
        return mImage;
    }

}
