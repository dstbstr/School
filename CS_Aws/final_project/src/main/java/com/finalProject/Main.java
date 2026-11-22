package com.finalProject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.finalProject.AiDetect.LocalAiDetect;
import com.finalProject.PlagDetect.LocalPlagDetect;
import com.finalProject.Testing.TestResultParser;
import com.finalProject.Testing.TestRunner;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.log.de.jplag", "error");

        Settings settings = new Settings(
            new Settings.User(
                "C:\\Program Files\\Java\\jdk-25\\bin",
                "Z:/source/repos/School/CS_Aws/final_project/src/main/resources/junit-platform-console-standalone-6.2.0-M1.jar",
                0
            ),
            new Settings.Assignment(
                "Z:/auto_grader_assets/Hw1/Tester.java",
                "Z:/auto_grader_assets/HW1/Provided.java",
                "Z:/auto_grader_assets/Hw1/AiSubmissions",
                null,
                "Z:/auto_grader_assets/Hw1/Results",
                Duration.ofSeconds(10), // compile timeout
                Duration.ofSeconds(20) // test timeout
            ),
            new Settings.Eval(
                true,  // localPlagDetect
                false,  // remotePlagDetect
                true,  // localAiDetect
                false,  // remoteAiDetect
                true   // testRun
            )

        );

        try(Evaluator evaluator = new Evaluator(settings)) {
            Path submissions = Path.of("Z:\\auto_grader_assets\\Hw1\\Submissions\\F25Hw1");
            var results = evaluator.evaluate(submissions.toString()).join();
            results.forEach((submission, result) -> {
                System.out.println("Submission: " + submission + ", Result: " + result);
            });
        }
        /*
        TestRunner testRunner = new TestRunner(settings, new TestResultParser());
        try(var stream = Files.list(Path.of("Z:/auto_grader_assets/Hw1/AiSubmissions"))) {
            stream.filter(Files::isDirectory)
                  .forEach(path -> {
                    var testResult = testRunner.runTests(path.toString()).join();
                    if(testResult.compileError() != null && testResult.compileError().length() > 0) {
                        System.out.println("Compile error in submission: " + path + "\n" + testResult.compileError());
                    }
                    System.out.println(path + " score: " + testResult.getScore());
                  }); 
        } catch(Exception e) {
            e.printStackTrace();
        }        

        LocalAiDetect aiDetect = new LocalAiDetect(settings.assignment(), "Z:/auto_grader_assets/Hw1/Submissions/F25Hw1");
        var aiReport = aiDetect.RunSimilarity();
        aiReport.highestScores().forEach((submission, bestMatch) -> {
            System.out.println("Submission: " + submission + ", Best Match: " + bestMatch.model() + ", Score: " + bestMatch.score());
        });
        */

        /*
        LocalPlagDetect plagDetect = new LocalPlagDetect(settings.assignment(), "Z:/auto_grader_assets/Hw1/Submissions/F25Hw1");
        var similarityReport = plagDetect.RunSimilarity();
        similarityReport.highestScores().forEach((submission, bestMatch) -> {
            System.out.println("Submission: " + submission + ", Best Match: " + bestMatch.submissionName() + ", Score: " + bestMatch.score());
        });
        */

        /*
        var maxThreads = settings.user().maxThreads() <= 0 ? Runtime.getRuntime().availableProcessors() : settings.user().maxThreads();
        try(CsvWriter csvWriter = new CsvWriter(settings.assignment());
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads))
        {
            TestRunner testRunner = new TestRunner(settings, new TestResultParser());
            
            Path submissions = Path.of("Z:\\auto_grader_assets\\Hw1\\Submissions\\F25Hw1");

            List<CompletableFuture<Void>> jobs;

            try(var stream = Files.list(submissions)) {
                jobs = stream.filter(Files::isDirectory)
                //.limit(1)
                .map(path -> {
                    return CompletableFuture.supplyAsync(() -> testRunner.runTests(path.toString()).join(), executor)
                        .handle((result, ex) -> {
                            csvWriter.writeResult(new CsvWriter.Result(path.getFileName().toString(), ex == null ? result.getScore() : 0, ex == null ? result.compileError() : ex.toString()));
                            return (Void) null;
                        });
                }).toList();
            }
            CompletableFuture.allOf(jobs.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            e.printStackTrace();
        }
            */
    }
}

/*
Notes:
Need to allow user to specify java directory (consider checking JAVA_HOME for default)

*/