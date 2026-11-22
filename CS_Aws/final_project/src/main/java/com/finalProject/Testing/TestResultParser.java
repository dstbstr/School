package com.finalProject.Testing;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;

public class TestResultParser {
    public record TestResult(int totalTests, int failures, int errors, String compileError) {
        public double getScore() {
            if (totalTests == 0) return 0.0;
            return (double) (totalTests - failures - errors) / totalTests * 100.0;
        }
    }

    public TestResult getTestResult(Path testResultFilePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            var doc = factory.newDocumentBuilder().parse(testResultFilePath.toFile());
            Element root = doc.getDocumentElement();
            
            int totalTests = Integer.parseInt(root.getAttribute("tests"));
            int failures = Integer.parseInt(root.getAttribute("failures"));
            int errors = Integer.parseInt(root.getAttribute("errors"));
            return new TestResult(totalTests, failures, errors, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
