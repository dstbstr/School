package com.finalProject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class TestResultParser {
    private DocumentBuilder builder;

    public TestResultParser() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize TestResultParser", e);
        }
    }

    public double getTestScore(String testResultFilePath) {
        try {
            Document doc = this.builder.parse(new File(testResultFilePath));

            Element root = doc.getDocumentElement();
            
            int totalTests = Integer.parseInt(root.getAttribute("tests"));
            int failures = Integer.parseInt(root.getAttribute("failures"));
            int errors = Integer.parseInt(root.getAttribute("errors"));
            return ((double) (totalTests - failures - errors) / totalTests) * 100;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
