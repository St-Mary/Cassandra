package com.stmarygate.cassandra.client;

import com.stmarygate.cassandra.application.Application;
import com.stmarygate.cassandra.application.database.DatabaseManager;
import com.stmarygate.cassandra.client.handlers.CassandraLoginPacketHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
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
  private static Thread clientThread;
  @Getter @Setter private static boolean mustBeClosed = false;

  public static void main(String[] args) throws IOException {
    Application.main(args);
  }

  /** Reload the Cassandra client. */
  public static void reload() throws InterruptedException {
    close();
    String address = DatabaseManager.queryResult("SELECT server_url FROM settings");
    String port = DatabaseManager.queryResult("SELECT server_port FROM settings");
    LOGGER.info("Starting Cassandra client... " + address + ":" + port);

    clientThread =
        new Thread(
            () -> {
              try {
                start(new InetSocketAddress(address, Integer.parseInt(port)));
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });
    clientThread.setName("CassandraClient");
    clientThread.start();
  }

  /**
   * Start the Cassandra client.
   *
   * @param address The address of the Luna server to connect to.
   */
  public static void start(SocketAddress address) throws InterruptedException {
    workerGroup = new NioEventLoopGroup();
    baseChannel = new CassandraChannel(CassandraLoginPacketHandler.class);
    baseInitializer = new CassandraInitializer(baseChannel);
    long time = System.currentTimeMillis();

    Bootstrap b = new Bootstrap();
    configureBootstrap(b);

    try {
      LOGGER.info("Connecting to Luna at " + address);
      future = b.connect(address).sync();

      LOGGER.info("Time start: " + (System.currentTimeMillis() - time) + "ms");
      future.channel().closeFuture().sync();
    } catch (Exception e) {
      LOGGER.error("Failed to start Cassandra");

    } finally {
      close();
    }
  }

  /**
   * Check if the Cassandra client is connected to the Luna server.
   *
   * @return True if the client is connected, false otherwise.
   */
  public static boolean isConnected() {
    return future != null && future.channel().isActive();
  }

  /** Close the connection to the Luna server. */
  public static void close() throws InterruptedException {
    if (future != null) {
      try {
        future.channel().close().sync();
      } catch (InterruptedException e) {
        LOGGER.error("Error while closing channel", e);
      }
    }

    LOGGER.info("Closing connection to Luna server...");
    workerGroup.shutdownGracefully();
    future = null;
    if (clientThread != null) clientThread.interrupt();
    LOGGER.info("Connection to Luna server closed");

    if (!Application.getPrimaryStage().getTitle().equals("Saint Mary's Gate - Loading")
        && !Application.getPrimaryStage().getTitle().equals("Saint Mary's Gate")) {
      Platform.runLater(Application::showServerConnectionLostPage);
    }

    mustBeClosed = false;
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
