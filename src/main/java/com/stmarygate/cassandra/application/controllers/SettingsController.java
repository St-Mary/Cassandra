package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.Application;
import com.stmarygate.cassandra.application.LanguageManager;
import com.stmarygate.cassandra.application.database.DatabaseManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SettingsController implements Initializable {
  public TextField serverUrl;
  public TextField serverPort;
  public TextField username;
  public PasswordField password;
  public Label savedLabel;
  public Label label_server_url;
  public Label label_server_port;
  public Label label_username;
  public Label label_password;
  public Button label_save;
  public Button label_cancel;
  public Label label_language;
  public TextField language;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    label_server_url.setText(LanguageManager.getString("Settings.server_url"));
    label_server_port.setText(LanguageManager.getString("Settings.server_port"));
    label_username.setText(LanguageManager.getString("Settings.username"));
    label_password.setText(LanguageManager.getString("Settings.password"));
    label_save.setText(LanguageManager.getString("Settings.btn_save"));
    label_cancel.setText(LanguageManager.getString("Settings.btn_cancel"));
    label_language.setText(LanguageManager.getString("Settings.language"));

    serverUrl.setText(DatabaseManager.queryResult("SELECT server_url FROM settings"));
    serverPort.setText(DatabaseManager.queryResult("SELECT server_port FROM settings"));
    username.setText(DatabaseManager.queryResult("SELECT username FROM settings"));
    password.setText(DatabaseManager.queryResult("SELECT password FROM settings"));
    language.setText(DatabaseManager.queryResult("SELECT language FROM settings"));
  }

  public void handleLeaveBtn() {
    System.exit(0);
  }

  public void handleSettingsBtn() throws IOException {
    Application.showSettingsPage();
    Application.getPrimaryStage().show();
  }

  public void handleSaveBtn(MouseEvent mouseEvent) {
    try {
      if (!language.getText().equals("fr") && !language.getText().equals("en")) {
        savedLabel.setText(LanguageManager.getString("Settings.language_error"));
        savedLabel.setVisible(true);
        savedLabel.setStyle("-fx-text-fill: red;");
        return;
      }
      DatabaseManager.query("UPDATE settings SET server_url = '" + serverUrl.getText() + "'");
      DatabaseManager.query("UPDATE settings SET server_port = '" + serverPort.getText() + "'");
      DatabaseManager.query("UPDATE settings SET username = '" + username.getText() + "'");
      DatabaseManager.query("UPDATE settings SET password = '" + password.getText() + "'");
      DatabaseManager.query("UPDATE settings SET language = '" + language.getText() + "'");
      Application.setLanguage(language.getText());
      savedLabel.setText(LanguageManager.getString("Settings.settings_saved"));
      savedLabel.setStyle("-fx-text-fill: green;");
      savedLabel.setVisible(true);
    } catch (Exception e) {
      savedLabel.setText(LanguageManager.getString("Settings.settings_failed"));
      savedLabel.setVisible(true);
      savedLabel.setStyle("-fx-text-fill: red;");
    }
  }

  public void handleCancelBtn(MouseEvent mouseEvent) {
    Application.showMainPage();
    Application.getPrimaryStage().show();
  }
}
