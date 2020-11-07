package com.helpers;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;

public class BaseCanvasView implements EventHandler<MouseEvent> {

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_SELECTED = 1;
    public static final int MODE_ENTERED = 2;
    public static final int MODE_EXITED = 3;
    public static final int MODE_RELEASED = 4;

    protected int mode = MODE_DEFAULT;

    protected Canvas canvas;
    protected double last_y;
    protected double last_x;
    protected int tile_width;
    protected int tile_height;


    public void clear() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public BaseCanvasView(Canvas canvas) {
        this.canvas = canvas;
        this.canvas.setOnMouseClicked(this);
        this.canvas.setOnMouseDragEntered(this);
        this.canvas.setOnMouseDragReleased(this);
        this.canvas.setOnMouseDragged(this);
        this.canvas.setOnMouseDragEntered(this);
        this.canvas.setOnMouseDragExited(this);
        this.canvas.setOnMouseEntered(this);
        this.canvas.setOnMouseExited(this);
    }



    @Override
    public void handle(MouseEvent event) {

    }

    public void setPixelDimension(int tile_width, int tile_height) {
        this.tile_width = tile_width;
        this.tile_height = tile_height;
    }


}
