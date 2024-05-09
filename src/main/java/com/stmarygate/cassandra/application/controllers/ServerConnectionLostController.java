package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.Application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ServerConnectionLostController implements Initializable {
  public Label loadingStatus;
  public Button cancelButton;

  public void cancelLoading(MouseEvent mouseEvent) {
    Application.showMainPage();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadingStatus.setText("Connection to the server has been lost.");
  }
}
