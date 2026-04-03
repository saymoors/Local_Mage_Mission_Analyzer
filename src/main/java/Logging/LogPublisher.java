package Logging;

import java.util.LinkedHashMap;
import java.util.Map;

public class LogPublisher {
    private final Map<String, ILogger> loggers = new LinkedHashMap<>();

    public void register(String loggerName, ILogger logger) {
        loggers.put(loggerName, logger);
    }

    public void publish(String stage, String message) {
        LogEntry logEntry = new LogEntry(stage, message);

        for(ILogger logger : loggers.values()) {
            logger.log(logEntry);
        }
    }
}
