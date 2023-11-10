package com.ht.hoteldelluna.controllers.main.RoomManager;

import com.ht.hoteldelluna.models.Room;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomCardController implements Initializable {
    public AnchorPane root;
    @FXML
    private Button contextButton;
    @FXML
    private Label roomNameLabel;
    @FXML
    private Label roomTypeLabel;
    @FXML
    private Label roomStatusLabel;
    private final Room room;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MFXContextMenu contextMenu = new MFXContextMenu(root);
        roomNameLabel.setText(room.getName());
        roomTypeLabel.setText(room.getType().getName());
        roomStatusLabel.setText(room.getStatus().name());

        MFXContextMenuItem item1 = new MFXContextMenuItem("Item 1");
        MFXContextMenuItem item2 = new MFXContextMenuItem("Item 2");

        contextMenu.getItems().addAll(item1, item2);
        contextButton.setOnAction(event -> {
            double screenX = contextButton.localToScreen(contextButton.getLayoutBounds().getMinX(), 0).getX() + 20;
            double screenY = contextButton.localToScreen(0, contextButton.getLayoutBounds().getMinY()).getY();
            contextMenu.show(contextButton, screenX, screenY);
        });
        item1.setOnAction(event -> System.out.println("Clicked Item 1"));
        item2.setOnAction(event -> System.out.println("Clicked Item 2"));
    }

    public RoomCardController(Room room) {
        this.room = room;
    }
}
