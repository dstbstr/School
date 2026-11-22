package com.finalProject.AiDetect;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import com.finalProject.Settings;

import de.jplag.JPlag;
import de.jplag.JPlagResult;
import de.jplag.exceptions.ExitException;
import de.jplag.java.JavaLanguage;
import de.jplag.options.JPlagOptions;

public class LocalAiDetect {
    public record AiMatch(double score, String model) {}
    public record AiReport(HashMap<String, AiMatch> highestScores) {}

    private final JPlagOptions jplagOptions;

    public LocalAiDetect(Settings.Assignment settings, String submissionDir) {
        JavaLanguage language = new JavaLanguage();

        // reverse the roles to have fewer comparisons
        Set<File> newDir = Set.of(new File(settings.aiCodeDir()));
        Set<File> oldDir = Set.of(new File(submissionDir));
        File baseCode = new File(settings.baseCode());
        jplagOptions = new JPlagOptions(language, newDir, oldDir)
            .withBaseCodeSubmissionDirectory(baseCode)
            .withNormalize(true);
    }

    public AiReport DetectAi() {
        var report = new AiReport(new HashMap<>());
        try {
            JPlagResult result = JPlag.run(jplagOptions);
            
            result.getAllComparisons().stream()
                .filter(comp -> comp.firstSubmission().isNew() != comp.secondSubmission().isNew())
                .forEach(comp -> {
                    var score = comp.similarity();
                    var id = comp.firstSubmission().isNew() ? comp.secondSubmission().getRoot().getName() : comp.firstSubmission().getRoot().getName();
                    var candidate = new AiMatch(score, id);
                    report.highestScores().merge(id, candidate, (current, next) -> next.score() > current.score() ? next : current);
                });
        } catch (ExitException e) {
            // error handling here
        }
        return report;
    }

}
