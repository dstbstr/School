package com.randalld;

public class ProgressReport {
    @FunctionalInterface
    public interface ProgressDisplay {
        void update(double progress);
    }

    private final ProgressDisplay display;

    public ProgressReport(ProgressDisplay display) {
        this.display = display;
    }

    public void update(long current, long total) {
        double progress = (double)current / (double)total;
        display.update(progress);
    }
}
