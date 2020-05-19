package com.shrikant.spring.hellospring.controllers;

import com.shrikant.spring.hellospring.services.HealthzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthzController {

  private static final Logger LOGGER = LoggerFactory.getLogger(HealthzController.class);

  private final HealthzService healthzService;

  @Autowired
  public HealthzController(HealthzService healthzService) {
    this.healthzService = healthzService;
  }

  /**
   * Service health endpoint.
   *
   * @return String
   */
  @ResponseBody
  @RequestMapping(value = "healthz", method = RequestMethod.GET)
  public ResponseEntity<String> healthz() throws InterruptedException {
    return new ResponseEntity<String>(this.healthzService.healthz(), HttpStatus.OK);
  }
}
