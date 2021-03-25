package com.shrikant.spring.hellospring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrikant.spring.hellospring.models.Paths;
import com.shrikant.spring.hellospring.services.HealthzService;
import java.io.IOException;
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
  private final ObjectMapper objectMapper;

  @Autowired
  public HealthzController(HealthzService healthzService, ObjectMapper objectMapper) {
    this.healthzService = healthzService;
    this.objectMapper = objectMapper;
  }

  /**
   * Service health endpoint.
   *
   * @return String
   */
  @ResponseBody
  @RequestMapping(value = "healthz", method = RequestMethod.GET)
  public ResponseEntity<String> healthz() throws InterruptedException {

    String jsonString = "{\"paths\": " + "{\n" +
        "                \"firstName\": {\n" +
        "                  \"fieldPath\": \"document/firstName\"\n" +
        "                },\n" +
        "                \"lastName\": {\n" +
        "                  \"fieldPath\": \"document/lastName\"\n" +
        "                },\n" +
        "                \"sex\": {\n" +
        "                  \"fieldPath\": \"document/sex\"\n" +
        "                }\n" +
        "}"
        + "}";
    try {
//      JsonNode pathsNode = this.objectMapper.readTree(this.getClass().getResourceAsStream("paths.json"));
      Paths paths = this.objectMapper.readValue(jsonString, Paths.class);
      System.out.println(paths.getProperties().get("firstName").fieldPath);
      System.out.println(paths.getProperties().get("lastName").fieldPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ResponseEntity<String>(this.healthzService.healthz(), HttpStatus.OK);
  }
}
