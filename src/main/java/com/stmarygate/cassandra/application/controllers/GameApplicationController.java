package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.GameApplication;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.stmarygate.cassandra.application.LanguageManager;
import com.stmarygate.cassandra.application.database.DatabaseManager;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class GameApplicationController implements Initializable {

  public Button btn_play;
  public Button btn_settings;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    btn_play.setText(LanguageManager.getString(GameApplication.getLanguage(), "Main.btn_play"));
    btn_settings.setText(LanguageManager.getString(GameApplication.getLanguage(), "Main.btn_settings"));
  }

  public void handleLeaveBtn() {
    System.exit(0);
  }

  public void handlePlayBtn() throws IOException {
    GameApplication.showLoadingPage();
  }

  public void handleSettingsBtn() throws IOException {
    GameApplication.showSettingsPage();
    GameApplication.getPrimaryStage().show();
  }
}
