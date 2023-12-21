package com.stmarygate.cassandra;

import com.stmarygate.cassandra.handlers.LoginPacketHandler;
import com.stmarygate.common.network.BaseChannel;
import com.stmarygate.common.network.BaseInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cassandra {
  private static final Logger LOGGER = LoggerFactory.getLogger(Cassandra.class);
  private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
  private static final BaseChannel baseChannel = new BaseChannel(LoginPacketHandler.class);
  private static final BaseInitializer baseInitializer = new BaseInitializer(baseChannel);

  public static void main(String[] args) {
    start("localhost", 8446);
  }

  /**
   * Start the Cassandra client.
   *
   * @param host The host to connect to.
   */
  public static void start(String host, int port) {
    long time = System.currentTimeMillis();

    Bootstrap b = new Bootstrap();
    configureBootstrap(b);

    try {
      ChannelFuture f = b.connect(host, port).sync();
      LOGGER.info("Time start: " + (System.currentTimeMillis() - time) + "ms");
      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Configure the bootstrap.
   *
   * @param b The bootstrap to configure.
   */
  private static void configureBootstrap(Bootstrap b) {
    b.group(workerGroup);
    b.channel(NioSocketChannel.class);
    b.handler(baseInitializer);
  }
}
