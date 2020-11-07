package com.helpers;

import com.contracts.CanvasHandlerReferenceInterface;
import com.contracts.CanvasImageReference;
import com.models.Point;
import com.models.Tile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TileMapCanvasView extends BaseCanvasView {

    private CanvasHandlerReferenceInterface canvasHandler;
    private CanvasImageReference imageReference;
    private boolean selected;
    private int last_index_selected;

    //<Position, Value>
    Map<Long, Long> tiles = new HashMap<>();


    public void draw(WritableImage toFXImage) {
        canvas.getGraphicsContext2D().drawImage(toFXImage, last_x, last_y);
    }

    public TileMapCanvasView(Canvas canvas) {
        super(canvas);
    }

    public int getSelectedIndex() {
        return last_index_selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public Map<Long, Long> getTiles() {
        return tiles;
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

                long key = Point.toLong(real_column, real_row);
                long value = Tile.toLong(image.getTileGridXOffset(), image.getTileGridYOffset(), image.getWidth(), image.getHeight());
                if (tiles.containsKey(key)) {
                    Long aLong = tiles.get(key);
                    tiles.remove(key);
                }
                tiles.put(key, value);
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

    public void setImageReference(CanvasImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public void setCanvasHandlerReference(CanvasHandlerReferenceInterface tileSetHandler) {
        this.canvasHandler = tileSetHandler;
    }

}
