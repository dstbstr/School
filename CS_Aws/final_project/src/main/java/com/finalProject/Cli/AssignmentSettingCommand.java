package com.finalProject.Cli;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;
import java.time.Duration;

import com.finalProject.Settings.AssignmentRegistry;
import com.finalProject.Settings.SettingsStore;

@Command(name = "assignment")
public class AssignmentSettingCommand implements Runnable {
    @Option(names = {"--assignment", "-a"}, required = true, description = "The path to the assignment directory")
    private Path assignmentPath;

    @Option(names = {"--setting", "-s"}, required = true, description = "The assignment setting to update")
    private String setting;

    @Option(names = {"--value", "-v"}, required = true, description = "The value for the assignment setting")
    private String value;

    @Override
    public void run() {
        var assignmentFile = assignmentPath.toFile();
        if(!(assignmentFile.exists() && assignmentFile.isDirectory())) throw new IllegalArgumentException("Assignment path is not a valid directory: " + assignmentPath);

        var registry = new AssignmentRegistry();
        registry.load();
        
        var assignmentId = registry.tryFindAssignmentByPath(assignmentPath); 
        if(assignmentId.isEmpty()) throw new IllegalArgumentException("Assignment not found in registry: " + assignmentPath);
        var id = assignmentId.get();
        
        var store = new SettingsStore();
        var settings = store.getAssignmentSettings(assignmentPath, id);

        switch(setting) {
            case "testerpath":
                settings = settings.withTesterPath(value);
                break;
            case "basecode":
                settings = settings.withBaseCode(value);
                break;
            case "aicodedir":
                settings = settings.withAiCodeDir(value);
                break;
            case "similarityprompt":
                settings = settings.withSimilarityPrompt(value);
                break;
            case "outdir":
                settings = settings.withOutDir(value);
                break;
            case "compiletimeout":
                settings = settings.withCompileTimeout(Duration.parse(value));
                break;
            case "testtimeout":
                settings = settings.withTestTimeout(Duration.parse(value));
                break;
            default:
                throw new IllegalArgumentException("Unknown assignment setting: " + setting);
        }

        store.saveAssignmentSettings(settings, assignmentPath, id);
    }
}
