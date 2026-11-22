package com.finalProject.PlagDetect;

import com.finalProject.Settings;

public class RemotePlagDetect {
    public record AiResult(String submissionName, double score) {}

    public RemotePlagDetect(Settings.Assignment settings, LocalPlagDetect.SimilarityReport localReport) {

    }
}
