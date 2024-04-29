package com.stmarygate.cassandra.game;

import com.stmarygate.cassandra.game.database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;

import java.awt.*;

public class GameApplication extends Application {

  @Getter
  private static Stage primaryStage;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    primaryStage = stage;
    DatabaseManager.initialize();

    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.setResizable(false);
    primaryStage.getIcons().add(new Image(
            GameApplication.class.getClassLoader().getResource("img/icon.png").toString()));

    loadFonts();
    showMainPage();

    if (Taskbar.isTaskbarSupported()) {
      Taskbar taskbar = Taskbar.getTaskbar();
      if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        java.awt.Image dockIcon = defaultToolkit.getImage(GameApplication.class.getResource("/img/icon.png"));
        taskbar.setIconImage(dockIcon);
      }
    }

    primaryStage.show();
  }

  public static void loadFonts() {
    Font.loadFont(GameApplication.class.getClassLoader().getResource("fonts/retrogaming.ttf").toExternalForm(),
            30);
  }

  public static void showSettingsPage() {
    try {
      Parent root = FXMLLoader.load(GameApplication.class.getClassLoader().getResource("fxml/Settings.fxml"));
      Scene scene = new Scene(root, 1060, 600);
      scene.getStylesheets().add(GameApplication.class.getClassLoader().getResource("css/Main" +
              ".css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate - Settings");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showMainPage() {
    try {
      Parent root = FXMLLoader.load(GameApplication.class.getClassLoader().getResource("fxml/Main.fxml"));
      Scene scene = new Scene(root, 1060, 600);
      scene.getStylesheets().add(GameApplication.class.getClassLoader().getResource("css/Main.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
