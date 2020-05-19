package com.shrikant.spring.hellospring.aspects;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import java.lang.reflect.Method;
import java.util.Date;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
@Aspect
public class TrackDependencyAspect {

  private static final Logger log = LoggerFactory.getLogger(TrackDependencyAspect.class);

  private static TelemetryClient telemetryClient;
  private static String serviceName = "hello-spring";

  /**
   * Set telemetry client for AppInsights.
   */
  public static void setTelemetryClient(final TelemetryClient telemetryClient) {
    TrackDependencyAspect.telemetryClient = telemetryClient;
  }

  /**
   * Set service name for AppInsights.
   */
  public static void setServiceName(final String serviceName) {
    if (serviceName != null) {
      TrackDependencyAspect.serviceName = serviceName;
    }
  }

  private static long now() {
    return System.currentTimeMillis();
  }

  private static void trackDependency(
      final long start,
      final ResourceOwner resourceOwner,
      final ResourceType resourceType,
      final String command,
      final String statusCode,
      final boolean success) {
    if (isValidTelemetryClient()) {
      // calculate duration
      final Duration duration = new Duration(now() - start);
      // create telemetry dependency
      final RemoteDependencyTelemetry remoteDependencyTelemetry = new RemoteDependencyTelemetry();
      // Note: As both the ResourceOwner and ResourceType are 'types' they don't line up exactly
      // with the
      //       RemoteResourceTelemetry concept of type and name. Using Application Insights through
      // the portal
      //       to view the duration of the tracked method it makes more visual sense to map the type
      // to
      //       ResourceOwner and the name to ResourceType.
      remoteDependencyTelemetry.setType(resourceOwner.toString());
      remoteDependencyTelemetry.setName(resourceType.toString());
      remoteDependencyTelemetry.setCommandName(serviceName + ":" + command);
      remoteDependencyTelemetry.setDuration(duration);
      remoteDependencyTelemetry.setSuccess(success);
      remoteDependencyTelemetry.setTimestamp(new Date(start));
      if (statusCode != null) {
        remoteDependencyTelemetry.setResultCode(statusCode);
      }
      // track dependency
      telemetryClient.trackDependency(remoteDependencyTelemetry);
    }
  }

  private static boolean isValidTelemetryClient() {
    boolean result = true;
    if (telemetryClient == null) {
      log.error("TelemetryClient not set");
      result = false;
    }
    return result;
  }

  @Pointcut(value = "execution(public * com.shrikant.spring.hellospring.services.HealthzService.healthz(..))")
  public void testMethods() {
  }

  /**
   * AppInsights configuration code.
   */
  //@Around("@annotation(com.motorolasolutions.amspe.aspects.TrackDependency)")
  @Around("testMethods()")
  public Object around(final ProceedingJoinPoint joinPoint) throws Throwable {
    final long start = now();
    boolean success = false;
    String statusCode = null;
    try {
      final Object object = joinPoint.proceed();
      success = true;
      return object;
    } catch (HttpClientErrorException e) {
      statusCode = String.valueOf(e.getRawStatusCode());
      throw e;
    } finally {
      final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
      final Method method = signature.getMethod();
      final TrackDependency trackDependency = method.getAnnotation(TrackDependency.class);
      trackDependency(
          start,
          trackDependency.resourceOwner(),
          trackDependency.resourceType(),
          trackDependency.commandName(),
          statusCode,
          success);

      telemetryClient.flush();
    }
  }
}
