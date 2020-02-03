package ci;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import utilities.Configuration;

import java.io.File;
import java.net.URL;

public class GitRepositoryHandler {
    private static final Logger logger = Logger.getLogger(GitRepositoryHandler.class);
    private final String id;
    private final String repositoryName;
    private final String cloneURL;

    public GitRepositoryHandler(final String id, final String repositoryName, final String cloneURL) {
        this.id = id;
        this.repositoryName = repositoryName;
        this.cloneURL = cloneURL;
    }

    public boolean cloneRepository() {
        logger.info("Cloning repository: " + repositoryName);

        CloneCommand cloneCommand = Git.cloneRepository();
        cloneCommand.setURI(cloneURL);
        cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Configuration.GITHUB_TOKEN, ""));
        cloneCommand.setDirectory(new File(Configuration.PATH_TO_GIT + id + "/" + repositoryName));
        try {
            cloneCommand.call();
            return true;
        } catch (TransportException e) {
            logger.error("Transport operation from Github remote failed", e);
        } catch (InvalidRemoteException e) {
            logger.error("Invalid remote repository", e);
        } catch (GitAPIException e) {
            logger.error("Something went wrong with the Github API", e);
        }

        return false;
    }
}
