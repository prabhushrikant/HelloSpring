import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.shrikant.spring.hellospring.caches.CachingManager;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

public class CachingManagerTest {

  CachingManager cachingManager;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
      @Override
      public String load(String s) throws Exception {
        return s + UUID.randomUUID().toString().substring(0, 6);
      }
    };

    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(Duration.ofSeconds(30))
        .maximumSize(5)
        .ticker(Ticker.systemTicker())
        .build(cacheLoader);

    cachingManager = new CachingManager(cache);
  }

  private void showCacheContents(List<String> cacheKeys) {
    System.out.println("Showing cache contents:");
    ImmutableMap<String, String> map = cachingManager.getCache().getAllPresent(cacheKeys);
    for (Entry<String, String> entry : map.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  private void showCacheContents(Map<String, String> cacheContentsMap) {
    for (Entry<String, String> entry : cacheContentsMap.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }

  @Test
  public void test_loadingcache_doesnt_load_automatically_after_eviction() throws InterruptedException, ExecutionException {
    for (int i = 0; i < 5; i++) {
      String key = "Shrikant" + i;
      cachingManager.getUserDetails(key);
    }
    Assertions.assertEquals(5, cachingManager.getCache().size());
    System.out.println("Showing cache contents before: " + cachingManager.getCache().asMap().size());
    showCacheContents(cachingManager.getCache().asMap());
    Thread.sleep(60000);
    System.out.println("Showing cache contents after: " + cachingManager.getCache().asMap().size());
    showCacheContents(cachingManager.getCache().asMap());
    System.out.println("Get an entry value: " + cachingManager.getCache().getIfPresent("Shrikant0"));
    System.out.println("Get an entry value: " + cachingManager.getCache().get("Shrikant0"));
    Assertions.assertEquals(1, cachingManager.getCache().size());
  }
}
