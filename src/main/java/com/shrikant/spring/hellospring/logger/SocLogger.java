package com.shrikant.spring.hellospring.logger;

import com.shrikant.spring.hellospring.models.SocEventDetails;
import java.lang.reflect.Field;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class SocLogger {

  @Qualifier("defaultValidator")
  @Autowired
  private Validator validator;

  private static final Logger LOGGER = LoggerFactory.getLogger(SocLogger.class);

  public void logInfo(SocEventDetails socEventDetails) throws IllegalAccessException {
//    if (socEventDetails.containsKey(SocKeys.Org.toString()) && SEARCH_TESTER_AGENCY.equals(socEventDetails.get(SocKeys.Org.toString()))) {
//      LOGGER.info(socEventDetails.get(SocKeys.Msg.toString())); // writing only to application log.
//      return;
//    }
    Set<ConstraintViolation<SocEventDetails>> violations = validator.validate(socEventDetails);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException("invalid soc details", violations);
    }

    populateMDC(socEventDetails);
    LOGGER.info(socEventDetails.getMsg());
    //clearMDC(socEventDetails);
  }

  private void populateMDC(SocEventDetails socEventDetails) throws IllegalAccessException {
    Class clazz = socEventDetails.getClass();

    Field[] fields = clazz.getDeclaredFields();
    int i = 0;
    while (i < fields.length) {
      LOGGER.info("Field: " + fields[i].getName());
      try {
        Field currField = fields[i];
        currField.setAccessible(true);
        Object value = currField.get(socEventDetails);
        if (null != value) {
          LOGGER.info("Value: " + value);
        }
      } catch (SecurityException ex) {
        throw new SecurityException("Some exception");
      }//+ ", Value: " + fields[i].get(socEventDetails));
      i++;
    }
    //socEventDetails.entrySet().stream().forEach(e -> MDC.put(e.getKey(), e.getValue()));
  }
//
//  private void clearMDC(SocEventDetails socEventDetails) {
//    socEventDetails.entrySet().stream().forEach(e -> MDC.remove(e.getKey()));
//  }
}
