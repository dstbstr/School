package com.finalProject.PlagDetect;
import com.finalProject.Settings;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.jplag.*;
import de.jplag.exceptions.ExitException;
import de.jplag.java.JavaLanguage;
import de.jplag.options.JPlagOptions;

public class LocalPlagDetect {
    public record BestMatch(double score, String submissionName) {}
    public record SimilarityReport(Map<String, BestMatch> highestScores) {}

    private JPlagOptions jplagOptions;

    public LocalPlagDetect(Settings.Assignment assignmentSettings, String submissionDir) {
        JavaLanguage language = new JavaLanguage();
        
        Set<File> newDir = Set.of(new File(submissionDir));
        Set<File> oldDir = Set.of();
        jplagOptions = new JPlagOptions(language, newDir, oldDir)
            .withNormalize(true);
            
        if(assignmentSettings.baseCode() != null) {
            File baseCode = new File(assignmentSettings.baseCode());
            jplagOptions = jplagOptions.withBaseCodeSubmissionDirectory(baseCode);
        }
    }

    public SimilarityReport RunSimilarity() {
        var report = new SimilarityReport(new HashMap<>());
        try {
            JPlagResult result = JPlag.run(jplagOptions);
            
            result.getAllComparisons().forEach(comp -> {
                var lhs = comp.firstSubmission().getRoot().getName();
                var rhs = comp.secondSubmission().getRoot().getName();
                var score = comp.similarity();

                var candidateLhs = new BestMatch(score, rhs);
                report.highestScores().merge(lhs, candidateLhs, (current, next) -> next.score() > current.score() ? next : current);

                var candidateRhs = new BestMatch(score, lhs);
                report.highestScores().merge(rhs, candidateRhs, (current, next) -> next.score() > current.score() ? next : current);                
            });
        } catch (ExitException e) {
            throw new RuntimeException(e);
        }
        return report;
    }
}
