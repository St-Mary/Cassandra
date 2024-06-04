package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.Application;
import com.stmarygate.cassandra.application.LanguageManager;
import com.stmarygate.cassandra.cache.Cache;
import com.stmarygate.cassandra.client.Cassandra;
import com.stmarygate.coral.entities.Player;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
  public TextField playerUUID;
  public Label playerMana;
  public Label playerAura;
  public Label playerStrength;
  public Label playerDefense;
  public Label playerSpeed;
  public Label playerStamina;
  public ImageView manaImage;
  public ImageView auraImage;
  public ImageView strengthImage;
  public ImageView defenseImage;
  public ImageView speedImage;
  public ImageView staminaImage;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Player player = Cache.getPlayer();
    playerName.setText(player != null ? player.getUsername() : "");

    initializeLevel(player);
    initializeHealth(player);
    initializeExp(player);
    initializeAdditionalInfo(player);

    startServerConnectionMonitor();
  }

  private void startServerConnectionMonitor() {
    Thread th = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(1000);
          if (!Cassandra.isConnected()) {
            Platform.runLater(Application::showServerConnectionLostPage);
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
    initializeImage(levelImage, "/img/level.png");
    initializeImage(levelImageTwo, "/img/level.png");
    playerUUID.setText("UUID: " + player.getId().toString());
    playerUUID.setFocusTraversable(false);
  }

  private void initializeExp(Player player) {
    if (player == null) return;
    playerExps.setText(player.getExp() + "/" + player.getExpToNextLevel());
    initializeImage(starImage, "/img/star.png");
    updateImageViewBasedOnPercentage(starBar, player.getExp(), player.getExpToNextLevel(), "/img/exp_bar/exp_", "/img/empty_bar.png");
  }

  private void initializeHealth(Player player) {
    if (player == null) return;
    playerLives.setText(player.getHealth() + "/" + player.getMaxHealth());
    initializeImage(heartImage, "/img/heart.png");
    updateImageViewBasedOnPercentage(healthBar, player.getHealth(), player.getMaxHealth(), "/img/health_bar/health_bar", "/img/empty_bar.png");
  }

  private void initializeAdditionalInfo(Player player) {
    if (player == null) return;
    initializeStat(player.getMana(), playerMana, manaImage, "/img/mana.png");
    initializeStat(player.getAura(), playerAura, auraImage, "/img/aura.png");
    initializeStat(player.getStrength(), playerStrength, strengthImage, "/img/strength.png");
    initializeStat(player.getDefense(), playerDefense, defenseImage, "/img/defense.png");
    initializeStat(player.getSpeed(), playerSpeed, speedImage, "/img/speed.png");
    initializeStat(player.getStamina(), playerStamina, staminaImage, "/img/stamina.png");
  }

  private void initializeStat(int statValue, Label statLabel, ImageView statImage, String imagePath) {
    initializeStat((long) statValue, statLabel, statImage, imagePath);
  }

  private void initializeStat(Long statValue, Label statLabel, ImageView statImage, String imagePath) {
    statLabel.setText(String.valueOf(statValue));
    initializeImage(statImage, imagePath);
  }

  private void initializeImage(ImageView imageView, String path) {
    imageView.setImage(new Image(GameController.class.getResourceAsStream(path)));
  }

  private void updateImageViewBasedOnPercentage(ImageView imageView, int currentValue, int maxValue, String basePath, String emptyImagePath) {
    updateImageViewBasedOnPercentage(imageView, (long) currentValue, (long) maxValue, basePath, emptyImagePath);
  }

  private void updateImageViewBasedOnPercentage(ImageView imageView, Long currentValue, Long maxValue, String basePath, String emptyImagePath) {
    double percentage = (double) currentValue / maxValue;
    String imagePath;
    if (percentage == 1) {
      imagePath = basePath + "0.png";
    } else if (percentage >= 0.75) {
      imagePath = basePath + "1.png";
    } else if (percentage >= 0.5) {
      imagePath = basePath + "2.png";
    } else if (percentage >= 0.25) {
      imagePath = basePath + "3.png";
    } else if (percentage >= 0.15) {
      imagePath = basePath + "4.png";
    } else {
      imagePath = basePath + "5.png";
    }

    if (currentValue == 0) {
      imagePath = emptyImagePath;
    }

    initializeImage(imageView, imagePath);
    imageView.setFitHeight(20);
    imageView.setPreserveRatio(true);
  }
}
