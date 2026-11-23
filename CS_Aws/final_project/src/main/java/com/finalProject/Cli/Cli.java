package com.finalProject.Cli;
import com.finalProject.Gui;

import javafx.application.Application;
import picocli.CommandLine.Command;

@Command(name = "auto-grader", mixinStandardHelpOptions = true, subcommands = {EvalCommand.class, SettingCommand.class})
public class Cli implements Runnable {
    @Override
    public void run() {
        Application.launch(Gui.class);
    }
}
