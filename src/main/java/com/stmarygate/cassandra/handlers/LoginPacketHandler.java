package com.stmarygate.cassandra.handlers;

import com.stmarygate.common.network.BaseChannel;
import com.stmarygate.common.network.PacketHandler;
import com.stmarygate.common.network.packets.server.PacketVersionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPacketHandler extends PacketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginPacketHandler.class);

  /**
   * Create a new packet handler.
   *
   * @param channel The channel from which the packet handler was created.
   */
  public LoginPacketHandler(BaseChannel channel) {
    super(channel);
  }

  /**
   * Handle the version result packet.
   *
   * @param packet The version result packet to handle.
   */
  public void handlePacketVersionResult(PacketVersionResult packet) {
    LOGGER.info(
        "CLIENT - VersionResultPacket received from server. Version result: {}",
        packet.isAccepted() ? "accepted" : "rejected");
  }
}
