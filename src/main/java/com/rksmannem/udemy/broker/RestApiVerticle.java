package com.rksmannem.udemy.broker;

import com.rksmannem.udemy.broker.assets.Api;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author rama
 */
public class RestApiVerticle extends AbstractVerticle {
  private static final Logger logger = LogManager.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startHttpServer(startPromise);
  }

  public void startHttpServer(Promise<Void> startPromise) {

    final Router restApi = Router.router(vertx);
    restApi.route().handler(BodyHandler.create()).failureHandler(handleFailure());
    Api.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> logger.error("HTTP server error: ", error))
      .listen(8888, http -> {
        if (http.succeeded()) {
          logger.info("HTTP server started on port 8888");
          startPromise.complete();
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private Handler<RoutingContext> handleFailure() {
    return errCtx -> {
      if (errCtx.response().ended()) {
        return;
      }
      logger.error("Route error: ", errCtx.failure());
      errCtx.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "something went wrong :(").toBuffer());
    };
  }
}
