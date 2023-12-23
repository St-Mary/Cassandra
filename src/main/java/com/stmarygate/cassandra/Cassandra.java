package com.stmarygate.cassandra;

import com.stmarygate.cassandra.handlers.LoginPacketHandler;
import com.stmarygate.cassandra.utils.CLI;
import com.stmarygate.cassandra.utils.ConsoleWindow;
import com.stmarygate.common.network.BaseChannel;
import com.stmarygate.common.network.BaseInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.SocketAddress;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cassandra {
  private static final Logger LOGGER = LoggerFactory.getLogger(Cassandra.class);
  private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
  @Getter private static final BaseChannel baseChannel = new BaseChannel(LoginPacketHandler.class);
  private static final BaseInitializer baseInitializer = new BaseInitializer(baseChannel);

  public static void main(String[] args) {
    ConsoleWindow.printHeader();
    start(ConsoleWindow.getAddress());
  }

  /**
   * Start the Cassandra client.
   *
   * @param address The address of the Luna server to connect to.
   */
  public static void start(SocketAddress address) {
    long time = System.currentTimeMillis();

    Bootstrap b = new Bootstrap();
    configureBootstrap(b);

    try {
      ChannelFuture f = b.connect(address).sync();
      LOGGER.info("Time start: " + (System.currentTimeMillis() - time) + "ms");
      CLI.start();
      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      workerGroup.shutdownGracefully();
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
