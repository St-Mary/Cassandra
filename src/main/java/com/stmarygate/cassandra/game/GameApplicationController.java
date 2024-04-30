package com.stmarygate.cassandra.game;

import com.stmarygate.cassandra.game.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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

  public void handleSettingsBtn() throws IOException {
    GameApplication.showSettingsPage();
    GameApplication.getPrimaryStage().show();
  }

  public void handleSaveBtn(MouseEvent mouseEvent) {
  }

  public void handleCancelBtn(MouseEvent mouseEvent) {
    GameApplication.showMainPage();
    GameApplication.getPrimaryStage().show();
  }
}
