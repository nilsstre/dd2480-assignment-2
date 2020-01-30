import ci.ProjectTester;
import utilities.ScriptRunner;

public class Main {
    public static void main(String[] args) {
        try {
            ProjectTester projectTester = new ProjectTester("TetsId", "dd2480-assignment-2","master", "nils");
            projectTester.processPush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
