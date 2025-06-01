package ai.someexamplesof.mcpclient.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

// @Service
public class DateTimeTools {

    private static final Log logger = LogFactory.getLog(DateTimeTools.class);
    
    @Tool(name="getCurrentDateTime", description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        logger.info("tool getCurrentDateTime called");
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(name="setAlarm", description = "Set a user alarm for the given time, provided in ISO-8601 format")
    public void setAlarm(String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        logger.info("Alarm set for " + alarmTime);
    }

}
