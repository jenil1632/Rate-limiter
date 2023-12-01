package com.example.ratelimiter.config;

public class Config {

  private static int numberOfApiRequestsAllowed = 5;
  //private String apiName;
  private static int timeIntervalInSeconds = 10;

  public static int getNumberOfApiRequestsAllowed() {
    return numberOfApiRequestsAllowed;
  }

  public static int getWindowInterval() {
    return timeIntervalInSeconds;
  }

}
