package com.finalProject.Cli;
import picocli.CommandLine.Command;

@Command(name = "settings", subcommands = {AssignmentSettingCommand.class, EvalSettingCommand.class, UserSettingCommand.class})
public class SettingCommand implements Runnable {
    @Override
    public void run() {
        // Implement the setting update logic here
    }
}
