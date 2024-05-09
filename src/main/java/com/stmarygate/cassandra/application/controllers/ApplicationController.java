package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.Application;
import com.stmarygate.cassandra.application.LanguageManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ApplicationController implements Initializable {

  public Button btn_play;
  public Button btn_settings;
  public Button btn_leave;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    btn_play.setText(LanguageManager.getString("Main.btn_play"));
    btn_settings.setText(LanguageManager.getString("Main.btn_settings"));
    btn_leave.setText(LanguageManager.getString("Main.btn_leave"));
  }

  public void handleLeaveBtn() {
    System.exit(0);
  }

  public void handlePlayBtn() throws IOException {
    Application.showLoadingPage();
  }

  public void handleSettingsBtn() throws IOException {
    Application.showSettingsPage();
    Application.getPrimaryStage().show();
  }
}
