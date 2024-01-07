package com.stmarygate.cassandra.handlers;

import com.stmarygate.coral.network.BaseChannel;
import com.stmarygate.coral.network.packets.PacketHandler;
import com.stmarygate.coral.network.packets.server.PacketLoginResult;
import com.stmarygate.coral.network.packets.server.PacketVersionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A {@link PacketHandler} which handles all packets for the Cassandra login. */
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
  @Override
  public void handlePacketVersionResult(PacketVersionResult packet) {
    if (!packet.isAccepted()) {
      LOGGER.error("Version mismatch!");
      this.getChannel().getSession().close();
    }
  }

  /**
   * Handle login result packet.
   *
   * @param packet The login result packet to handle.
   */
  @Override
  public void handlePacketLoginResult(PacketLoginResult packet) {
    if (!packet.isAccepted()) {
      LOGGER.error("Login failed with code " + packet.getCode());
      return;
    }

    LOGGER.info("Login successful!");
  }
}
