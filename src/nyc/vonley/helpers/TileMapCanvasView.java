package nyc.vonley.helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import nyc.vonley.contracts.CanvasHandlerReferenceInterface;
import nyc.vonley.contracts.CanvasImageReference;
import nyc.vonley.models.Point;
import nyc.vonley.models.Tile;
import nyc.vonley.models.TileSet;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TileMapCanvasView extends BaseCanvasView {

    private CanvasHandlerReferenceInterface canvasHandler;
    private CanvasImageReference imageReference;

    //<Position, Value>
    private TileSet.LongMap tiles = new TileSet.LongMap();

    public TileMapCanvasView(Canvas canvas) {
        super(canvas);
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            double spx = e.getX();
            double spy = e.getY();
            if (canvasHandler != null) {
                int selectedIndex = canvasHandler.getReference().getSelectedIndex();
                BufferedImage image = imageReference.getImage(selectedIndex);
                int spx_remainder = (int) (spx % tile_width);
                int spy_remainder = (int) (spy % tile_height);
                int real_column = (int) (spx - spx_remainder);
                int real_row = (int) (spy - spy_remainder);
                long pointKey = Point.toLong(real_column, real_row);
                int tileGridXOffset = image.getTileGridXOffset();
                int tileGridYOffset = image.getTileGridYOffset();
                long valueKey = Tile.toLong(Math.abs(tileGridXOffset), Math.abs(tileGridYOffset), image.getWidth(), image.getHeight());
                tiles.remove(pointKey);
                tiles.put(pointKey, valueKey);
                canvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(image, null), real_column, real_row);
            }
        }
        if (e.getEventType() == MouseEvent.MOUSE_ENTERED_TARGET) {
            mode = MODE_ENTERED;
            System.out.println("ENTERED TARGET");
        }
        if (e.getEventType() == MouseEvent.MOUSE_EXITED_TARGET) {
            mode = MODE_EXITED;
            System.out.println("EXITED TARGET");
        }
        if (e.getEventType() == MouseEvent.MOUSE_ENTERED) {
            mode = MODE_ENTERED;
            System.out.println("ENTERED");
        }
        if (e.getEventType() == MouseEvent.MOUSE_EXITED) {
            mode = MODE_EXITED;
            System.out.println("EXITED");
        }
    }

    public TileSet.LongMap getTiles() {
        return tiles;
    }

    public void setImageReference(CanvasImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public void setCanvasHandlerReference(CanvasHandlerReferenceInterface tileSetHandler) {
        this.canvasHandler = tileSetHandler;
    }

    @Override
    public void load(Map<String, Object> load) {
        super.load(load);
    }

    public void setTiles(TileSet tileSet) {
        clear();
        if (tileSet == null) {
            this.tiles = new TileSet.LongMap();
        } else {
            this.tiles.clear();
        }
        this.tiles.putAll(tileSet.getTiles());

        tiles.forEach((key, value) -> {
            Point position = Point.fromLong(key);
            Tile tile = Tile.create(value);
            BufferedImage subImage = imageReference.getSubImage(
                    Math.abs(tile.getPositionX()),
                    Math.abs(tile.getPositionY()),
                    tile_width,
                    tile_height);
            canvas.getGraphicsContext2D().drawImage(
                    SwingFXUtils.toFXImage(subImage, null),
                    position.getX(),
                    position.getY());
        });
    }
}
