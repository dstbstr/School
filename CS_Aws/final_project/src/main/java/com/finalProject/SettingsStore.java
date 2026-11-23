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

    public Settings.User getUserSettings() {
        Path userSettingsPath = getSaveRootPath().resolve("userSettings.json");
        
        if(userSettingsPath.toFile().exists()) {
            return loadSettings(userSettingsPath, Settings.User.class);
        }

        var settings = defaultUserSettings();
        saveSettings(settings, userSettingsPath);
        return settings;
    }

    public void saveUserSettings(Settings.User settings) {
        Path userSettingsPath = getSaveRootPath().resolve("userSettings.json");
        saveSettings(settings, userSettingsPath);
    }

    public Settings.Assignment getAssignmentSettings(Path assignmentPath) {
        Path settingsDir = assignmentPath.resolve("settings.json");
        if(settingsDir.toFile().exists()) {
            return loadSettings(settingsDir, Settings.Assignment.class);
        }

        var settings = defaultAssignmentSettings(assignmentPath);
        saveSettings(settings, settingsDir);
        return settings;
    }

    public void saveAssignmentSettings(Settings.Assignment settings, Path assignmentPath) {
        Path settingsDir = assignmentPath.resolve("settings.json");
        saveSettings(settings, settingsDir);
    }

    public Settings.Eval getEvalSettings() {
        Path evalSettingsPath = getSaveRootPath().resolve("evalSettings.json");
        if(evalSettingsPath.toFile().exists()) {
            return loadSettings(evalSettingsPath, Settings.Eval.class);
        }

        var settings = defaultEvalSettings();
        saveSettings(settings, evalSettingsPath);
        return settings;
    }

    public void saveEvalSettings(Settings.Eval settings) {
        Path evalSettingsPath = getSaveRootPath().resolve("evalSettings.json");
        saveSettings(settings, evalSettingsPath);
    }

    private <T> T loadSettings(Path settingsPath, Class<T> clazz) {
        try {
            return mapper.readValue(settingsPath.toFile(), clazz);
        } catch(Exception ex) {
            throw new RuntimeException("Failed to read settings from " + settingsPath, ex);
        }
    }

    private <T> void saveSettings(T settings, Path settingsPath) {
        try {
            Files.createDirectories(settingsPath.getParent());
            mapper.writeValue(settingsPath.toFile(), settings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save settings to " + settingsPath, e);
        }
    }

    private Settings.User defaultUserSettings() {
        String javaHome = System.getenv("JAVA_HOME");
        if(javaHome != null && !javaHome.isBlank()) {
            javaHome = javaHome + "\\bin";
        } else {
            String javaHomeProp = System.getProperty("java.home");
            if(javaHomeProp != null && !javaHomeProp.isBlank()) {
                javaHome = javaHomeProp + "\\bin";
            } else {
                javaHome = "C:\\Program Files\\Java\\jdk-25\\bin";
            }
        }

        String jarName = "junit-platform-console-standalone-6.2.0-M1.jar";
        String junitPath = "";
        try (var input = SettingsStore.class.getResourceAsStream("/" + jarName)) {
            if (input == null) {
                throw new RuntimeException("Failed to find " + jarName + " in resources");
            }

            Path toolsPath = getSaveRootPath().resolve("tools");
            Files.createDirectories(toolsPath);
            Path jarPath = toolsPath.resolve(jarName);
            if (!Files.exists(jarPath)) {
                Files.copy(input, jarPath);
            }
            junitPath = jarPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to check for " + jarName + " in resources", e);
        }

        return new Settings.User(
            javaHome,
            junitPath,
            "",
            0
        );
    }

    private Settings.Assignment defaultAssignmentSettings(Path assignmentPath) {
        return new Settings.Assignment(
            assignmentPath.resolve("Tester.java").toString(),
            assignmentPath.resolve("Provided.java").toString(),
            assignmentPath.resolve("AiSubmissions").toString(),
            null, // grading prompt
            null, // similarity prompt
            assignmentPath.resolve("Results").toString(),
            Duration.ofSeconds(10), // compile timeout
            Duration.ofSeconds(20) // test timeout
        );
    }

    private Settings.Eval defaultEvalSettings() {
        return new Settings.Eval(
            true,  // localPlagDetect
            false,  // remotePlagDetect
            true,  // localAiDetect
            false,  // remoteAiDetect
            true,   // testRun
            false   // remoteAiScore
        );
    }

    private Path getSaveRootPath() {
        String localAppData = System.getenv("LOCALAPPDATA");
        if(localAppData == null || localAppData.isBlank()) {
            localAppData = ".";
        }
        return Path.of(localAppData, "drandall-auto-grader");
    }
}
