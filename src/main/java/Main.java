import utilities.ScriptRunner;

public class Main {
    public static void main(String[] args) {
        try {
            if (new ScriptRunner("projectId", "master").cloneRepository(15000, false)) {
                if (new ScriptRunner("projectId", "master").testProject(15000, false)) {
                    if (new ScriptRunner("projectId", "master").cleanUpProject(15000, false)) {
                        System.out.println("Finished");
                    } else {
                        System.err.println("Failed to clean up the project");
                    }
                } else {
                    System.err.println("Failed to test the project");
                }
            } else {
                System.err.println("Failed to clone the project");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
