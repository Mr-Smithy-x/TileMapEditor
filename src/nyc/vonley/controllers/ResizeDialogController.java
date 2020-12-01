package nyc.vonley.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class ResizeDialogController implements EventHandler<ActionEvent> {

    private ResizeDialogHandler handler;

    public void setCloseHandler(ResizeDialogHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == submitBtn){
            handler.onResize(Integer.parseInt(tPixelWidth.getText()), Integer.parseInt(tPixelHeight.getText()));
        }else if(event.getSource() == cancelBtn){
            handler.close();
        }
    }

    public interface ResizeDialogHandler {
        void close();
        void onResize(int width, int height);
    }

    public DialogPane dialogPane;
    public TextField tPixelWidth;
    public TextField tPixelHeight;
    public Button submitBtn;
    public Button cancelBtn;

    @FXML
    public void initialize(){
        submitBtn.setOnAction(this);
        cancelBtn.setOnAction(this);
    }


}
