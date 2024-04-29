package com.stmarygate.cassandra;

import com.stmarygate.cassandra.game.GameApplication;
import com.stmarygate.cassandra.handlers.CassandraLoginPacketHandler;
import com.stmarygate.cassandra.utils.CLI;
import com.stmarygate.cassandra.utils.ConsoleWindow;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.ConnectException;
import java.net.SocketAddress;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cassandra {

  private static final Logger LOGGER = LoggerFactory.getLogger(Cassandra.class);

  private static EventLoopGroup workerGroup = new NioEventLoopGroup();

  @Getter
  private static CassandraChannel baseChannel =
      new CassandraChannel(CassandraLoginPacketHandler.class);

  private static CassandraInitializer baseInitializer = new CassandraInitializer(baseChannel);

  private static ChannelFuture future;

  public static void main(String[] args) {
    GameApplication.main(args);
    //ConsoleWindow.printHeader();
    //reload();
  }

  /** Reload the Cassandra client. */
  public static void reload() {
    close();
    workerGroup = new NioEventLoopGroup();
    baseChannel = new CassandraChannel(CassandraLoginPacketHandler.class);
    baseInitializer = new CassandraInitializer(baseChannel);
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
      future = b.connect(address).sync();
      LOGGER.info("Time start: " + (System.currentTimeMillis() - time) + "ms");
      CLI.startCLI();
      future.channel().closeFuture().sync();
    } catch (Exception e) {
      if (e instanceof ConnectException) {
        LOGGER.error("Error while starting Cassandra, please check the address and try again");
        reload();
      } else {
        LOGGER.error("Failed to start Cassandra");
      }
    } finally {
      close();
    }
  }

  /** Close the connection to the Luna server. */
  public static void close() {
    if (future != null) {
      try {
        future.channel().close().sync();
      } catch (InterruptedException e) {
        LOGGER.error("Error while closing channel", e);
      }
    }

    LOGGER.info("Closing connection to Luna server...");
    workerGroup.shutdownGracefully();
    LOGGER.info("Connection to Luna server closed");
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
