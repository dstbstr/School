package com.finalProject.Reporting;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.finalProject.Evaluator.EvalRow;
import com.finalProject.Settings.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvWriter implements AutoCloseable {
    public static final String GEN_ID = null;

    private final BufferedWriter writer;
    private boolean headerWritten = false;

    public CsvWriter(Settings.Assignment settings, String runId) throws IOException {
        if(runId == null || runId.isEmpty()) {
            runId = DateTimeFormatter.ofPattern("MMdd_HHmmss").format(LocalDateTime.now());
        }
        Path outDir = Path.of(settings.outDir());
        Files.createDirectories(outDir);
        Path csvPath = outDir.resolve("report-" + runId + ".csv");
        writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
    }

    @Override
    public synchronized void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }

    public synchronized void writeResult(String submission, EvalRow result) {
        writeHeader(result);
        try {
            writer.write(quoteCsv(submission));
            if(result.localPlagScore().isPresent()) {
                writer.write("," + formatDouble(result.localPlagScore().get()));
            }
            if(result.remotePlagScore().isPresent()) {
                writer.write("," + formatDouble(result.remotePlagScore().get()));
            }
            if(result.localAiScore().isPresent()) {
                writer.write("," + formatDouble(result.localAiScore().get()));
            }
            if(result.remoteAiScore().isPresent()) {
                writer.write("," + formatDouble(result.remoteAiScore().get()));
            }
            if(result.testRunScore().isPresent()) {
                writer.write("," + formatDouble(result.testRunScore().get()));
            }
            writer.write("," + (result.errors() != null ? quoteCsv(String.join(" ", result.errors())) : ""));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeHeader(EvalRow result) {
        if(!headerWritten) {
            try {
                writer.write("Submission");
                if(result.localPlagScore().isPresent()) {
                    writer.write(",Local Plagiarism Score");
                }
                if(result.remotePlagScore().isPresent()) {
                    writer.write(",Remote Plagiarism Score");
                }
                if(result.localAiScore().isPresent()) {
                    writer.write(",Local AI Score");
                }
                if(result.remoteAiScore().isPresent()) {
                    writer.write(",Remote AI Score");
                }
                if(result.testRunScore().isPresent()) {
                    writer.write(",Test Run Score");
                }
                writer.write(",Errors");
                writer.newLine();
                headerWritten = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String quoteCsv(String value) {
        return "\"" + value.replace("\"", "\"\"").replace("\n", " ") + "\"";
    }
    private static String formatDouble(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }
}
