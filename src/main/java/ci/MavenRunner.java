package ci;

import org.apache.log4j.Logger;
import org.apache.maven.shared.invoker.*;
import utilities.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collections;

public class MavenRunner {
    private static final Logger logger = Logger.getLogger(MavenRunner.class);
    private final String projectId;
    private final String repositoryName;

    public MavenRunner(final String projectId, final String repositoryName) {
        this.projectId = projectId;
        this.repositoryName = repositoryName;
    }

    public boolean runProject() {

        final File temp1 = new File(Configuration.PATH_TO_REPORTS);
        String[] tempArray1 = temp1.list();

        for (String file : tempArray1) {
            System.out.println("File: " + file);
        }

        final File temp2 = new File("./");
        String[] tempArray2 = temp2.list();

        for (String file : tempArray2) {
            System.out.println("File: " + file);
        }

        InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(new File(Configuration.PATH_TO_GIT + projectId + "/" + repositoryName));
        request.setGoals(Collections.singletonList("test"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(Configuration.M3_HOME));

        try {
            logger.info("Running test for Maven project in repository: " + repositoryName);

            PrintStream writeToFile = new PrintStream(new FileOutputStream(Configuration.PATH_TO_REPORTS + projectId + ".txt"));
            InvocationOutputHandler outputHandler = new PrintStreamHandler(writeToFile, false);

            invoker.setOutputHandler(outputHandler);
            invoker.execute(request);

            InvocationResult result = invoker.execute(request);

            if (result.getExitCode() != 0) {
                throw new IllegalStateException("Build failed");
            }

            return true;
        } catch (MavenInvocationException e) {
            logger.error("Error while trying to run testes", e);
        } catch (IllegalStateException e) {
            logger.error("Build failed", e);
        } catch (FileNotFoundException e) {
            logger.error("Could not find report file", e);
        }

        return false;
    }
}
