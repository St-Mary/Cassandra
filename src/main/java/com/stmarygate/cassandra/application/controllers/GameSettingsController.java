package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.GameApplication;
import com.stmarygate.cassandra.application.database.DatabaseManager;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingsController implements Initializable {
  public TextField serverUrl;
  public TextField serverPort;
  public TextField username;
  public PasswordField password;
  public Label savedLabel;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    serverUrl.setText(DatabaseManager.queryResult("SELECT server_url FROM settings"));
    serverPort.setText(DatabaseManager.queryResult("SELECT server_port FROM settings"));
    username.setText(DatabaseManager.queryResult("SELECT username FROM settings"));
    password.setText(DatabaseManager.queryResult("SELECT password FROM settings"));
  }


  public void handleLeaveBtn() {
    System.exit(0);
  }

  public void handleSettingsBtn() throws IOException {
    GameApplication.showSettingsPage();
    GameApplication.getPrimaryStage().show();
  }

  public void handleSaveBtn(MouseEvent mouseEvent) {
    try {
      DatabaseManager.query("UPDATE settings SET server_url = '" + serverUrl.getText() + "'");
      DatabaseManager.query("UPDATE settings SET server_port = '" + serverPort.getText() + "'");
      DatabaseManager.query("UPDATE settings SET username = '" + username.getText() + "'");
      DatabaseManager.query("UPDATE settings SET password = '" + password.getText() + "'");
      savedLabel.setText("Settings saved.");
      savedLabel.setStyle("-fx-text-fill: green;");
      savedLabel.setVisible(true);
    } catch (Exception e) {
      savedLabel.setText("Failed to save settings.");
      savedLabel.setVisible(true);
      savedLabel.setStyle("-fx-text-fill: red;");
    }
  }

  public void handleCancelBtn(MouseEvent mouseEvent) {
    GameApplication.showMainPage();
    GameApplication.getPrimaryStage().show();
  }
}
