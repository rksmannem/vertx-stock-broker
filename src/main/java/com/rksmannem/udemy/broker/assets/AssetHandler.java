package com.rksmannem.udemy.broker.assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author rama
 */
public class AssetHandler implements Handler<RoutingContext> {

  private static final Logger logger = LogManager.getLogger(AssetHandler.class);

  @Override
  public void handle(final RoutingContext context) {
    final JsonArray response = new JsonArray();
    Api.ASSETS.stream().map(Asset::new).forEach(response::add);
    logger.info("Path {} responds with {}", context.normalizedPath(), response.encode());

    artificialSleep(context);

    context.response()
      .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .putHeader("my-header", "my-value")
      .end(response.toBuffer());
  }


  /**
   * used for load testing
   * @param context routing context
   */
  private void artificialSleep(final io.vertx.ext.web.RoutingContext context) {
    try {
      final int random = ThreadLocalRandom.current().nextInt(100, 300);
      if (random % 2 == 0) {
        Thread.sleep(random);
        context.response()
          .setStatusCode(500)
          .end("Sleeping...");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
