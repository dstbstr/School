package com.finalProject;

public record AssignmentSettings(String testerFile, String outputDir) {
    public AssignmentSettings(String testerFile, String outputDir) {
        this.testerFile = testerFile;
        this.outputDir = outputDir;
    }

    public AssignmentSettings withTesterFile(String testerFile) {
        return new AssignmentSettings(testerFile, this.outputDir);
    }

    public AssignmentSettings withOutputDir(String outputDir) {
        return new AssignmentSettings(this.testerFile, outputDir);
    }
}
