package nyc.vonley.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import nyc.vonley.contracts.CanvasImageReference;
import nyc.vonley.helpers.TileMapCanvasView;
import nyc.vonley.helpers.TileSetCanvasView;
import nyc.vonley.models.TileSet;
import nyc.vonley.utils.PixelParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class MainViewController implements PixelDialogController.PixelDialogHandler, CanvasImageReference {

    public Canvas map_canvas, tile_canvas;
    public MenuItem newMenuItem, openMenuItem, closeMenuItem, retileMenuItem, saveMenuItem;
    private FileChooser.ExtensionFilter
            images = new FileChooser.ExtensionFilter("Image", "*.jpg", "*.png"),
            formats = new FileChooser.ExtensionFilter("Formats", "*.json");
    private FileChooser fileChooser = new FileChooser();
    private File imageFile, jsonFile;
    private BufferedImage tilemap;
    private List<BufferedImage> tileImages;
    private Stage stage;
    private Scene dialogScene;
    private TileSetCanvasView tileSetHandler;
    private TileMapCanvasView tileMapHandler;
    private int tile_width, tile_height;

    @FXML
    public void initialize() {
        newMenuItem.setOnAction(onMenuItemClicked);
        openMenuItem.setOnAction(onMenuItemClicked);
        closeMenuItem.setOnAction(onMenuItemClicked);
        retileMenuItem.setOnAction(onMenuItemClicked);
        saveMenuItem.setOnAction(onMenuItemClicked);
    }

    protected void showDialog() throws IOException {
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

    private EventHandler<ActionEvent> onMenuItemClicked = event -> {
        MenuItem source = (MenuItem) event.getSource();
        if (source == newMenuItem) {
            fileChooser.setSelectedExtensionFilter(images);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                this.imageFile = file;
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
                try {
                    loadTemplate(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (source == closeMenuItem) {

        } else if (source == retileMenuItem) {
            if (imageFile != null) {
                try {
                    showDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (source == saveMenuItem) {
            fileChooser.setTitle("Save");
            fileChooser.getExtensionFilters().addAll(formats);
            File jsonFile = fileChooser.showSaveDialog(stage);
            try {
                FileWriter fw = new FileWriter(jsonFile);
                fw.write(saveJson());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    protected String saveJson() {
        TileSet tileSet = new TileSet();
        tileSet.setMapImage(imageFile.getName());
        tileSet.setTileWidth(tile_width);
        tileSet.setTileHeight(tile_height);
        tileSet.setTiles(tileMapHandler.getTiles());
        return new GsonBuilder().setPrettyPrinting().create().toJson(tileSet);
    }

    @Override
    public void close() {
        stage.close();
    }

    public void loadTemplate(File json) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(json));
        String stub = null;
        StringBuilder sb = new StringBuilder();
        while ((stub = br.readLine()) != null) {
            sb.append(stub);
        }
        br.close();

        TileSet tileSet = new Gson().fromJson(sb.toString(), TileSet.class);
        int tile_width = tileSet.getTileWidth();
        int tile_height = tileSet.getTileHeight();
        String imageFileString = String.format("%s/assets/sets/%s", System.getProperty("user.dir"), tileSet.getMapImage());
        imageFile = new File(imageFileString);
        tilemap = ImageIO.read(imageFile);
        submit(tile_width, tile_height);
        tileMapHandler.setTiles(tileSet);
    }

    @Override
    public void submit(int width, int height) {
        this.tile_width = width;
        this.tile_height = height;
        if(stage != null) {
            stage.close();
        }
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

    @Override
    public BufferedImage getSubImage(int x, int y, int width, int height) {
        System.out.printf("X: %s, Y: %s, WIDTH: %s, HEIGHT: %s\n", x, y, width, height);
        return tilemap.getSubimage(x, y, width, height);
    }
}
