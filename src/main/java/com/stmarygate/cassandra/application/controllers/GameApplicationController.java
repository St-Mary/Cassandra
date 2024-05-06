package com.stmarygate.cassandra.application.controllers;

import com.stmarygate.cassandra.application.GameApplication;
import com.stmarygate.cassandra.cache.PlayerCache;
import com.stmarygate.coral.entities.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class GameApplicationController implements Initializable {

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {}

  public void handleLeaveBtn() {
    System.exit(0);
  }

  public void handlePlayBtn() throws IOException {
    //GameApplication.showLoadingPage();
    Player p = new Player();
    p.setUsername("Player Name");
    p.setExp(100L);
    p.setLevel(1);
    p.setMana(100L);
    p.setAura(100);
    p.setStrength(100);
    p.setDefense(100);
    p.setSpeed(100);
    p.setHealth(1);
    p.setMaxHealth(100);
    p.setStamina(100L);
    p.setExpToNextLevel(200L);
    PlayerCache.setPlayer(p);
    GameApplication.showGamePage();
  }

  public void handleSettingsBtn() throws IOException {
    GameApplication.showSettingsPage();
    GameApplication.getPrimaryStage().show();
  }
}
