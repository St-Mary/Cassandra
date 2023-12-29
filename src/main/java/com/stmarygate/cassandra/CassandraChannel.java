package com.stmarygate.cassandra;

import com.stmarygate.coral.network.BaseChannel;
import com.stmarygate.coral.network.PacketHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraChannel extends BaseChannel {

  private final Logger LOGGER = LoggerFactory.getLogger(CassandraChannel.class);

  public CassandraChannel(Class<? extends PacketHandler> clazz) {
    super(clazz);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    LOGGER.info("Channel active");
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    if (ctx != null) {
      LOGGER.info("Channel inactive");
      super.session.close();
      Cassandra.reload();
      super.channelInactive(ctx);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    LOGGER.error("Exception caught", cause);
    super.exceptionCaught(ctx, cause);
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("Channel registered");
    super.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("Channel unregistered");
    super.channelUnregistered(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    LOGGER.info("Channel read: ");
    super.channelRead(ctx, msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("Channel read complete");
    super.channelReadComplete(ctx);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    LOGGER.info("User event triggered " + evt.toString());
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("Channel writability changed");
    super.channelWritabilityChanged(ctx);
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("Handler added");
    super.handlerAdded(ctx);
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    LOGGER.info("Handler removed");
    super.handlerRemoved(ctx);
  }
}
