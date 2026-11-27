package com.finalProject.Testing;

import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestResultParser {
    public record TestResult(int totalTests, int failures, int errors, List<String> failedTestNames, String compileError) {
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
            List<String> failedTestNames = new ArrayList<>();
            var testCases = root.getElementsByTagName("testcase");
            for (int i = 0; i < testCases.getLength(); i++) {
                Element testCase = (Element) testCases.item(i);
                if (testCase.getElementsByTagName("failure").getLength() > 0 || testCase.getElementsByTagName("error").getLength() > 0) {
                    failedTestNames.add(testCase.getAttribute("name"));
                }
            }
            return new TestResult(totalTests, failures, errors, List.copyOf(failedTestNames), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
