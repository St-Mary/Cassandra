package com.stmarygate.cassandra.cache;

import com.stmarygate.coral.entities.Account;
import com.stmarygate.coral.entities.Player;

public class PlayerCache {
  private static Account account;
  private static Player player;

  public static Player getPlayer() {
    return PlayerCache.player;
  }

  public static void setPlayer(Player player) {
    PlayerCache.player = player;
  }

  public static Account getAccount() {
    return PlayerCache.account;
  }

  public static void setAccount(Account account) {
    PlayerCache.account = account;
  }
}
