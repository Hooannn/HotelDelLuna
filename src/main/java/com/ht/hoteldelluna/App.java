package com.ht.hoteldelluna;
import com.ht.hoteldelluna.controllers.auth.AuthController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MFXResourcesLoader.loadURL("fxml/Authentication.fxml"));
        loader.setControllerFactory(c -> new AuthController(primaryStage));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hotel Del Luna");
        primaryStage.show();
    }

    @Override
    public void stop() {

    }
}
