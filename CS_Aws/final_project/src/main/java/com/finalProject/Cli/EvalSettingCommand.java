package com.finalProject.Cli;
import com.finalProject.SettingsStore;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "eval")
public class EvalSettingCommand implements Runnable {
    @Option(names = {"--setting", "-s"}, required = true, description = "The evaluation setting to update")
    private String setting;

    @Option(names = {"--value", "-v"}, required = false, description = "The value for the evaluation setting", fallbackValue = "true")
    private Boolean enable;

    @Override
    public void run() {
        var store = new SettingsStore();
        var evalSettings = store.getEvalSettings();

        switch (setting) {
            case "localai":
                evalSettings = evalSettings.withLocalAiDetect(enable);
                store.saveEvalSettings(evalSettings);
                break;
            case "remoteai":
                evalSettings = evalSettings.withRemoteAiDetect(enable);
                store.saveEvalSettings(evalSettings);
                break;
            case "localplag":
                evalSettings = evalSettings.withLocalPlagDetect(enable);
                store.saveEvalSettings(evalSettings);
                break;
            case "remoteplag":
                evalSettings = evalSettings.withRemotePlagDetect(enable);
                store.saveEvalSettings(evalSettings);
                break;
            case "testrun":
                evalSettings = evalSettings.withTestRun(enable);
                store.saveEvalSettings(evalSettings);
                break;

            default:
                throw new IllegalArgumentException("Unknown setting: " + setting);
        }

        store.saveEvalSettings(evalSettings);
    }
}
