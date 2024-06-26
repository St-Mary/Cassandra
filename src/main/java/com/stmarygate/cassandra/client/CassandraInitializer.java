package com.stmarygate.cassandra.client;

import com.stmarygate.cassandra.Constants;
import com.stmarygate.cassandra.client.codec.CassandraPacketEncoder;
import com.stmarygate.coral.network.BaseChannel;
import com.stmarygate.coral.network.BaseInitializer;
import com.stmarygate.coral.network.codec.PacketDecoder;
import com.stmarygate.coral.network.codec.PacketEncoder;
import com.stmarygate.coral.utils.SSLContextUtils;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import java.io.FileInputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Initializes a {@link SocketChannel} by configuring its {@link ChannelPipeline} with necessary */
public class CassandraInitializer extends BaseInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(CassandraInitializer.class);

  /** The {@link BaseChannel} responsible for handling business logic. */
  private final BaseChannel channel;

  public CassandraInitializer(BaseChannel channel) {
    super(channel);
    this.channel = channel;
  }

  /**
   * Initializes the given {@link SocketChannel} by configuring its {@link ChannelPipeline}. Adds a
   * {@link PacketDecoder}, {@link PacketEncoder}, and the specified {@link BaseChannel} handler.
   *
   * @param ch The {@link SocketChannel} to be initialized.
   */
  @Override
  protected void initChannel(SocketChannel ch) {
    ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast("decoder", new PacketDecoder());
    pipeline.addLast("encoder", new CassandraPacketEncoder());

    // SSL
    try {
      SSLContext sslContext =
          SSLContextUtils.createAndInitSSLContext(
              new FileInputStream("./ssl/client.jks"), Constants.getStorePass());
      SSLEngine engine = sslContext.createSSLEngine();
      engine.setUseClientMode(true);

      pipeline.addFirst("ssl", new SslHandler(engine));
    } catch (Exception e) {
      LOGGER.error("Error while initializing SSL", e);
    }

    pipeline.addLast("handler", this.channel);
  }
}
