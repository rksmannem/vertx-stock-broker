package com.rksmannem.udemy.broker.assets;

import io.vertx.ext.web.Router;

import java.util.Arrays;
import java.util.List;

/**
 * @author rama
 */
public class Api {

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "NFLX", "TSLA");

  public static void attach(Router parent) {
    parent.get("/assets").handler(new AssetHandler());
  }
}

