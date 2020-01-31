package utilities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.MissingResourceException;

public class Helpers {
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
        if (args.length != 5) throw new MissingResourceException("Missing configuration values", "", "");
        Configuration.GITHUB_TOKEN = args[0];
        Configuration.BUCKET_NAME = args[1];
        Configuration.AWS_ACCESS_KEY_ID = args[2];
        Configuration.AWS_SECRET_KEY = args[3];
        Configuration.S3_BUCKET_ADDRESS = args[4];

        AWSFileUploader awsFileUploader = new AWSFileUploader();
        Configuration.PREVIOUS_BUILDS = awsFileUploader.getReports();
    }

    public static void updatePreviousBuilds(final String newReport) {
        Configuration.PREVIOUS_BUILDS.add(newReport);
    }
}
