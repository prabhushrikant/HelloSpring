package com.shrikant.spring.hellospring.configs;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.shrikant.spring.hellospring.aspects.TrackDependencyAspect;
import com.shrikant.spring.hellospring.filters.Slf4jMdcFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class HelloSpringConfig {

  public static final String APP_INSIGHTS_KEY = "<your app insight instrumentation key goes here>";

  private final TelemetryClient telemetryClient = new TelemetryClient();

  @Bean
  public TelemetryClient telemetryClient() {
    return telemetryClient;
  }

  /**
   * Get the telemetry instrumentation key from the environment.
   *
   * @return instrumentation key from the environment.
   */
  @Bean
  public String telemetryConfig(@Value("${spring.application.name: hello-spring}") String appName) {
    final String telemetryKey = APP_INSIGHTS_KEY;
    final TelemetryConfiguration telemetryConfiguration = new TelemetryConfiguration();
    telemetryConfiguration.getActive().setInstrumentationKey(telemetryKey);
    telemetryConfiguration.getActive().setTrackingIsDisabled(false);
    telemetryConfiguration.setRoleName(appName);
    setupTrackDependencyAspect(telemetryClient, appName);
    return telemetryKey;
  }

  @Bean
  public FilterRegistrationBean<Slf4jMdcFilter> mdc() {
    FilterRegistrationBean registration = new FilterRegistrationBean<Slf4jMdcFilter>();
    registration.setFilter(new Slf4jMdcFilter());
    registration.addUrlPatterns("/*");
    registration.setName("Slf4jMdcFilter");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);

    return registration;
  }

  public static void setupTrackDependencyAspect(TelemetryClient telemetryClient, String appName) {
    TrackDependencyAspect.setTelemetryClient(telemetryClient);
    TrackDependencyAspect.setServiceName(appName);
  }
}
