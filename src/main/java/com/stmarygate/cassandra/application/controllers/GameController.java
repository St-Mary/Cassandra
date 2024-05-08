package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.GameApplication;
import com.stmarygate.cassandra.application.LanguageManager;
import com.stmarygate.cassandra.cache.PlayerCache;
import com.stmarygate.cassandra.client.Cassandra;
import com.stmarygate.coral.entities.Player;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameController implements Initializable {
  public Label playerName;
  public Label playerLives;
  public Label playerExps;
  public ImageView heartImage;
  public ImageView healthBar;
  public ImageView starImage;
  public ImageView starBar;
  public ImageView levelImage;
  public Label level;
  public ImageView levelImageTwo;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Player player = PlayerCache.getPlayer();

    playerName.setText(player != null ? player.getUsername() : "");

    initializeLevel(player);
    initializeHealth(player);
    initializeExp(player);

    Thread th =
        new Thread(
            () -> {
              while (true) {
                try {
                  Thread.sleep(1000);
                  if (!Cassandra.isConnected()) {
                    Platform.runLater(GameApplication::showServerConnectionLostPage);
                    break;
                  }
                } catch (InterruptedException e) {
                  System.out.println("Error while updating player informations : " + e);
                }
              }
            });
    th.setDaemon(true);
    th.start();
  }

  private void initializeLevel(Player player) {
    if (player == null) return;
    level.setText(LanguageManager.getString("GameMenu.level") + player.getLevel());
    levelImage.setImage(new Image(GameController.class.getResourceAsStream("/img/level.png")));
    levelImageTwo.setImage(new Image(GameController.class.getResourceAsStream("/img/level.png")));
  }

  private void initializeExp(Player player) {
    if (player == null) return;
    playerExps.setText(player.getExp() + "/" + player.getExpToNextLevel());
    starImage.setImage(new Image(GameController.class.getResourceAsStream("/img/star.png")));
    // Set the player's image based on the player's level
    double expPercentage = (double) player.getExp() / player.getExpToNextLevel();
    if (expPercentage == 1) {
      starBar.setImage(
          new Image(
              GameController.class.getResourceAsStream("/img" + "/star_bar/star_bar0" + ".png")));
    } else if (expPercentage >= 0.75) {
      starBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/exp_bar/exp_1" + ".png")));
    } else if (expPercentage >= 0.5) {
      starBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/exp_bar/exp_2" + ".png")));
    } else if (expPercentage >= 0.25) {
      starBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/exp_bar/exp_3" + ".png")));
    } else if (expPercentage >= 0.15) {
      starBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/exp_bar/exp_4" + ".png")));
    } else {
      starBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/exp_bar/exp_5" + ".png")));
    }

    if (player.getExp() == 0) {
      starBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/empty_bar" + ".png")));
    }

    starBar.setFitHeight(20);
    starBar.setPreserveRatio(true);
  }

  private void initializeHealth(Player player) {
    if (player == null) return;
    playerLives.setText(player.getHealth() + "/" + player.getMaxHealth());
    heartImage.setImage(new Image(GameController.class.getResourceAsStream("/img/heart.png")));
    // Set the player's image based on the player's level
    double healthPercentage = (double) player.getHealth() / player.getMaxHealth();
    if (healthPercentage == 1) {
      healthBar.setImage(
          new Image(
              GameController.class.getResourceAsStream(
                  "/img" + "/health_bar/health_bar0" + ".png")));
    } else if (healthPercentage >= 0.75) {
      healthBar.setImage(
          new Image(
              GameController.class.getResourceAsStream("/img/health_bar/health_bar1" + ".png")));
    } else if (healthPercentage >= 0.5) {
      healthBar.setImage(
          new Image(
              GameController.class.getResourceAsStream("/img/health_bar/health_bar2" + ".png")));
    } else if (healthPercentage >= 0.25) {
      healthBar.setImage(
          new Image(
              GameController.class.getResourceAsStream("/img/health_bar/health_bar3" + ".png")));
    } else if (healthPercentage >= 0.15) {
      healthBar.setImage(
          new Image(
              GameController.class.getResourceAsStream("/img/health_bar/health_bar4" + ".png")));
    } else {
      healthBar.setImage(
          new Image(
              GameController.class.getResourceAsStream("/img/health_bar/health_bar5" + ".png")));
    }

    if (player.getHealth() == 0) {
      healthBar.setImage(
          new Image(GameController.class.getResourceAsStream("/img/empty_bar" + ".png")));
    }

    healthBar.setFitHeight(20);
    healthBar.setPreserveRatio(true);
  }
}
