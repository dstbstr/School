package com.finalProject.Cli;
import java.nio.file.Path;

import com.finalProject.SettingsStore;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "user") 
public class UserSettingCommand implements Runnable {
    @Option(names = {"--setting", "-s"}, required = true, description = "The user setting to update")
    private String setting;

    @Option(names = {"--value", "-v"}, required = true, description = "The value for the user setting")
    private String value;

    @Override
    public void run() {
        SettingsStore store = new SettingsStore();
        var userSettings = store.getUserSettings();

        switch(setting) {
            case "javahome":
                if(Path.of(value).toFile().exists()) {
                    userSettings = userSettings.withJavaDir(value);
                    store.saveUserSettings(userSettings);
                } else {
                    throw new IllegalArgumentException("Invalid Java directory: " + value);
                }
                break;
            case "junit":
                if(Path.of(value).toFile().exists()) {
                    userSettings = userSettings.withJunitJar(value);
                    store.saveUserSettings(userSettings);
                } else {
                    throw new IllegalArgumentException("Invalid JUnit directory: " + value);
                }
                break;
            case "maxthreads":
                try {
                    int maxThreads = Integer.parseInt(value);
                    userSettings = userSettings.withMaxThreads(maxThreads);
                    store.saveUserSettings(userSettings);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number for max threads: " + value);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown setting: " + setting);
        }

        store.saveUserSettings(userSettings);
    }
}
