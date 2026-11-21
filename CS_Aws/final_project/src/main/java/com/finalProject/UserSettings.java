package com.finalProject;

// TODO: add in other settings as needed (report out director, model type, etc.)
public record UserSettings(String javaDir, String junitJar) {
    public UserSettings(String javaDir, String junitJar) {
        this.javaDir = javaDir;
        this.junitJar = junitJar;
    }

    public UserSettings withJavaDir(String javaDir) {
        return new UserSettings(javaDir, this.junitJar);
    }

    public UserSettings withJunitJar(String junitJar) {
        return new UserSettings(this.javaDir, junitJar);
    }
}
