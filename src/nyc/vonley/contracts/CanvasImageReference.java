package nyc.vonley.contracts;

import nyc.vonley.models.Tile;

import java.awt.image.BufferedImage;

public interface CanvasImageReference {
    BufferedImage getImage(int index);
    BufferedImage getSubImage(int x, int y, int width, int height);
    BufferedImage getSubImageAtAddress(long address);
    BufferedImage getSubImageFromTile(Tile tile);
}
