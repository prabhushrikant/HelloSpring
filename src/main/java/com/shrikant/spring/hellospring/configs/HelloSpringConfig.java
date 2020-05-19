package com.shrikant.spring.hellospring.configs;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.shrikant.spring.hellospring.aspects.TrackDependencyAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

  public static void setupTrackDependencyAspect(TelemetryClient telemetryClient, String appName) {
    TrackDependencyAspect.setTelemetryClient(telemetryClient);
    TrackDependencyAspect.setServiceName(appName);
  }
}
