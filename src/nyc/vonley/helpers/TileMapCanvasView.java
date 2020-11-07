package nyc.vonley.helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import nyc.vonley.contracts.CanvasHandlerReferenceInterface;
import nyc.vonley.contracts.CanvasImageReference;
import nyc.vonley.models.Point;
import nyc.vonley.models.Tile;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TileMapCanvasView extends BaseCanvasView {

    private CanvasHandlerReferenceInterface canvasHandler;
    private CanvasImageReference imageReference;

    //<Position, Value>
    private Map<Long, Long> tiles = new HashMap<>();

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
                long valueKey = Tile.toLong(image.getTileGridXOffset(), image.getTileGridYOffset(), image.getWidth(), image.getHeight());
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

    public Map<Long, Long> getTiles() {
        return tiles;
    }

    public void setImageReference(CanvasImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public void setCanvasHandlerReference(CanvasHandlerReferenceInterface tileSetHandler) {
        this.canvasHandler = tileSetHandler;
    }

}
