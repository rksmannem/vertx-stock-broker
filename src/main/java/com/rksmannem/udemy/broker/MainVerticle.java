package com.rksmannem.udemy.broker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author rama
 */
public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);

  public static void main(String[] args) {

    Vertx vertex = Vertx.vertx();
    vertex.exceptionHandler(error -> logger.error("unhandled: ", error));

    vertex.deployVerticle(new MainVerticle())
      .onFailure(err -> logger.error("fail to deploy: ", err))
      .onSuccess(id -> logger.info("deployed {} with id {}", MainVerticle.class.getSimpleName(), id));
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(
        RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(processors()))
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        logger.info("deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }

  private int processors() {
    return Math.max(1, Runtime.getRuntime().availableProcessors()* 3/4);
  }

}
