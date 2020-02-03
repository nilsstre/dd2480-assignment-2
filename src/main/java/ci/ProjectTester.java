package ci;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import utilities.Helpers;

public class ProjectTester {
    private static final Logger logger = Logger.getLogger(ProjectTester.class);

    private final String branch;
    private final String author;
    private final String id;
    private final String repositoryName;
    private final String cloneURL;
    private boolean cloned = false;
    private boolean tested = false;
    private boolean cleanedUp = false;

    public ProjectTester(JSONObject jsonObject) {
        id = Helpers.generateId(Helpers.getHeadCommitId(jsonObject));
        branch = Helpers.getBranch(jsonObject);
        author = Helpers.getAuthor(jsonObject);
        repositoryName = Helpers.getRepositoryName(jsonObject);
        cloneURL = Helpers.getCloneURL(jsonObject);
        logger.info("Started test of repository: " + repositoryName + ", branch: " + branch + ", pushed by: " + author + ", id: " + Helpers.getHeadCommitId(jsonObject));
    }

    public ProjectTester(final String repositoryName, final String headCommitId, final String branch, final String author, final String cloneURL) {
        this.repositoryName = repositoryName;
        this.branch = branch;
        this.author = author;
        this.cloneURL = cloneURL;
        id = Helpers.generateId(headCommitId);
    }

    public void processPush() {
        logger.info("Started test of repository: " + repositoryName + ", branch: " + branch + ", pushed by: " + author + ", id: " + id);

        AWSFileUploader awsFileUploader = new AWSFileUploader();
        MavenRunner mavenRunner = new MavenRunner(id, repositoryName);
        GitRepositoryHandler gitRepositoryHandler = new GitRepositoryHandler(id, repositoryName, cloneURL);

        for (int i = 0; i < 3 && !cloned; i++) {
            cloned = gitRepositoryHandler.cloneRepository();
        }

        for (int i = 0; i < 3 && cloned && !tested; i++) {
            tested = mavenRunner.runProject();
        }

        if (tested) {
            awsFileUploader.uploadFile(id);
        }

        for (int i = 0; i < 3 && !cleanedUp; i++) {
            //cleanedUp = Helpers.cleanUp(id);
        }
    }
}
