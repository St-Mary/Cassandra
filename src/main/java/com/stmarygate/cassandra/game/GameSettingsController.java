package com.stmarygate.cassandra.game;

import com.stmarygate.cassandra.game.database.DatabaseManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class GameSettingsController implements Initializable {
  public TextField serverUrl;
  public TextField serverPort;
  public TextField username;
  public PasswordField password;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    serverUrl.setText(DatabaseManager.queryResult("SELECT server_url FROM settings"));
  }

  public void handleLeaveBtn() {
    System.exit(0);
  }

  public void handleSettingsBtn() throws IOException {
    GameApplication.showSettingsPage();
    GameApplication.getPrimaryStage().show();
  }

  public void handleSaveBtn(MouseEvent mouseEvent) {}

  public void handleCancelBtn(MouseEvent mouseEvent) {
    GameApplication.showMainPage();
    GameApplication.getPrimaryStage().show();
  }
}
