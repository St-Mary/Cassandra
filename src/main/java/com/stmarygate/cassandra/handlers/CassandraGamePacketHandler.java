package com.stmarygate.cassandra.handlers;

import com.stmarygate.cassandra.Cassandra;
import com.stmarygate.cassandra.cache.PlayerCache;
import com.stmarygate.coral.network.BaseChannel;
import com.stmarygate.coral.network.packets.PacketHandler;
import com.stmarygate.coral.network.packets.server.PacketGetPlayerInformationsResult;

/** A {@link PacketHandler} which handles all packets for the Cassandra gaming. */
public class CassandraGamePacketHandler extends PacketHandler {

  /**
   * Create a new packet handler.
   *
   * @param channel The channel from which the packet handler was created.
   */
  public CassandraGamePacketHandler(BaseChannel channel) {
    super(channel);
  }

  public void handlePacketGetPlayerInformationsResult(PacketGetPlayerInformationsResult packet) {
    Cassandra.getBaseChannel().setPacketGetPlayerInformationsResult(packet);
    PlayerCache.setPlayer(packet.getPlayer());
  }
}
