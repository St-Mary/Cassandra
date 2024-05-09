package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.Constants;
import com.stmarygate.cassandra.Utils;
import com.stmarygate.cassandra.application.Application;
import com.stmarygate.cassandra.application.database.DatabaseManager;
import com.stmarygate.cassandra.cache.PlayerCache;
import com.stmarygate.cassandra.client.Cassandra;
import com.stmarygate.cassandra.client.handlers.CassandraGamePacketHandler;
import com.stmarygate.coral.network.packets.client.PacketGetPlayerInformations;
import com.stmarygate.coral.network.packets.client.PacketLoginUsingCredentials;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingGameController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoadingGameController.class);
  public ProgressBar progressBar;
  public Label loadingStatus;
  public Button cancelButton;

  public void initialize() {
    startLoadingTask();
  }

  private void startLoadingTask() {
    LoadingTask task = new LoadingTask();
    progressBar.progressProperty().bind(task.progressProperty());
    loadingStatus.textProperty().bind(task.messageProperty());

    task.setOnFailed(event -> progressBar.setVisible(false));

    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
  }

  public void cancelLoading(MouseEvent mouseEvent) {
    try {
      Cassandra.setMustBeClosed(true);
      Platform.runLater(Application::showMainPage);
    } catch (RuntimeException e) {
      System.out.println("Error while closing connection : " + e);
    }
  }

  private static class LoadingTask extends Task<Void> {

    @Override
    protected Void call() throws Exception {
      connectToServer();
      this.failed();
      return null;
    }

    private void connectToServer() throws InterruptedException {
      updateMessage("Connecting to server...");
      updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, 1);

      Cassandra.reload();
      long startTime = System.currentTimeMillis();

      while (!Cassandra.isConnected()) {
        Thread.sleep(100);
        if (System.currentTimeMillis() - startTime > Constants.getMaxTimeOutConnection) {
          updateMessage("Connection timed out.");
          updateProgress(0, 1);
          break;
        }
      }

      if (Cassandra.isConnected()) {
        loginToServer();
      }
    }

    private void loginToServer() throws InterruptedException {
      updateMessage("Trying to login to server...");
      updateProgress(0.5, 1);

      String username = DatabaseManager.queryResult("SELECT username FROM settings");
      String password = DatabaseManager.queryResult("SELECT password FROM settings");

      Thread.sleep(1000);
      Cassandra.getBaseChannel().sendPacket(new PacketLoginUsingCredentials(username, password));
      waitForLoginResult();
    }

    private void waitForLoginResult() throws InterruptedException {
      long startTime = System.currentTimeMillis();

      while (Cassandra.getBaseChannel().getPacketLoginResult() == null) {
        Thread.sleep(100);
        if (System.currentTimeMillis() - startTime > 15000) {
          updateMessage("Login timed out, trying again.");
          Thread.sleep(1000);
          loginToServer();
          break;
        }
      }

      if (Cassandra.getBaseChannel().getPacketLoginResult() != null
          && Cassandra.getBaseChannel().getPacketLoginResult().isAccepted()) {

        updateMessage("Login successful!");
        Cassandra.getBaseChannel()
            .setHandler(new CassandraGamePacketHandler(Cassandra.getBaseChannel()));
        getUserInformation();
      } else {
        updateMessage("Login failed, please check your credentials.");
      }
    }

    private void getUserInformation() throws InterruptedException {
      LOGGER.info("Getting user information...");
      updateMessage("Getting user information...");

      updateProgress(0.7, 1);

      long startTime = System.currentTimeMillis();

      try {
        Cassandra.getBaseChannel()
            .sendPacket(new PacketGetPlayerInformations(PlayerCache.getAccount().getId()));
        LOGGER.info("Sent packet to get player informations");
      } catch (Exception e) {
        e.printStackTrace();
      }

      while (Cassandra.getBaseChannel().getPacketGetPlayerInformationsResult() == null) {
        try {
          Thread.sleep(100);
          if (System.currentTimeMillis() - startTime > 25000) {
            updateMessage("Failed to get user information.");
            break;
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      if (Cassandra.getBaseChannel().getPacketGetPlayerInformationsResult() != null) {
        PlayerCache.setPlayer(
            Cassandra.getBaseChannel().getPacketGetPlayerInformationsResult().getPlayer());
        updateMessage("Done!");
        updateProgress(1, 1);
        Utils.wait(1000);

        if (Cassandra.isMustBeClosed()) {
          Cassandra.close();
          return;
        }

        if (Cassandra.isConnected() && PlayerCache.getPlayer() != null) {
          Platform.runLater(Application::showGamePage);
        }
      }
    }
  }
}
