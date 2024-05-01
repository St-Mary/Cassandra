package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.Cassandra;
import com.stmarygate.cassandra.Utils;
import com.stmarygate.cassandra.application.GameApplication;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameApplicationController implements Initializable {

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
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
