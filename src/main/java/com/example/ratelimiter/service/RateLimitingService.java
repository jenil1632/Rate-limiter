package com.example.ratelimiter.service;

import com.example.ratelimiter.config.Config;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RateLimitingService {

  private static Map<String, List<LocalDateTime>> userRequestMap;
  private static RateLimitingService instance;

  private RateLimitingService() {
    userRequestMap = new HashMap<>();
  }

  public static RateLimitingService getInstance() {
    if (instance == null) {
      instance = new RateLimitingService();
    }
    return instance;
  }

  public boolean isRequestRateLimited(String param, LocalDateTime timestamp) {
    filterOldTimestamps(param, timestamp);
    addTimeStampToMap(param, timestamp);
    boolean isRateLimited = checkForExceedingRequests(param);
    return isRateLimited;
  }

  public boolean checkForExceedingRequests(String param) {
    return userRequestMap.get(param).size() > Config.getNumberOfApiRequestsAllowed();
  }

  private void filterOldTimestamps(String param, LocalDateTime timestamp) {
    if (!userRequestMap.containsKey(param))
        return;
    List<LocalDateTime> apiCalls = userRequestMap.get(param);

    apiCalls = apiCalls.stream().filter(apiCall-> apiCall.until(timestamp, ChronoUnit.SECONDS) < Config.getWindowInterval())
        .collect(Collectors.toList());
    userRequestMap.replace(param, apiCalls);
//    for (LocalDateTime apiCall : apiCalls) {
//      System.out.println(apiCall.until(timestamp, ChronoUnit.SECONDS));
//
//      if (apiCall.until(timestamp, ChronoUnit.SECONDS) > Config.getWindowInterval()) {
//        apiCalls.remove(apiCall);
//      }
//    }
  }

  private void addTimeStampToMap(String param, LocalDateTime timestamp) {
    if (userRequestMap.containsKey(param))
      userRequestMap.get(param).add(timestamp);
    else {
      List<LocalDateTime> apiCallTimes = new ArrayList<>();
      apiCallTimes.add(timestamp);
      userRequestMap.put(param, apiCallTimes);
    }
  }

}
