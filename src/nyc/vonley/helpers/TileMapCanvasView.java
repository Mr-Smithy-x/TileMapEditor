package nyc.vonley.helpers;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import nyc.vonley.contracts.CanvasHandlerReferenceInterface;
import nyc.vonley.contracts.CanvasImageReference;
import nyc.vonley.models.Point;
import nyc.vonley.models.Tile;
import nyc.vonley.models.TileSet;

import java.awt.image.BufferedImage;
import java.io.File;

public class TileMapCanvasView extends BaseCanvasView implements EventHandler<MouseEvent>{

    private CanvasHandlerReferenceInterface canvasHandler;
    private CanvasImageReference imageReference;
    private TileSet tileSet;

    public TileMapCanvasView(Canvas canvas, TileSet tileSet) {
        super(canvas, new File(String.format("%s/sets/%s", System.getProperty("user.dir"), tileSet.getMapImage())));
        this.tileSet = tileSet;
        setPixelDimension(tileSet.getTileWidth(), tileSet.getTileHeight());
    }

    public TileMapCanvasView(Canvas canvas, File file, int tile_width, int tile_height) {
        super(canvas, file);
        tileSet = new TileSet();
        tileSet.setMapImage(file.getName());
        tileSet.setTileWidth(tile_width);
        tileSet.setTileHeight(tile_height);
        setPixelDimension(tile_width, tile_height);
        canvas.setOnDragDetected(this);
    }

    @Override
    public void setPixelDimension(int tile_width, int tile_height) {
        super.setPixelDimension(tile_width, tile_height);
        tileSet.setTileHeight(tile_height);
        tileSet.setTileWidth(tile_width);
    }

    @Override
    public void setFile(File file) {
        super.setFile(file);
        tileSet.setMapImage(file.getName());
        tileSet.clear();
        clear();
    }

    public void drawImage(MouseEvent e){
        double spx = e.getX();
        double spy = e.getY();
        this.drawImage(spx, spy);
    }

    public void drawImage(double spx, double spy){
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
            tileSet.remove(pointKey);
            tileSet.put(pointKey, valueKey);
            canvas.getGraphicsContext2D().drawImage(SwingFXUtils.toFXImage(image, null), real_column, real_row);
        }
    }



    @Override
    public void handle(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            drawImage(e);
        }
        if(e.getEventType() == MouseEvent.MOUSE_PRESSED) {
            System.out.println("MOUSE PRESSING");
        }
        if(e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            drawImage(e);
        }
        if(e.getEventType() == MouseEvent.DRAG_DETECTED){
            canvas.startDragAndDrop();
            System.out.println("DRAG_DETECTED");
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

    public TileSet getTiles() {
        return tileSet;
    }

    public void setImageReference(CanvasImageReference imageReference) {
        this.imageReference = imageReference;
    }

    public void setCanvasHandlerReference(CanvasHandlerReferenceInterface tileSetHandler) {
        this.canvasHandler = tileSetHandler;
    }


    public void setTiles(TileSet tileSet) {
        clear();
        if (this.tileSet != null) {
            this.tileSet.clear();
        }
        this.tileSet = tileSet;
        for (Point position: tileSet.pointIterator()) {
            Tile tile = Tile.create(tileSet.get(position));
            BufferedImage subImage = imageReference.getSubImage(
                    Math.abs(tile.getPositionX()),
                    Math.abs(tile.getPositionY()),
                    tile_width,
                    tile_height
            );
            canvas.getGraphicsContext2D().drawImage(
                    SwingFXUtils.toFXImage(subImage, null),
                    position.getX(),
                    position.getY()
            );
        }
    }


    public void handle(DragEvent event) {
        System.out.println("DRAGGING");
        drawImage(event.getX(), event.getY());
    }
}
