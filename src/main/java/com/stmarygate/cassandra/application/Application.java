package com.stmarygate.cassandra.application;

import com.stmarygate.cassandra.application.database.DatabaseManager;
import com.stmarygate.cassandra.client.Cassandra;
import java.awt.*;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class Application extends javafx.application.Application {

  @Getter private static Stage primaryStage;
  @Getter @Setter private static String language;

  public static void main(String[] args) throws IOException {
    LanguageManager.loadLanguages();
    DatabaseManager.initialize();
    language = DatabaseManager.getLanguage();
    launch(args);
  }

  public static void loadFonts() {
    Font.loadFont(
        Application.class.getClassLoader().getResource("fonts/retrogaming.ttf").toExternalForm(),
        30);
    Font.loadFont(
        Application.class.getClassLoader().getResource("fonts/PixelOperator8.ttf").toExternalForm(),
        40);
    Font.loadFont(
        Application.class
            .getClassLoader()
            .getResource("fonts/PixelOperator8-Bold.ttf")
            .toExternalForm(),
        50);
  }

  public static void showSettingsPage() {
    try {
      Parent root =
          FXMLLoader.load(Application.class.getClassLoader().getResource("fxml/Settings.fxml"));
      Scene scene = new Scene(root, 1060, 600);
      scene
          .getStylesheets()
          .add(
              Application.class.getClassLoader().getResource("css/Main" + ".css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate - Settings");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showGamePage() {
    try {
      Parent root =
          FXMLLoader.load(Application.class.getClassLoader().getResource("fxml/Game.fxml"));
      Scene scene = new Scene(root, 1060, 600);
      scene
          .getStylesheets()
          .add(Application.class.getClassLoader().getResource("css/Main.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showLoadingPage() {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              Application.class.getClassLoader().getResource("fxml" + "/LoadingGame.fxml"));
      Scene scene = new Scene(loader.load(), 1060, 600);
      scene
          .getStylesheets()
          .add(Application.class.getClassLoader().getResource("css/Main.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate - Loading");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showMainPage() {
    try {
      Parent root =
          FXMLLoader.load(Application.class.getClassLoader().getResource("fxml/Main.fxml"));
      Scene scene = new Scene(root, 1060, 600);
      scene
          .getStylesheets()
          .add(Application.class.getClassLoader().getResource("css/Main.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void showServerConnectionLostPage() {
    try {
      FXMLLoader loader =
          new FXMLLoader(
              Application.class
                  .getClassLoader()
                  .getResource("fxml" + "/ServerConnectionLost" + ".fxml"));
      Scene scene = new Scene(loader.load(), 1060, 600);
      scene
          .getStylesheets()
          .add(Application.class.getClassLoader().getResource("css/Main.css").toExternalForm());
      primaryStage.setScene(scene);
      primaryStage.setTitle("Saint Mary's Gate - Connection closed");
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    primaryStage = stage;

    primaryStage.setOnCloseRequest(event -> System.exit(0));
    primaryStage.setResizable(false);
    primaryStage
        .getIcons()
        .add(new Image(Application.class.getClassLoader().getResource("img/icon.png").toString()));
    loadFonts();
    showMainPage();

    if (Taskbar.isTaskbarSupported()) {
      Taskbar taskbar = Taskbar.getTaskbar();
      if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        java.awt.Image dockIcon =
            defaultToolkit.getImage(Application.class.getResource("/img/icon.png"));
        taskbar.setIconImage(dockIcon);
      }
    }

    primaryStage.setOnCloseRequest(
        event -> {
          DatabaseManager.close();
          if (Cassandra.isConnected()) {
            try {
              Cassandra.close();
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          }
          System.exit(0);
        });

    primaryStage.show();
  }
}
