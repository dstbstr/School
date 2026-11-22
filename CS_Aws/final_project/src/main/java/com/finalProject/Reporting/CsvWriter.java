package com.finalProject.Reporting;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.finalProject.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvWriter implements AutoCloseable {
    public record Result(String name, double testScore, String errors) {}

    private final BufferedWriter writer;

    public CsvWriter(Settings.Assignment settings) throws IOException {
        String timestamp = DateTimeFormatter.ofPattern("MMdd_HHmmss").format(LocalDateTime.now());
        Path outDir = Path.of(settings.outDir());
        Files.createDirectories(outDir);
        Path csvPath = outDir.resolve("report-" + timestamp + ".csv");
        writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        
        writer.write("Name,TestScore,Notes");
        writer.newLine();
    }

    @Override
    public synchronized void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }

    public synchronized void writeResult(Result result) {
        try {
            writer.write(quoteCsv(result.name()) + "," + formatDouble(result.testScore()) + "," + (result.errors() != null ? quoteCsv(result.errors()) : ""));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String quoteCsv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    private static String formatDouble(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
}
