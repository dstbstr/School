package com.finalProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import com.finalProject.Reporting.CsvWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Objects;

import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.de.jplag", "error");
        if(args.length == 0) {
            Application.launch(Gui.class, args);
            return;
        }

        Path settingsPath = null;
        Path submissions = null;
        for(String arg : args) {
            if(arg.startsWith("--settings=")) {
                settingsPath = Path.of(arg.substring("--settings=".length()));
            } else {
                submissions = Path.of(arg);
            }
        }
        if(submissions == null) {
            throw new IllegalArgumentException("Submissions path must be specified");
        }

        SettingsStore store = new SettingsStore();
        var settings = settingsPath == null ? store.getDefaultSettings() : store.loadSettings(settingsPath);

        try(Evaluator evaluator = new Evaluator(settings);
            CsvWriter csv = new CsvWriter(settings.assignment())) {
            var results = evaluator.evaluate(submissions.toString()).join();
            results.forEach((submission, result) -> {
                csv.writeResult(submission, result);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

/*
Notes:
Need to allow user to specify java directory (consider checking JAVA_HOME for default)

*/