package com.helpers;

import com.contracts.CanvasHandlerReferenceInterface;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

public class TileSetCanvasView extends BaseCanvasView implements CanvasHandlerReferenceInterface {

    private boolean selected;
    private int index;

    public TileSetCanvasView(Canvas canvas) {
        super(canvas);
    }

    @Override
    public void handle(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            double spx = e.getX();
            double spy = e.getY();
            int columns = (int) (canvas.getWidth() / tile_width);
            int rows = (int) (canvas.getHeight() / tile_height);
            int index_col = (int) (spx / tile_width);
            int index_row = (int) (spy / tile_height);
            int index = (index_row * columns) + index_col;
            this.index = index;
            this.selected = true;
            System.out.println(index);
            mode = MODE_SELECTED;
            System.out.println("SELECTED");
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


    @Override
    public TileSetCanvasView getReference() {
        return this;
    }

    public void draw(WritableImage toFXImage) {
        canvas.getGraphicsContext2D().drawImage(toFXImage, last_x, last_y);
    }

    public int getSelectedIndex() {
        return index;
    }

    public boolean isSelected() {
        return selected;
    }
}
