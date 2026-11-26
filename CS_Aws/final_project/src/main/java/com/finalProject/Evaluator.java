package com.finalProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.finalProject.AiDetect.LocalAiDetect;
import com.finalProject.AiDetect.LocalAiDetect.AiReport;
import com.finalProject.AiDetect.RemoteAiDetect;
import com.finalProject.CodeQuality.RemoteScore;
import com.finalProject.PlagDetect.LocalPlagDetect;
import com.finalProject.PlagDetect.LocalPlagDetect.SimilarityReport;
import com.finalProject.PlagDetect.RemotePlagDetect;
import com.finalProject.Testing.TestResultParser;
import com.finalProject.Testing.TestRunner;

public class Evaluator implements AutoCloseable {
    public record EvalRow(
        Optional<Double> localPlagScore, 
        Optional<Double> remotePlagScore, 
        Optional<Double> localAiScore, 
        Optional<Double> remoteAiScore,
        Optional<Double> testRunScore,
        Optional<Double> remoteQualityScore,
        List<String> comments,
        List<String> errors
    ) {
        public static EvalRow empty() {return new EvalRow(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), List.of(), List.of());}
        public EvalRow withLocalPlagScore(Optional<Double> score) {return new EvalRow(score, this.remotePlagScore, this.localAiScore, this.remoteAiScore, this.testRunScore, this.remoteQualityScore, this.comments, this.errors);}
        public EvalRow withRemotePlagScore(Optional<Double> score) {return new EvalRow(this.localPlagScore, score, this.localAiScore, this.remoteAiScore, this.testRunScore, this.remoteQualityScore, this.comments, this.errors);}
        public EvalRow withLocalAiScore(Optional<Double> score) {return new EvalRow(this.localPlagScore, this.remotePlagScore, score, this.remoteAiScore, this.testRunScore, this.remoteQualityScore, this.comments, this.errors);}
        public EvalRow withRemoteAiScore(Optional<Double> score) {return new EvalRow(this.localPlagScore, this.remotePlagScore, this.localAiScore, score, this.testRunScore, this.remoteQualityScore, this.comments, this.errors);}
        public EvalRow withTestRunScore(Optional<Double> score) {return new EvalRow(this.localPlagScore, this.remotePlagScore, this.localAiScore, this.remoteAiScore, score, this.remoteQualityScore, this.comments, this.errors);}
        public EvalRow withRemoteQualityScore(Optional<Double> score) {return new EvalRow(this.localPlagScore, this.remotePlagScore, this.localAiScore, this.remoteAiScore, this.testRunScore, score, this.comments, this.errors);}
        public EvalRow withError(String error) {
            var updatedErrors = new ArrayList<>(errors);
            updatedErrors.add(error);
            return new EvalRow(this.localPlagScore, this.remotePlagScore, this.localAiScore, this.remoteAiScore, this.testRunScore, this.remoteQualityScore, this.comments, updatedErrors);
        }
        public EvalRow withComment(String comment) {
            var updatedComments = new ArrayList<>(comments);
            updatedComments.add(comment);
            return new EvalRow(this.localPlagScore, this.remotePlagScore, this.localAiScore, this.remoteAiScore, this.testRunScore, this.remoteQualityScore, updatedComments, this.errors);
        }
    }
    
    public record TokenUsage(long input, long output) {
        public static final TokenUsage ZERO = new TokenUsage(0, 0);
        public TokenUsage plus(TokenUsage other) {
            return new TokenUsage(this.input + other.input, this.output + other.output);
        }
    }

    public record EvalPart(Map<String, EvalRow> rows, TokenUsage tokenUsage) {}
    public record EvalResult(String runId, TokenUsage tokenUsage, Map<String, EvalRow> results) {}
    public record RemoteResult<T>(T value, TokenUsage tokenUsage) {}

    private final Settings settings;
    private final ExecutorService executor;
    private final RemotePlagDetect remotePlagDetect;
    private final RemoteAiDetect remoteAiDetect;
    private final RemoteScore remoteScore;

    public Evaluator(Settings settings) {
        this.settings = settings;
        var maxThreads = settings.user().maxThreads() <= 0 ? Runtime.getRuntime().availableProcessors() : settings.user().maxThreads();
        this.executor = Executors.newFixedThreadPool(maxThreads);
        this.remoteAiDetect = settings.eval().remoteAiDetect() ? new RemoteAiDetect(settings.assignment(), settings.model()) : null;
        this.remotePlagDetect = settings.eval().remotePlagDetect() ? new RemotePlagDetect(settings.assignment(), settings.model()) : null;
        this.remoteScore = settings.eval().remoteAiScore() ? new RemoteScore(settings.assignment(), settings.model()) : null;
    }

    @Override 
    public void close() {
        if(executor != null) {
            executor.shutdown();
        }
        if(remotePlagDetect != null) {
            remotePlagDetect.close();
        }
        if(remoteAiDetect != null) {
            remoteAiDetect.close();
        }
        if(remoteScore != null) {
            remoteScore.close();
        }
    }

    public CompletableFuture<EvalResult> evaluate(String submissionDir) {
        var runId = UUID.randomUUID().toString();
        var tasks = new ArrayList<CompletableFuture<EvalPart>>();
        
        if(settings.eval().localPlagDetect()) {
            var report = CompletableFuture.supplyAsync(() -> runLocalPlagDetect(submissionDir), executor);

            var localResults = report.thenApply(Evaluator::toMap).thenApply(m -> {
                return new EvalPart(m, TokenUsage.ZERO);
            });

            if(settings.eval().remotePlagDetect()) {
                var combinedPlagResults = report
                    .thenCompose(r -> runRemotePlagDetect(submissionDir, r))
                    .thenCombine(localResults, (remote, local) -> {
                        var rows = Evaluator.mergeResultMaps(remote.rows(), local.rows());
                        var tokenUsage = remote.tokenUsage().plus(local.tokenUsage());
                        return new EvalPart(rows, tokenUsage);
                    });
                tasks.add(combinedPlagResults);
            } else {
                tasks.add(localResults);
            }
        } else if(settings.eval().remotePlagDetect()) {
            throw new RuntimeException("Remote plagiarism detection requires local plagiarism detection to be enabled.");
        }

        tasks.add(CompletableFuture.supplyAsync(() -> {
            Map<String, EvalRow> map = new HashMap<>();
            if(settings.eval().localAiDetect()) {
                runLocalAiDetect(submissionDir, map);
            }
            return new EvalPart(map, TokenUsage.ZERO);
        }, executor));

        if(settings.eval().remoteAiDetect()) {
            tasks.add(runRemoteAiDetect(submissionDir));
        }
        if(settings.eval().remoteAiScore()) {
            tasks.add(runRemoteAiScore(submissionDir));
        }
        if(settings.eval().testRun()) {
            tasks.add(runTestRun(submissionDir));
        }

        return CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new))
            .thenApply(ignored -> {
                Map<String, EvalRow> combinedRows = new HashMap<>();
                TokenUsage combinedTokenUsage = TokenUsage.ZERO;
                for(var task : tasks) {
                    var part = task.join();
                    combinedTokenUsage = combinedTokenUsage.plus(part.tokenUsage());
                    part.rows().forEach((submission, partial) -> {
                        combinedRows.merge(submission, partial, Evaluator::mergeResults);
                    });
                }

                return new EvalResult(runId, combinedTokenUsage, Map.copyOf(combinedRows));
            });
    }

    private SimilarityReport runLocalPlagDetect(String submissionDir) {
        var detect = new LocalPlagDetect(settings.assignment(), submissionDir);
        return detect.RunSimilarity();
    }

    private static Map<String, EvalRow> toMap(SimilarityReport report) {
        var result = new HashMap<String, EvalRow>();
        report.highestScores().forEach((submission, bestMatch) -> {
            result.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withLocalPlagScore(Optional.of(bestMatch.score())));
        });
        return result;
    }
    
    private CompletableFuture<EvalPart> runRemotePlagDetect(String submissionDir, SimilarityReport localResult) {
        return remotePlagDetect.DetectPlag(localResult, submissionDir)
        .thenApply(results -> {
            var outResult = new HashMap<String, EvalRow>();
            results.value().forEach((submission, result) -> {
                outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withRemotePlagScore(Optional.of(result.score())));
                if(result != null && !result.evidence().isBlank()) {
                    outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withComment(result.evidence()));
                }
            });

            return new EvalPart(outResult, results.tokenUsage());
        });
    }
    
    private AiReport runLocalAiDetect(String submissionDir, Map<String, EvalRow> outResult) {        
        var report = new LocalAiDetect(settings.assignment(), submissionDir).DetectAi();
        report.highestScores().forEach((submission, bestMatch) -> {
            outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withLocalAiScore(Optional.of(bestMatch.score())));
        });
        return report;
    }

    private CompletableFuture<EvalPart> runRemoteAiDetect(String submissionDir) {
        return remoteAiDetect.DetectAi(submissionDir)
        .thenApply(report -> {
            var outResult = new HashMap<String, EvalRow>();
            report.value().forEach((submission, response) -> {
                outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withRemoteAiScore(Optional.of(response.aiLikenessScore())));
                if(response.comments() != null && !response.comments().isBlank()) {
                    outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withComment(response.comments()));
                }
            });
            return new EvalPart(outResult, report.tokenUsage());
        });
    }

    private CompletableFuture<EvalPart> runRemoteAiScore(String submissionDir) {
        return remoteScore.RemoteGrade(submissionDir)
        .thenApply(report -> {
            var outResult = new HashMap<String, EvalRow>();
            report.value().forEach((submission, response) -> {
                outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withRemoteQualityScore(Optional.of(response.totalScore())));
                outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withComment(String.format("Correctness: %.2f, Completeness: %.2f, Clarity: %.2f", response.correctness(), response.completeness(), response.clarity())));
                if(response.comments() != null && !response.comments().isBlank()) {
                    outResult.compute(submission, (key, current) -> (current == null ? EvalRow.empty() : current).withComment(response.comments()));
                }
            });
            return new EvalPart(outResult, report.tokenUsage());
        });
    }
    
    private record TestOutcome(String submission, EvalRow result, String errors) {}
    
    private CompletableFuture<EvalPart> runTestRun(String submissionDir) {
        TestRunner testRunner = new TestRunner(settings, new TestResultParser());
        
        List<CompletableFuture<TestOutcome>> jobs;

        try(var stream = Files.list(Path.of(submissionDir))) {
            jobs = stream
                .filter(Files::isDirectory)
                .map(path -> CompletableFuture
                    .supplyAsync(() -> testRunner.runTests(path.toString()), executor)
                    .thenCompose(future -> future)
                    .thenApply(result -> {
                        String submission = path.getFileName().toString();
                        if(result.testResult().isPresent()) {
                            var score = result.testResult().orElseThrow().getScore();
                            return new TestOutcome(submission, EvalRow.empty().withTestRunScore(Optional.of(score)), result.error().orElse(""));
                        } else {
                            return new TestOutcome(submission, EvalRow.empty(), result.error().orElse("Unknown test-run error"));
                        }
                })).toList();

                return CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    var outResult = new HashMap<String, EvalRow>();
                    for(var job : jobs) {
                        var outcome = job.join();
                        var row = outcome.result();
                        if(outcome.errors() != null && !outcome.errors().isBlank()) {
                            row = row.withError(outcome.errors());
                        }
                        outResult.put(outcome.submission(), row);
                    }
                    return new EvalPart(outResult, TokenUsage.ZERO);
                });
        } catch(IOException e) {
            throw new CompletionException(e);
        }
    }

    private static Map<String, EvalRow> mergeResultMaps(Map<String, EvalRow> current, Map<String, EvalRow> update) {
        var merged = new HashMap<>(current);
        update.forEach((key, value) -> merged.merge(key, value, Evaluator::mergeResults));
        return merged;
    }

    private static EvalRow mergeResults(EvalRow current, EvalRow update) {
        if(update.localPlagScore().isPresent()) {
            current = current.withLocalPlagScore(update.localPlagScore());
        }
        if(update.remotePlagScore().isPresent()) {
            current = current.withRemotePlagScore(update.remotePlagScore());
        }
        if(update.localAiScore().isPresent()) {
            current = current.withLocalAiScore(update.localAiScore());
        }
        if(update.remoteAiScore().isPresent()) {
            current = current.withRemoteAiScore(update.remoteAiScore());
        }
        if(update.testRunScore().isPresent()) {
            current = current.withTestRunScore(update.testRunScore());
        }
        if(update.remoteQualityScore().isPresent()) {
            current = current.withRemoteQualityScore(update.remoteQualityScore());
        }
        for(var comment : update.comments()) {
            current = current.withComment(comment);
        }
        for(var error : update.errors()) {
            current = current.withError(error);
        }
        return current;
    }
}
