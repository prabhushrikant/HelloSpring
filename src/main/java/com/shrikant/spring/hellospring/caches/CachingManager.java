package com.shrikant.spring.hellospring.caches;

import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;

public class CachingManager {

  private final LoadingCache<String, String> userCache;

  public CachingManager(LoadingCache<String, String> userCache) {
    this.userCache = userCache;
  }

  public String getUserDetails(String key) {
    try {
      return userCache.get(key);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public LoadingCache<String, String> getCache() {
    return userCache;
  }
}


