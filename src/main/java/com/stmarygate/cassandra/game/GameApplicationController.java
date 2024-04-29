package com.stmarygate.cassandra.game;

import java.io.IOException;
import javafx.scene.input.MouseEvent;

public class GameApplicationController {

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
