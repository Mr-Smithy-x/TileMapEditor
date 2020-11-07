package nyc.vonley.contracts;

import java.awt.image.BufferedImage;

public interface CanvasImageReference {
    BufferedImage getImage(int index);
    BufferedImage getSubImage(int x, int y, int width, int height);
}
