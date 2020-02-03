import ci.GitRepositoryHandler;
import ci.MavenRunner;
import ci.ProjectTester;
import utilities.Helpers;

public class Main {
    public static void main(String[] args) {
        try {
            Helpers.setUpConfiguration(args);
            GitRepositoryHandler gitRepositoryHandler = new GitRepositoryHandler("test", "webhook-test", "https://github.com/nilsstre/webhook-test.git");
            gitRepositoryHandler.cloneRepository();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
