package com.shrikant.spring.hellospring.filters;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

@Order(2)
public class Slf4jMdcFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(Slf4jMdcFilter.class);

  public static final String X_REQUEST_ID_HEADER = "x-request-id";
  public static final String X_FORWARDED_FOR_HEADER = "X-Forwarded-For";
  public static final String X_PROXY_CLIENT_IP_HEADER = "Proxy-Client-IP";
  public static final String X_WL_PROXY_CLIENT_IP_HEADER = "WL-Proxy-Client-IP";

  final String MDC_SOURCE_IP_KEY = "srcIP";

  @Override
  protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
    try {
      String xRequestId = request.getHeader(X_REQUEST_ID_HEADER);
      if (StringUtils.isEmpty(xRequestId)) {
        xRequestId = UUID.randomUUID().toString().toUpperCase(Locale.ENGLISH).replace("-", "");
        LOG.debug("creating x-request-id {}", xRequestId);
      } else {
        LOG.debug("using existing x-request-id {}", xRequestId);
      }
      MDC.put(X_REQUEST_ID_HEADER, xRequestId);
      LOG.debug("adding x-request-id {}", xRequestId);
      response.setHeader(X_REQUEST_ID_HEADER, xRequestId);
      MDC.put(MDC_SOURCE_IP_KEY, getClientIP(request));
      chain.doFilter(request, response);
    } finally {
      MDC.remove(X_REQUEST_ID_HEADER);
    }
  }

  //TODO: might be wise to change it to its own filter.
  private String getClientIP(final HttpServletRequest request) {
    final String LOCALHOST_IPV4 = "127.0.0.1";
    final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    String ipAddress = request.getHeader("X-Forwarded-For");
    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("Proxy-Client-IP");
    }

    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getHeader("WL-Proxy-Client-IP");
    }

    if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
      if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
        try {
          InetAddress inetAddress = InetAddress.getLocalHost();
          ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
          LOG.warn("Error calculating client IP address.", e);
        }
      }
    }

    if (!StringUtils.isEmpty(ipAddress)
        && ipAddress.length() > 15
        && ipAddress.indexOf(",") > 0) {
      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
    }

    return ipAddress;
  }
}

