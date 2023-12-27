package com.stmarygate.cassandra.handlers;

import com.stmarygate.coral.network.BaseChannel;
import com.stmarygate.coral.network.PacketHandler;
import com.stmarygate.coral.network.packets.server.PacketVersionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraLoginPacketHandler extends PacketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CassandraLoginPacketHandler.class);

  /**
   * Create a new packet handler.
   *
   * @param channel The channel from which the packet handler was created.
   */
  public CassandraLoginPacketHandler(BaseChannel channel) {
    super(channel);
  }

  /**
   * Handle the version result packet.
   *
   * @param packet The version result packet to handle.
   */
  public void handlePacketVersionResult(PacketVersionResult packet) {
    LOGGER.info(
        "VersionResultPacket received. Version result: {}",
        packet.isAccepted() ? "accepted" : "rejected");
  }
}
