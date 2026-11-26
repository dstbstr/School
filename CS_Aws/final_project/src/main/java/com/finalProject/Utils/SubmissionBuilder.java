package com.finalProject.Utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;

public class SubmissionBuilder {
    private static final String FLAG = "=====";

    public record NumberedSource(int id, String name, String code) {}

    public static HashMap<Integer, NumberedSource> buildNumberedSources(Path submissionDir) {
        var namedSources = buildNamedSources(submissionDir);
        
        HashMap<Integer, NumberedSource> sources = new HashMap<>();
        for(var entry : namedSources.entrySet()) {
            int id = sources.size() + 1;
            sources.put(id, new NumberedSource(id, entry.getKey(), entry.getValue()));
        }

        return sources;
    }

    public static HashMap<String, String> buildNamedSources(Path submissionDir) {
        HashMap<String, String> sources = new HashMap<>();
        try (var entries = Files.list(submissionDir)) {
            for(Path dir : entries.filter(Files::isDirectory).sorted(Comparator.comparing(p -> p.getFileName().toString())).toList()) {
                String name = dir.getFileName().toString();
                sources.put(name, SubmissionBuilder.buildSubmission(dir));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to list submission directories", e);
        }

        return sources;
    }

    public static String buildSubmission(Path fileOrDir) {
        if(!Files.exists(fileOrDir)) {
            throw new RuntimeException("File or directory does not exist: " + fileOrDir);
        }

        StringBuilder sb = new StringBuilder();
        if(Files.isDirectory(fileOrDir)) {
            appendDirectoryContent(sb, fileOrDir);
        } else if(Files.isRegularFile(fileOrDir)) {
            appendFileContent(sb, fileOrDir.toAbsolutePath().normalize(), fileOrDir);
        }

        return sb.toString();
    }

    private static void appendDirectoryContent(StringBuilder sb, Path dir) {
        try(var stream = Files.walk(dir).sorted((p1, p2) -> dir.relativize(p1).compareTo(dir.relativize(p2)))) {
            stream.filter(Files::isRegularFile)
                  .forEach(file -> appendFileContent(sb, dir.relativize(file), file));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read directory content", e);
        }
    }

    private static void appendFileContent(StringBuilder sb, Path relativePath, Path file) {
        try {
            sb.append(FLAG).append(relativePath).append(FLAG).append("\n");
            sb.append(Files.readString(file)).append("\n");
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file content", e);
        }
    }
}
