package com.stmarygate.cassandra.client.codec;

import com.stmarygate.coral.network.codec.PacketEncoder;
import com.stmarygate.coral.network.packets.Packet;
import com.stmarygate.coral.network.packets.PacketBuffer;
import com.stmarygate.coral.network.packets.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.jetbrains.annotations.NotNull;

public class CassandraPacketEncoder extends PacketEncoder {
  /**
   * Encodes the specified {@link Packet} into raw bytes and writes them into the output* buffer.
   *
   * @param ctx The channel handler context.
   * @param msg The {@link Packet} to be encoded.
   * @param out The output buffer to write the encoded bytes into.
   * @throws RuntimeException If an exception occurs during the encoding process.
   * @see Packet#encode(PacketBuffer)
   */
  @Override
  protected void encode(
      @NotNull ChannelHandlerContext ctx, @NotNull Packet msg, @NotNull ByteBuf out)
      throws Exception {
    msg.encode(
        new PacketBuffer(out, Protocol.getInstance().getPacketId(msg), Packet.PacketAction.WRITE));
    ctx.channel().flush();
  }
}
