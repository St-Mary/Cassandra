package com.stmarygate.cassandra.cache;

import com.stmarygate.coral.entities.Account;
import com.stmarygate.coral.entities.Player;

public class Cache {
  private static Account account;
  private static Player player;

  public static Player getPlayer() {
    return Cache.player;
  }

  public static void setPlayer(Player player) {
    Cache.player = player;
  }

  public static Account getAccount() {
    return Cache.account;
  }

  public static void setAccount(Account account) {
    Cache.account = account;
  }
}
