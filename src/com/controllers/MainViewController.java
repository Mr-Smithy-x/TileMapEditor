package com.controllers;

import com.contracts.CanvasImageReference;
import com.helpers.TileMapCanvasView;
import com.helpers.TileSetCanvasView;
import com.utils.PixelParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainViewController implements PixelDialogController.PixelDialogHandler, CanvasImageReference {

    public Canvas map_canvas;

    public Canvas tile_canvas;
    public MenuItem newMenuItem;
    public MenuItem openMenuItem;
    public MenuItem closeMenuItem;
    public MenuItem retileMenuItem;
    private File file;
    private FileChooser.ExtensionFilter images = new FileChooser.ExtensionFilter("Image", "jpg", "png");
    private FileChooser.ExtensionFilter formats = new FileChooser.ExtensionFilter("Formats", "json");
    private FileChooser fileChooser = new FileChooser();
    private BufferedImage tilemap;
    private List<BufferedImage> tileImages;
    private Stage stage;
    private Scene dialogScene;
    private TileSetCanvasView tileSetHandler;
    private TileMapCanvasView tileMapHandler;
    private int tile_width;
    private int tile_height;


    public void showDialog() throws IOException {
        if (stage == null) {
            stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/pixeldialog.fxml"));
            Parent root = loader.load();
            PixelDialogController controller = loader.getController();
            dialogScene = new Scene(root);
            stage.setScene(dialogScene);
            controller.setCloseHandler(this);
        }
        if (!stage.isShowing()) {
            stage.showAndWait();
        }
    }


    public EventHandler<ActionEvent> onMenuItemClicked = event -> {
        MenuItem source = (MenuItem) event.getSource();
        if (source == newMenuItem) {
            fileChooser.setSelectedExtensionFilter(images);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                this.file = file;
                try {
                    tilemap = ImageIO.read(file);
                    showDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (source == openMenuItem) {
            fileChooser.setSelectedExtensionFilter(formats);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                this.file = file;
            }
        } else if (source == closeMenuItem) {

        } else if (source == retileMenuItem) {
            if (file != null) {
                try {
                    showDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @FXML
    public void initialize() {
        newMenuItem.setOnAction(onMenuItemClicked);
        openMenuItem.setOnAction(onMenuItemClicked);
        closeMenuItem.setOnAction(onMenuItemClicked);
        retileMenuItem.setOnAction(onMenuItemClicked);
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void submit(int width, int height) {
        this.tile_width = width;
        this.tile_height = height;
        System.out.printf("%sx%s", width, height);
        stage.close();
        tile_canvas.setHeight(tilemap.getHeight());
        tile_canvas.setWidth(tilemap.getWidth());
        if (tileImages != null) {
            tileImages.forEach(Image::flush);
            tileImages.clear();
        }
        tileImages = PixelParser.parse(tilemap, width, height);
        Paint p = Paint.valueOf("000000");
        tile_canvas.getGraphicsContext2D().setFill(p);
        tile_canvas.getGraphicsContext2D().fillRect(0, 0, tile_canvas.getWidth(), tile_canvas.getHeight());
        PixelParser.adjust(tile_canvas, tileImages, width, height);
        if (tileSetHandler == null) {
            tileSetHandler = new TileSetCanvasView(tile_canvas);
        }
        if (tileMapHandler == null) {
            tileMapHandler = new TileMapCanvasView(map_canvas);
            tileMapHandler.setCanvasHandlerReference(tileSetHandler);
            tileMapHandler.setImageReference(this);
        }

        tileMapHandler.setPixelDimension(width, height);
        tileSetHandler.setPixelDimension(width, height);
    }


    @Override
    public BufferedImage getImage(int index) {
        return tileImages.get(index);
    }
}
