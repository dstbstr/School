package com.finalProject.Reporting;

import java.nio.file.Files; 
import java.nio.file.Path;

import com.finalProject.Evaluator;
import com.finalProject.Settings;

public final class CommentWriter {
    private final Settings.Assignment settings;

    public CommentWriter(Settings.Assignment settings) {
        this.settings = settings;
    }

    public void writeComments(Evaluator.EvalResult results) {
        try {
            Files.createDirectories(getCommentFilePath(results.runId(), "sample").getParent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for(var entry : results.results().entrySet()) {
            StringBuilder sb = new StringBuilder();
            var result = entry.getValue();
            if(result.errors().size() > 0) {
                sb.append("Errors: \n");
                sb.append(String.join("\n", result.errors()));
            }

            if(result.comments().size() > 0) {
                sb.append("Comments: \n");
                sb.append(String.join("\n", result.comments()));
            }

            try(var writer = Files.newBufferedWriter(getCommentFilePath(results.runId(), entry.getKey()))) {
                writer.write(sb.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Path getCommentFilePath(String runId, String submission) {
        return Path.of(settings.outDir(), runId, submission + ".txt");
    }
}