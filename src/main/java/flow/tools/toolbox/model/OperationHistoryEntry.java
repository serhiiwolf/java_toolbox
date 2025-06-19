package flow.tools.toolbox.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OperationHistoryEntry {
    private String operation;
    private String format;
    private String filePath;
    private LocalDateTime timestamp;

    public OperationHistoryEntry(String operation, String format, String filePath) {
        this.operation = operation;
        this.format = format;
        this.filePath = filePath;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s to %s (%s)",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                operation, format, filePath);
    }
}