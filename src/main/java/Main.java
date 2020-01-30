import org.apache.log4j.BasicConfigurator;
import utilities.ScriptRunner;

public class Main {
    public static void main(String[] args) {
        try {
            if (new ScriptRunner().cloneRepository()) {
                if (new ScriptRunner().testProject()) {
                    if (new ScriptRunner().cleanUpProject()) {
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
