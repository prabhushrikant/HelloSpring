package com.shrikant.spring.hellospring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrikant.spring.hellospring.logger.SocLogger;
import com.shrikant.spring.hellospring.models.SocEventDetails;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class SearchesController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SearchesController.class);
  private final ObjectMapper objectMapper;

  @Autowired
  SocLogger socLogger;

  @Autowired
  public SearchesController(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Service searches endpoint
   *
   * @return String
   */
  @ResponseBody
  @RequestMapping(value = "searches", method = RequestMethod.GET)
  public ResponseEntity<String> searches(
      @Pattern(regexp = "^[a-zA-Z0-9\\.]+$", message = "tenant must not contain special characters except period(.)") @RequestParam(value = "tenant", required = true) String tenant
  ) throws InterruptedException, IllegalAccessException {
    //MDC.put("ts", ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    //LOGGER.info("Showing mdc fields in log entry.");
    SocEventDetails socEventDetails = new SocEventDetails();
    socEventDetails.setMsg("Test message.");
    socEventDetails.setTS(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
    socLogger.logInfo(socEventDetails);
    return new ResponseEntity<>("found tenant: " + tenant, HttpStatus.OK);
  }
}
