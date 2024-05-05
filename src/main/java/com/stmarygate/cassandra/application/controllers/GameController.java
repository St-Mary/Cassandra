package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.cache.PlayerCache;
import com.stmarygate.coral.entities.Player;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class GameController implements Initializable {
  public Label playerName;
  public Label playerLives;
  public Label playerExps;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    Player player = PlayerCache.getPlayer();
    playerName.setText(player.getUsername());
    playerLives.setText(String.valueOf(player.getHealth()));
    playerExps.setText(String.valueOf(player.getExp()));
  }
}
