package com.finalProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.finalProject.AiDetect.LocalAiDetect;
import com.finalProject.AiDetect.LocalAiDetect.AiReport;
import com.finalProject.PlagDetect.LocalPlagDetect;
import com.finalProject.PlagDetect.LocalPlagDetect.SimilarityReport;
import com.finalProject.Testing.TestResultParser;
import com.finalProject.Testing.TestRunner;

public class Evaluator implements AutoCloseable {
    public record EvalResult(
        Optional<Double> localPlagScore, 
        Optional<Double> remotePlagScore, 
        Optional<Double> localAiScore, 
        Optional<Double> remoteAiScore,
        Optional<Double> testRunScore,
        List<String> errors
    ) {
        public static EvalResult empty() {return new EvalResult(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of());}
        public EvalResult withLocalPlagScore(Optional<Double> score) {return new EvalResult(score, this.remotePlagScore, this.localAiScore, this.remoteAiScore, this.testRunScore, this.errors);}
        public EvalResult withRemotePlagScore(Optional<Double> score) {return new EvalResult(this.localPlagScore, score, this.localAiScore, this.remoteAiScore, this.testRunScore, this.errors);}
        public EvalResult withLocalAiScore(Optional<Double> score) {return new EvalResult(this.localPlagScore, this.remotePlagScore, score, this.remoteAiScore, this.testRunScore, this.errors);}
        public EvalResult withRemoteAiScore(Optional<Double> score) {return new EvalResult(this.localPlagScore, this.remotePlagScore, this.localAiScore, score, this.testRunScore, this.errors);}
        public EvalResult withTestRunScore(Optional<Double> score) {return new EvalResult(this.localPlagScore, this.remotePlagScore, this.localAiScore, this.remoteAiScore, score, this.errors);}
        public EvalResult withError(String error) {
            var updatedErrors = new ArrayList<>(errors);
            updatedErrors.add(error);
            return new EvalResult(this.localPlagScore, this.remotePlagScore, this.localAiScore, this.remoteAiScore, this.testRunScore, updatedErrors);
        }
    }
    
    private final Settings settings;
    private final ExecutorService executor;

    public Evaluator(Settings settings) {
        this.settings = settings;
        var maxThreads = settings.user().maxThreads() <= 0 ? Runtime.getRuntime().availableProcessors() : settings.user().maxThreads();
        this.executor = Executors.newFixedThreadPool(maxThreads);
    }

    @Override 
    public void close() {
        if(executor != null) {
            executor.shutdown();
        }
    }

    public CompletableFuture<Map<String, EvalResult>> evaluate(String submissionDir) {
        Map<String, EvalResult> results = new HashMap<>();
        
        if(settings.eval().localPlagDetect()) {
            var localResult = runLocalPlagDetect(submissionDir, results);
            if(settings.eval().remotePlagDetect()) {
                runRemotePlagDetect(submissionDir, localResult, results);
            }   
        }

        if(settings.eval().localAiDetect()) {
            var localResult = runLocalAiDetect(submissionDir, results);
            if(settings.eval().remoteAiDetect()) {
                runRemoteAiDetect(submissionDir, localResult, results);
            }
        }

        if(settings.eval().testRun()) {
            return runTestRun(submissionDir, results)
                .thenApply(ignored -> Map.copyOf(results));            
        }
        
        return CompletableFuture.completedFuture(Map.copyOf(results));
    }

    private SimilarityReport runLocalPlagDetect(String submissionDir, Map<String, EvalResult> outResult) {
        var detect = new LocalPlagDetect(settings.assignment(), submissionDir);
        var similarityReport = detect.RunSimilarity();
        similarityReport.highestScores().forEach((submission, bestMatch) -> {
            outResult.compute(submission, (key, current) -> (current == null ? EvalResult.empty() : current).withLocalPlagScore(Optional.of(bestMatch.score())));
        });
        return similarityReport;
    }
    
    private void runRemotePlagDetect(String submissionDir, SimilarityReport localResult, Map<String, EvalResult> outResult) {
    }
    
    private AiReport runLocalAiDetect(String submissionDir, Map<String, EvalResult> outResult) {        
        var report = new LocalAiDetect(settings.assignment(), submissionDir).DetectAi();
        report.highestScores().forEach((submission, bestMatch) -> {
            outResult.compute(submission, (key, current) -> (current == null ? EvalResult.empty() : current).withLocalAiScore(Optional.of(bestMatch.score())));
        });
        return report;
    }

    private void runRemoteAiDetect(String submissionDir, AiReport localResult, Map<String, EvalResult> outResult) {
    }
    
    private record TestOutcome(String submission, EvalResult result, String compileErrors) {}
    
    private CompletableFuture<Void> runTestRun(String submissionDir, Map<String, EvalResult> outResult) {
            TestRunner testRunner = new TestRunner(settings, new TestResultParser());
            
            List<CompletableFuture<TestOutcome>> jobs;

            try(var stream = Files.list(Path.of(submissionDir))) {
                jobs = stream
                    .filter(Files::isDirectory)
                    .map(path -> {
                        return CompletableFuture.supplyAsync(() -> testRunner.runTests(path.toString()).join(), executor)
                            .thenApply(result -> new TestOutcome(path.getFileName().toString(), EvalResult.empty().withTestRunScore(Optional.of(result.getScore())), result.compileError()));
                    }).toList();

                return CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0]))
                    .thenRun(() -> jobs.forEach(job -> {
                        TestOutcome outcome = job.join();
                        outResult.merge(outcome.submission(), outcome.result(), (current, update) -> current.withTestRunScore(update.testRunScore()));
                        if(outcome.compileErrors() != null && !outcome.compileErrors().isEmpty()) {
                            outResult.merge(outcome.submission(), outcome.result(), (current, update) -> current.withError(outcome.compileErrors()));
                        }
                    }));
            } catch(IOException e) {
                throw new CompletionException(e);
            }
    }
}
