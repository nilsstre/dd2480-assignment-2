package utilities;

import ci.AWSFileUploader;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.MissingResourceException;

public class Helpers {
    private static final Logger logger = Logger.getLogger(Helpers.class);

    @NotNull
    public static String generateId(final String headCommitId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        return headCommitId + "_" + dtf.format(now);
    }

    public static String getHeadCommitId(@NotNull JSONObject jsonObject) {
        return jsonObject.getJSONObject("head_commit").get("id").toString();
    }

    public static String getBranch(@NotNull JSONObject jsonObject) {
        String ref = jsonObject.get("ref").toString();
        return ref.split("^(refs/heads/)")[1];
    }

    public static String getAuthor(@NotNull JSONObject jsonObject) {
        return jsonObject.getJSONObject("pusher").get("name").toString();
    }

    public static String getRepositoryName(@NotNull JSONObject jsonObject) {
        return jsonObject.getJSONObject("repository").get("name").toString();
    }

    public static String getCloneURL(@NotNull JSONObject jsonObject) {
        return jsonObject.getJSONObject("repository").get("clone_url").toString();
    }

    public static void setUpConfiguration(@NotNull String[] args) {
        if (args.length != 2) throw new MissingResourceException("Missing configuration values", "", "");
        Configuration.BUCKET_NAME = args[0];
        Configuration.S3_BUCKET_ADDRESS = args[1];

        AWSFileUploader awsFileUploader = new AWSFileUploader();
        Configuration.PREVIOUS_BUILDS = awsFileUploader.getReports();

        System.out.println("AWS ACCESS KEY: " + Configuration.AWS_ACCESS_KEY_ID);
        System.out.println("AWS SECRET KEY: " + Configuration.AWS_SECRET_KEY);

        if (Configuration.AWS_ACCESS_KEY_ID.isEmpty()) {
            throw new MissingResourceException("The AWS access key id is missing", "", "");
        } else if (Configuration.AWS_SECRET_KEY.isEmpty()){
            throw new MissingResourceException("The AWS secret key is missing", "", "");
        } else if (Configuration.GITHUB_TOKEN.isEmpty()) {
            throw new MissingResourceException("The Github token is missing", "", "");
        } else if (Configuration.M3_HOME.isEmpty()) {
            throw new MissingResourceException("The M3 Home path is missing", "", "");
        } else if (Configuration.BUCKET_NAME.isEmpty()) {
            throw new MissingResourceException("The AWS bucket name is missing", "", "");
        } else if (Configuration.S3_BUCKET_ADDRESS.isEmpty()) {
            throw new MissingResourceException("The AWS bucket address is missing", "", "");
        }
    }

    public static void updatePreviousBuilds(final String newReport) {
        Configuration.PREVIOUS_BUILDS.add(newReport);
    }

    public static boolean cleanUp(final String id) {
        try {
            FileUtils.deleteDirectory(new File(Configuration.PATH_TO_GIT + id));
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("The directory " + id + " does not exist or is not a directory", e);
        } catch (IOException e) {
            logger.error("Failed to delete the directory: " + id, e);
        }
        return false;
    }
}
