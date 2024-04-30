package com.stmarygate.cassandra.game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class GameApplicationController implements Initializable {

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {}

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
