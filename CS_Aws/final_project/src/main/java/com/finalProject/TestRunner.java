package com.finalProject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.io.IOException;
public class TestRunner {
    private final UserSettings userSettings; 
    private final AssignmentSettings assignmentSettings; 

    public TestRunner(UserSettings settings, AssignmentSettings assignmentSettings) {
        this.userSettings = settings;
        this.assignmentSettings = assignmentSettings;
    }

    public CompletableFuture<String> runTests(String submissionDir) throws IOException {
        Path testerFilePath = Path.of(assignmentSettings.testerFile());
        if(testerFilePath == null || !testerFilePath.toFile().isFile()) {
            throw new IOException("Invalid tester file: " + assignmentSettings.testerFile());
        }

        Path submissionPath = Path.of(submissionDir);
        if(submissionPath == null || !submissionPath.toFile().isDirectory()) {
            throw new IOException("Invalid submission directory: " + submissionDir);
        }

        List<Path> javaFiles = new ArrayList<>();
        try (var stream = Files.walk(submissionPath)) {
            javaFiles = stream.filter(Files::isRegularFile)
            .filter(f -> f.toString().endsWith(".java"))
            .filter(f -> !f.getFileName().toString().toLowerCase().contains("tester"))
            .toList();
        }

        if(javaFiles.isEmpty()) {
            throw new IOException("No Java files found in submission directory: " + submissionDir);
        }

        Path tempDir = Files.createTempDirectory("auto-grader");
        List<Path> copiedJavaFiles = new ArrayList<>();
        for(Path javaFile : javaFiles) {
            Path relative = submissionPath.relativize(javaFile);
            Path dest = tempDir.resolve(relative);
            Files.createDirectories(dest.getParent());
            Files.copy(javaFile, dest);
            copiedJavaFiles.add(dest);
        }
        Files.copy(testerFilePath, tempDir.resolve(testerFilePath.getFileName()));
        copiedJavaFiles.add(tempDir.resolve(testerFilePath.getFileName()));

        return runTestCommand(tempDir, copiedJavaFiles)
            .thenApply(ignored -> tempDir.toString());
    }

    private CompletableFuture<Void> runTestCommand(Path tempDir, List<Path> javaFiles) {
        //&$javaPath/javac -cp /resources/junit-platform-console-standalone-6.2.0-M1.jar *.java
        List<String> compileCommand = new ArrayList<>();
        compileCommand.addAll(List.of(
            userSettings.javaDir() + "/javac.exe",
            "-cp",
            userSettings.junitJar(),
            "-d",
            tempDir.toString()
        ));
        compileCommand.addAll(javaFiles.stream()
            .map(f -> tempDir.relativize(f).toString())
            .toList());
        

        //&$javaPath/java -jar /resources/junit-platform-console-standalone-6.2.0-M1.jar execute --class-path . --scan-class-path --details=none --reports-dir report
        List<String> testCommand = List.of(
            userSettings.javaDir() + "/java.exe",
            "-jar",
            userSettings.junitJar(),
            "execute",
            "--class-path",
            ".",
            "--scan-class-path",
            "--reports-dir",
            "report"
        );

        try {
            Process compileProcess = new ProcessBuilder(compileCommand)
                .directory(tempDir.toFile())
                .inheritIO()
                .start();
            return requireZero(compileProcess)
                .thenCompose(ignored -> {
                    try {
                        Process testProcess = new ProcessBuilder(testCommand)
                        .directory(tempDir.toFile())
                        .inheritIO()
                        .start();
                        return testProcess.onExit().thenAccept(ignored2 -> {});
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                });
        } catch (IOException e) {
            throw new CompletionException(e);
        }

    }

    private static CompletableFuture<Void> requireZero(Process process) {
        return process.onExit().thenApply(exited -> {
            int code = exited.exitValue();
            if(code != 0) {
                throw new CompletionException(new IOException("Process failed with exit code " + code));
            }
            return null;
        });
    }
}
