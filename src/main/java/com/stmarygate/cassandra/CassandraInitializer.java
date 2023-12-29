package com.stmarygate.cassandra;

import com.stmarygate.coral.network.BaseChannel;
import com.stmarygate.coral.network.BaseInitializer;
import com.stmarygate.coral.network.codec.PacketDecoder;
import com.stmarygate.coral.network.codec.PacketEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import javax.net.ssl.SSLEngine;

public class CassandraInitializer extends BaseInitializer {

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

    // Add packet decoding and encoding handlers
    pipeline.addLast("decoder", new PacketDecoder());
    pipeline.addLast("encoder", new PacketEncoder());

    // SSL
    try {
      SslContext sslContext =
          SslContextBuilder.forClient()
              .trustManager(getClass().getResourceAsStream("./ssl/csr.pem"))
              .build();
      SSLEngine engine = sslContext.newEngine(ch.alloc());
      engine.setUseClientMode(true);
      engine.setNeedClientAuth(false);
      // Add engine to pipeline
      pipeline.addFirst("ssl", new SslHandler(engine));
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Add the business logic handler
    pipeline.addLast("handler", this.channel);
  }
}
