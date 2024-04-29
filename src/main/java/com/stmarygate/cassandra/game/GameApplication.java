package com.stmarygate.cassandra.game;

import com.stmarygate.cassandra.game.database.DatabaseManager;
import java.awt.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    DatabaseManager.initialize();
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/Main.fxml"));
    primaryStage.setTitle("Saint Mary's Gate");
    Scene scene = new Scene(root);
    Font.loadFont(
        getClass().getClassLoader().getResource("fonts/retrogaming.ttf").toExternalForm(), 30);
    scene
        .getStylesheets()
        .add(getClass().getClassLoader().getResource("css/Main.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setOnCloseRequest(event -> System.exit(0));

    primaryStage.setResizable(false);

    primaryStage
        .getIcons()
        .add(new Image(getClass().getClassLoader().getResource("img/icon.png").toString()));
    if (Taskbar.isTaskbarSupported()) {
      Taskbar taskbar = Taskbar.getTaskbar();

      if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        java.awt.Image dockIcon = defaultToolkit.getImage(getClass().getResource("/img/icon.png"));
        taskbar.setIconImage(dockIcon);
      }
    }

    primaryStage.show();
  }
}
