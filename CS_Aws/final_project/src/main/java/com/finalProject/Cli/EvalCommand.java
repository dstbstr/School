package com.finalProject.Cli;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.nio.file.Path;

import com.finalProject.Evaluator;
import com.finalProject.Settings;
import com.finalProject.SettingsStore;
import com.finalProject.Reporting.CsvWriter;

@Command(name = "eval")
public class EvalCommand implements Runnable {
    @Option(names = {"--assignment", "-a"}, required = true)
    private Path assignmentDir;
    @Option(names = {"--submission", "-s"}, required = true)
    private Path submissionDir;

    @Override
    public void run() {
        SettingsStore store = new SettingsStore();
        var settings = new Settings(store.getUserSettings(), store.getAssignmentSettings(assignmentDir), store.getEvalSettings(), store.getModelSettings());

        try(Evaluator evaluator = new Evaluator(settings);
            CsvWriter csv = new CsvWriter(settings.assignment())) {
            var results = evaluator.evaluate(submissionDir.toString()).join();
            results.forEach((submission, result) -> {
                csv.writeResult(submission, result);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
