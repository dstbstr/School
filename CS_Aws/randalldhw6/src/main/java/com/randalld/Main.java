package com.randalld;

import javafx.application.Application;

import java.util.Arrays;

import com.randalld.Commands.CliCommands;
import com.randalld.Commands.GuiCommands;

public class Main {
    public static void main(String[] args) {
        if(args.length > 1) {
            CliCommands cmd = new CliCommands(args[0], args[1], Arrays.copyOfRange(args, 2, args.length));
            cmd.run();
        } else {
            System.out.println("Extra Credit Part 1 complete");
            System.out.println("Extra Credit Part 2 complete");
            Application.launch(GuiCommands.class, args);
        }
    }
}