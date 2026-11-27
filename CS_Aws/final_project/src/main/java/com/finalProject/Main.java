package com.finalProject;

import com.finalProject.Cli.Cli;
import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.de.jplag", "error");
        System.setProperty("org.slf4j.simpleLogger.log.com.finalProject.Utils.BedrockResponseUtils", "info");
        new CommandLine(new Cli()).execute(args);
    }
}