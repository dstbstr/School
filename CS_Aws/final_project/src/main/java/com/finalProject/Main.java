package com.finalProject;

public class Main {
    public static void main(String[] args) {
        UserSettings userSettings = new UserSettings("C:\\Program Files\\Java\\jdk-25\\bin", "Z:/source/repos/School/CS_Aws/final_project/src/main/resources/junit-platform-console-standalone-6.2.0-M1.jar");
        AssignmentSettings assignmentSettings = new AssignmentSettings("Z:/source/repos/School/CS_Aws/auto_grader_assets/Hw1/Tester.java", "Z:/source/repos/School/CS_Aws/auto_grader_assets/Hw1/Temp");
        String submission = "Z:\\source\\repos\\School\\CS_Aws\\auto_grader_assets\\Hw1\\Submissions\\F25Hw1\\1_4100720_139664267_1";
        
        TestRunner testRunner = new TestRunner(userSettings, assignmentSettings);
        try {
            String resultDir = testRunner.runTests(submission).join();
            TestResultParser parser = new TestResultParser();

            double score = parser.getTestScore(resultDir + "\\report\\TEST-junit-vintage.xml");

            System.out.println("Test score: " + score);
        } catch (Exception e) {
            e.printStackTrace();        
        }
    }
}

/*
Notes:
Need to allow user to specify java directory (consider checking JAVA_HOME for default)
Use java dir for both javac and java commands

For each submission, 
    copy submission into temp directory
    copy tester into temp directory
    run javac on the copied files explicitly (no need for *.java)
    var javaPath
    var junitJar = final_project/src/main/resources/junit-platform-console-standalone-6.2.0-M1.jar
    run from the temp directory
        javaPath/java -jar junitJar execute --class-path . --scan-class-path --details=none --reports-dir report
    read the TEST-junit-vintage.xml files
    parse the results, looking for the count of failures
    pass rate = ((total tests - failures) / total tests) * 100

    clear temp dir

*/