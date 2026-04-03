package Logging.Rules;

import Logging.ILogger;
import Logging.LogEntry;

public class ConsoleLogger implements ILogger {
    @Override
    public void log(LogEntry logEntry) {
        System.out.println("[" + logEntry.getTimestamp() +
                "] " + "[" + logEntry.getStage() +
                "] " + logEntry.getMessage());
    }
}
