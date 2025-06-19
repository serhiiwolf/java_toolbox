package flow.tools.toolbox.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class OperationHistory {
    private static final String LOG_FILE = "history.txt";
    private static final ArrayList<OperationHistoryEntry> history = new ArrayList<>();

    public static void log(String operation, String format, String filePath) {
        OperationHistoryEntry entry = new OperationHistoryEntry(operation, format, filePath);
        history.add(entry);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entry.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to history log: " + e.getMessage());
        }
    }

    public static String getLogFilePath() {
        return LOG_FILE;
    }
}
