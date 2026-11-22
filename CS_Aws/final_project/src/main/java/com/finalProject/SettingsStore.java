package com.finalProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SettingsStore {
    private final ObjectMapper mapper;
    public SettingsStore() {
        this.mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Settings getDefaultSettings() {
        if(getSettingsPath().toFile().exists()) {
            return loadSettings(getSettingsPath());
        }
        return writeDefaultSettings();
    }

    public void saveSettings(Settings settings, Path settingsPath) {
        Path targetPath = settingsPath == null ? getSettingsPath() : settingsPath;
        try {
            Files.createDirectories(targetPath.getParent());
            mapper.writeValue(targetPath.toFile(), settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save settings to " + targetPath, e);
        }
    }
    public Settings loadSettings(Path settingsPath) {
        try {
            return mapper.readValue(settingsPath.toFile(), Settings.class);
        } catch(Exception ex) {
            throw new RuntimeException("Failed to read settings from " + settingsPath, ex);
        }
    }
    
    private Settings writeDefaultSettings() {
        Settings settings = new Settings(
            new Settings.User(
                "C:\\Program Files\\Java\\jdk-25\\bin",
                "Z:/source/repos/School/CS_Aws/final_project/src/main/resources/junit-platform-console-standalone-6.2.0-M1.jar",
                0
            ),
            new Settings.Assignment(
                "Z:/auto_grader_assets/Hw1/Tester.java",
                "Z:/auto_grader_assets/HW1/Provided.java",
                "Z:/auto_grader_assets/Hw1/AiSubmissions",
                null,
                "Z:/auto_grader_assets/Hw1/Results",
                Duration.ofSeconds(10), // compile timeout
                Duration.ofSeconds(20) // test timeout
            ),
            new Settings.Eval(
                true,  // localPlagDetect
                false,  // remotePlagDetect
                true,  // localAiDetect
                false,  // remoteAiDetect
                true   // testRun
            )
        );

        saveSettings(settings, null);
        return settings;
    }

    private Path getSettingsPath() {
        String localAppData = System.getenv("LOCALAPPDATA");
        if(localAppData == null || localAppData.isBlank()) {
            localAppData = ".";
        }
        Path targetPath = Path.of(localAppData, "drandall-auto-grader");
        return targetPath.resolve("settings.json");
    }
}
