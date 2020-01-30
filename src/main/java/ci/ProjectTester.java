package ci;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import server.ContinuousIntegrationServer;
import utilities.Helpers;
import utilities.ScriptRunner;

public class ProjectTester {
    private static final Logger logger = Logger.getLogger(ContinuousIntegrationServer.class);

    private final String branch;
    private final String author;
    private final String id;
    private final String repositoryName;
    private boolean cloned = false;
    private boolean tested = false;
    private boolean cleanedUp = false;

    public ProjectTester(JSONObject jsonObject) {
        id = Helpers.generateId(Helpers.getHeadCommitId(jsonObject));
        branch = Helpers.getBranch(jsonObject);
        author = Helpers.getAuthor(jsonObject);
        repositoryName = Helpers.getRepositoryName(jsonObject);
    }

    public ProjectTester(final String repositoryName, final String headCommitId, final String branch, final String author) {
        this.repositoryName = repositoryName;
        this.branch = branch;
        this.author = author;
        id = Helpers.generateId(headCommitId);
        logger.info("Started test of repository: " + repositoryName + ", branch: " + branch + ", pushed by: " + author + ", id: " + headCommitId);
    }

    public boolean processPush() {
        final long timeOut = 10000;
        ScriptRunner scriptRunner = new ScriptRunner(id, repositoryName, branch);

        for (int i = 0; i < 3 && !cloned; i++) {
            cloned = scriptRunner.cloneRepository(timeOut, false);
        }

        for (int i = 0; i < 3 && cloned && !tested; i++) {
            tested = scriptRunner.testProject(timeOut, false);
        }

        /*for (int i = 0; i < 3 && !cleanedUp; i++) {
            cleanedUp = scriptRunner.cleanUpProject(timeOut, false);
        }*/

        return cleanedUp;
    }
}
