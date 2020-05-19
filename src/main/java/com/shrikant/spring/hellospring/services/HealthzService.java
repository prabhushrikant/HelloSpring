package com.shrikant.spring.hellospring.services;

import com.microsoft.applicationinsights.TelemetryClient;
import com.shrikant.spring.hellospring.aspects.ResourceOwner;
import com.shrikant.spring.hellospring.aspects.ResourceType;
import com.shrikant.spring.hellospring.aspects.TrackDependency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthzService {

  private final TelemetryClient telemetryClient;

  @Autowired
  public HealthzService(TelemetryClient telemetryClient) {
    this.telemetryClient = telemetryClient;
  }

  @TrackDependency(
      commandName = "test-command",
      resourceOwner = ResourceOwner.TEST_OWNER,
      resourceType = ResourceType.INTERNAL_METHOD)
  public String healthz() throws InterruptedException {
    Thread.sleep(500);
    return "Hello from Spring";
  }
}
