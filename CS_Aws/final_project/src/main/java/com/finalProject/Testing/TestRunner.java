package com.finalProject.Testing;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.finalProject.Settings;

import java.io.IOException;
public class TestRunner {
    private final Settings settings; 
    private final String reportPath = "\\report\\TEST-junit-jupiter.xml";
    private final TestResultParser parser;

    private record RunTestResult(int exitCode, String errors) {}

    public TestRunner(Settings settings, TestResultParser parser) {
        this.settings = settings;
        this.parser = parser;
    }

    public CompletableFuture<TestResultParser.TestResult> runTests(String submissionDir) {
        Path testerFilePath = Path.of(settings.assignment().testerPath());
        if(testerFilePath == null || !testerFilePath.toFile().isFile()) {
            throw new RuntimeException("Invalid tester file: " + settings.assignment().testerPath());
        }

        Path submissionPath = Path.of(submissionDir);
        if(submissionPath == null || !submissionPath.toFile().isDirectory()) {
            throw new RuntimeException("Invalid submission directory: " + submissionDir);
        }

        List<Path> javaFiles = new ArrayList<>();

        try (var stream = Files.walk(submissionPath)) {
            javaFiles = stream.filter(Files::isRegularFile)
            .filter(f -> f.toString().endsWith(".java"))
            .filter(f -> !f.getFileName().toString().toLowerCase().contains("tester"))
            .toList();
        } catch(IOException e) {
            throw new RuntimeException("Failed to list Java files in submission directory: " + submissionDir, e);
        }

        if(javaFiles.isEmpty()) {
            throw new RuntimeException("No Java files found in submission directory: " + submissionDir);
        }

        Path tempDir;
        try {
            tempDir = Files.createTempDirectory("auto-grader");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary directory", e);
        }

        List<Path> copiedJavaFiles = new ArrayList<>();
        for(Path javaFile : javaFiles) {
            Path dest = tempDir.resolve(submissionPath.relativize(javaFile));
            try {
                Files.createDirectories(dest.getParent());
                Files.copy(javaFile, dest);
                copiedJavaFiles.add(dest);
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy Java file: " + javaFile, e);
            }
        }
        try {
            Files.copy(testerFilePath, tempDir.resolve(testerFilePath.getFileName()));
            copiedJavaFiles.add(tempDir.resolve(testerFilePath.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy tester file: " + testerFilePath, e);
        }

        return runTestCommand(tempDir, copiedJavaFiles)
            .thenApply(testResult -> {
                if(testResult.exitCode != 0) {
                    return new TestResultParser.TestResult(0, 0, 0, testResult.errors());
                }

                return this.parser.getTestResult(Path.of(tempDir.toString(), reportPath));
            })
            .whenComplete((result, err) -> {
                try {
                    deleteRecursively(tempDir);
                } catch (IOException e) {
                    if(err != null) {
                        err.addSuppressed(e);
                    } else {
                        throw new CompletionException(e);
                    }
                }
            });
    }

    private CompletableFuture<RunTestResult> runTestCommand(Path tempDir, List<Path> javaFiles) {
        //&$javaPath/javac -cp /resources/junit-platform-console-standalone-6.2.0-M1.jar *.java
        List<String> compileCommand = new ArrayList<>();
        compileCommand.addAll(List.of(
            settings.user().javaDir() + "/javac.exe",
            "-cp",
            settings.user().junitJar(),
            "-d",
            tempDir.toString()
        ));
        compileCommand.addAll(javaFiles.stream()
            .map(f -> tempDir.relativize(f).toString())
            .toList());
        

        //&$javaPath/java -jar /resources/junit-platform-console-standalone-6.2.0-M1.jar execute --class-path . --scan-class-path --details=none --reports-dir report
        List<String> testCommand = List.of(
            settings.user().javaDir() + "/java.exe",
            "-jar",
            settings.user().junitJar(),
            "execute",
            "--class-path",
            ".",
            "--scan-class-path",
            "--reports-dir",
            "report"
        );

        Path errorLog = tempDir.resolve("compile.log");

        try {
            Process compileProcess = new ProcessBuilder(compileCommand)
                .directory(tempDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.to(errorLog.toFile()))
                .redirectError(ProcessBuilder.Redirect.to(errorLog.toFile()))
                .start();

            return exitCodeWithTimeout(compileProcess, settings.assignment().compileTimeout())
                .thenCompose(compileExitCode -> {
                    if(compileExitCode != 0) {
                        try {
                            return CompletableFuture.completedFuture(new RunTestResult(compileExitCode, Files.readString(errorLog)));
                        } catch (IOException e) {
                            throw new CompletionException(e);
                        }
                    }
                    try {
                        Process testProcess = new ProcessBuilder(testCommand)
                            .directory(tempDir.toFile())
                            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                            .redirectError(ProcessBuilder.Redirect.INHERIT)
                            .start();
                        return exitCodeWithTimeout(testProcess, settings.assignment().testTimeout()).thenApply(ignored -> {
                            return new RunTestResult(0, null);
                        });
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                });
        } catch (IOException e) {
            throw new CompletionException(e);
        }

    }

    private static void deleteRecursively(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static CompletableFuture<Integer> exitCodeWithTimeout(Process process, Duration timeout) {
        return process.onExit()
            .thenApply(Process::exitValue)
            .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
            .exceptionallyCompose(err -> {
                Throwable cause = err instanceof CompletionException && err.getCause() != null ? err.getCause() : err;
                if(!(cause instanceof TimeoutException)) {
                    return CompletableFuture.failedFuture(cause);
                }
                process.destroyForcibly();
                return process.onExit()
                    .thenCompose(ignored -> CompletableFuture.failedFuture(new TimeoutException("Process timed out after " + timeout)));
            });
    }
}
