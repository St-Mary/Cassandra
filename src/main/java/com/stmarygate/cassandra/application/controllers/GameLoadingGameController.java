package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.Cassandra;
import com.stmarygate.cassandra.application.database.DatabaseManager;
import com.stmarygate.coral.network.packets.client.PacketLoginUsingCredentials;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class GameLoadingGameController implements Initializable {
  public ProgressBar progressBar;
  public Label loadingStatus;
  private LoadingTask task;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    startLoading();
  }

  public void startLoading() {
    task = createLoadingTask();

    // Bind the progress bar and label to the task
    progressBar.progressProperty().bind(task.progressProperty());
    loadingStatus.textProperty().bind(task.messageProperty());

    new Thread(task).start();
  }

  private LoadingTask createLoadingTask() {
    return new LoadingTask();
  }

  private class LoadingTask extends Task<Void> {
    @Override
    protected Void call() throws Exception {
      connectToServer();
      loginToServer();
      return null;
    }

    public void updateLoadingMessage(String message) {
      updateMessage(message);
    }

    public void updateLoadingProgress(double progress) {
      updateProgress(progress, 1);
    }
  }

  private void connectToServer() throws InterruptedException {
    task.updateLoadingMessage("Connecting to server...");
    task.updateLoadingProgress(0.1);
    Cassandra.reload();
    while (!Cassandra.isConnected()) {
      Thread.sleep(100);
    }
  }

  private void loginToServer() throws InterruptedException {
    task.updateLoadingMessage("Trying to login to server...");
    task.updateLoadingProgress(0.5);
    String username = DatabaseManager.queryResult("SELECT username FROM settings");
    String password = DatabaseManager.queryResult("SELECT password FROM settings");
    Thread.sleep(1000);
    Cassandra.getBaseChannel().sendPacket(new PacketLoginUsingCredentials(username, password));
    waitForLoginResult();
  }

  private void waitForLoginResult() throws InterruptedException {
    while (Cassandra.getBaseChannel().getPacketLoginResult() == null) {
      Thread.sleep(100);
    }

    if (!Cassandra.getBaseChannel().getPacketLoginResult().isAccepted()) {
      task.updateLoadingMessage("Login failed, please check your credentials.");
      progressBar.setVisible(false);
    } else {
      task.updateLoadingMessage("Login successful!");
      task.updateLoadingProgress(1);
    }
  }
}
