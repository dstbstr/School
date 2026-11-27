# Auto Grader

## Running
Use run.ps1 to run the Maven build and run the GUI. It depends on Maven (mvn) existing on the Path, and having a Java version of 25 or later.
On Linux run `mvn exec:java -Dexec.mainClass=com.finalProject.Main` from the project directory.

## AWS setup
In order to use the Remote functions, the application needs access to an AWS account.
The current way of doing this involves:
- Log into an AWS account
- Search for the Bedrock module in the console
- Go to the API Keys tab of the Discover group
- Generate a short-term key
- Copy the provided bearer token (it's good for 12 hours)
- Set this in your environment
    - Powershell: `$env:AWS_BEARER_TOKEN_BEDROCK="<token>"`
    - Command Prompt: `set AWS_BEARER_TOKEN_BEDROCK="<token>"`
    - Bash: `export AWS_BEARER_TOKEN_BEDROCK="<token>"
- If running from an IDE, restart the IDE to get the new environment variables.

## Arguments
There is a CLI built into the program to enable scripting. Running `run.ps1 --help` to see available commands.
Running with no arguments starts the GUI

## Settings
There are several kinds of settings in the project:
- **User**: Machine specific details.
    - **javaDir**: Allows specifying where the javac and java executables are.
    - **junitJar**: Allows overriding where the JUnit jar is. One comes bundled with the application.
    - **rootAssignmentDir**: The root directory holding assignments.
    - **maxThreads**: How many threads to spawn when running tests. 0 defaults to the number of processors on the machine.
- **Eval**: Controls which checks to run.
    - **localAiDetect**: Compare submissions against AI generated solutions. Requires AiGenerated submissions.
    - **remoteAiDetect**: Asks a remote model to check if a submission looks AI generated.
    - **localPlagDetect**: Compare each submission against all other submissions along with any base code provided.
    - **remotePlagDetect**: Asks a remote model to provide a plagiarism score.
    - **testRun**: Compile and run tests against the provided Tester file.
    - **remoteAiScore**: Asks a remote model to evaluate the submission and provide a score.
- **Model**: Controls details about which model to use and how.
    - **modelId**: Which Bedrock model to use.
    - **region**: Which region to send the request against.
    - **maxTokens**: The maximum number of tokens to spend.
    - **temperature**: The model's temperature.
    - **plagThreshold**: How similar submissions need to look to evaluate them remotely.
- **Assignment**: Assignment specific details (one per assignment).
    - **testerPath**: The path to the test file to run in JUnit.
    - **baseCode**: Any base code provided for the assignment.
    - **aiCodeDir**: AI generated solutions to the problem.
    - **gradingPrompt**: The text to send to the remote AI score model.
    - **similarityPrompt**: The text to send to the remote plagiarism model.
    - **aiLikenessPrompt**: The text to send to the remote AI detection model.
    - **outDir**: Where to store results of an evaluation.
    - **compileTimeout**: How long to wait for a submission to compile.
    - **testTimeout**: How long to wait for a submission's tests.

## Plagiarism Detection
Local plagiarism detection works using the JPlag library to compare similarity scores between each submission.
If there is a base submission which all submissions are based on, this is taken in as an argument.
Once JPlag returns the results, the LocalPlagDetector keeps just the strongest similarity score for each submission.

For remote plagiarism detection, we take those local results as an input to avoid an $O(N^2)$ number of requests to the model.
The RemotePlagDetector looks for clusters of similar submissions, and sends each cluster to the model.
For instance, if there are 5 submissions, and 1 is most like 3, 2 is most like 3, and 3 is most like 1, and 4 and 5 are most similar to each other, the remote model would send 2 requests.
One would contain the base submission along with 1, 2 and 3.
The second request would contain the base submission along with 4 and 5.

The RemotePlagDetector also takes a threshold, and only includes submissions which have a similarity score which meets or exceeds the provided threshold.

## Results
For each evaluation, a UUID is generated as the run id.
Within the directory pointed to by outDir a folder with that id is created.
For each submission included in the evaluation, a file is created.
This file stores all the errors and comments from the steps run in the evaluation.
Additionally, if "Export Csv" is clicked, then the results will be stored under the run id.

## Submission Structure
In general, the expected structure is
- RootAssignmentDir
    - Assignment_A
        - Provided.java
        - Tester.java
        - AiSubmissions
            - Model1
                - javaFiles
            - Model2
                - javaFiles
        - Submissions
            - ArbitraryNesting
                - Submission1
                    - javaFiles
                - Submission2
                    - javaFiles
    - Assignment_B
        - Provided.java
        - Tester.java
        - ...

If the names Provided.java, Tester.java, and AiSubmissions are used, then creating an assignment will pick them up automatically, otherwise they can be overwritten when creating an assignment.
        