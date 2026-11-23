package com.finalProject;

import java.time.Duration;

public record Settings(Settings.User user, Settings.Assignment assignment, Settings.Eval eval) {
    public record User(String javaDir, String junitJar, String rootAssignmentDir, int maxThreads) {
        public User withJavaDir(String javaDir) {
            return new User(javaDir, this.junitJar, this.rootAssignmentDir, this.maxThreads);
        }

        public User withJunitJar(String junitJar) {
            return new User(this.javaDir, junitJar, this.rootAssignmentDir, this.maxThreads);
        }
        
        public User withRootAssignmentDir(String rootAssignmentDir) {
            return new User(this.javaDir, this.junitJar, rootAssignmentDir, this.maxThreads);
        }

        public User withMaxThreads(int maxThreads) {
            return new User(this.javaDir, this.junitJar, this.rootAssignmentDir, maxThreads);
        }
    }
    
    public record Assignment(String testerPath, String baseCode, String aiCodeDir, String gradingPrompt, String similarityPrompt, String outDir, Duration compileTimeout, Duration testTimeout) {
        public Assignment withTesterPath(String testerPath) {
            return new Assignment(testerPath, this.baseCode, this.aiCodeDir, this.gradingPrompt, this.similarityPrompt, this.outDir, this.compileTimeout, this.testTimeout);
        }

        public Assignment withBaseCode(String baseCode) {
            return new Assignment(this.testerPath, baseCode, this.aiCodeDir, this.gradingPrompt, this.similarityPrompt, this.outDir, this.compileTimeout, this.testTimeout);
        }

        public Assignment withAiCodeDir(String aiCodeDir) {
            return new Assignment(this.testerPath, this.baseCode, aiCodeDir, this.gradingPrompt, this.similarityPrompt, this.outDir, this.compileTimeout, this.testTimeout);
        }

        public Assignment withGradingPrompt(String gradingPrompt) {
            return new Assignment(this.testerPath, this.baseCode, this.aiCodeDir, gradingPrompt, this.similarityPrompt, this.outDir, this.compileTimeout, this.testTimeout);
        }

        public Assignment withSimilarityPrompt(String similarityPrompt) {
            return new Assignment(this.testerPath, this.baseCode, this.aiCodeDir, this.gradingPrompt, similarityPrompt, this.outDir, this.compileTimeout, this.testTimeout);
        }

        public Assignment withOutDir(String outDir) {
            return new Assignment(this.testerPath, this.baseCode, this.aiCodeDir, this.gradingPrompt, this.similarityPrompt, outDir, this.compileTimeout, this.testTimeout);
        }

        public Assignment withCompileTimeout(Duration compileTimeout) {
            return new Assignment(this.testerPath, this.baseCode, this.aiCodeDir, this.gradingPrompt, this.similarityPrompt, this.outDir, compileTimeout, this.testTimeout);
        }

        public Assignment withTestTimeout(Duration testTimeout) {
            return new Assignment(this.testerPath, this.baseCode, this.aiCodeDir, this.gradingPrompt, this.similarityPrompt, this.outDir, this.compileTimeout, testTimeout);
        }
    }

    public record Eval(boolean localAiDetect, boolean remoteAiDetect, boolean localPlagDetect, boolean remotePlagDetect, boolean testRun, boolean remoteAiScore) {
        public Eval withLocalAiDetect(boolean localAiDetect) {
            return new Eval(localAiDetect, this.remoteAiDetect, this.localPlagDetect, this.remotePlagDetect, this.testRun, this.remoteAiScore);
        }
        public Eval withRemoteAiDetect(boolean remoteAiDetect) {
            return new Eval(this.localAiDetect, remoteAiDetect, this.localPlagDetect, this.remotePlagDetect, this.testRun, this.remoteAiScore);
        }
        public Eval withLocalPlagDetect(boolean localPlagDetect) {
            return new Eval(this.localAiDetect, this.remoteAiDetect, localPlagDetect, this.remotePlagDetect, this.testRun, this.remoteAiScore);
        }
        public Eval withRemotePlagDetect(boolean remotePlagDetect) {
            return new Eval(this.localAiDetect, this.remoteAiDetect, this.localPlagDetect, remotePlagDetect, this.testRun, this.remoteAiScore);
        }
        public Eval withTestRun(boolean testRun) {
            return new Eval(this.localAiDetect, this.remoteAiDetect, this.localPlagDetect, this.remotePlagDetect, testRun, this.remoteAiScore);
        }
        public Eval withRemoteAiScore(boolean remoteAiScore) {
            return new Eval(this.localAiDetect, this.remoteAiDetect, this.localPlagDetect, this.remotePlagDetect, this.testRun, remoteAiScore);
        }
    }


    public Settings withUser(User user) {
        return new Settings(user, this.assignment, this.eval);
    }
    
    public Settings withAssignment(Assignment assignment) {
        return new Settings(this.user, assignment, this.eval);
    }

    public Settings withEval(Eval eval) {
        return new Settings(this.user, this.assignment, eval);
    }
}
