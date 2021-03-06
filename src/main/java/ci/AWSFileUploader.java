package ci;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.log4j.Logger;
import utilities.Configuration;
import utilities.Helpers;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AWSFileUploader {
    private static final Logger logger = Logger.getLogger(AWSFileUploader.class);
    private final AmazonS3 s3Client;

    public AWSFileUploader() {
        AWSCredentials credentials = new BasicAWSCredentials(Configuration.AWS_ACCESS_KEY_ID, Configuration.AWS_SECRET_KEY);

        Regions clientRegion = Regions.EU_NORTH_1;
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(clientRegion).build();
    }


    public boolean uploadFile(final String fileName) {
        try {
            final String folderFileName = "reports/" + fileName + ".txt";
            PutObjectRequest request = new PutObjectRequest(
                    Configuration.BUCKET_NAME,
                    folderFileName,
                    new File(Configuration.PATH_TO_REPORTS + fileName + ".txt")
            );

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("x-amz-meta-title", fileName);
            request.setMetadata(metadata);
            s3Client.putObject(request);
            Helpers.updatePreviousBuilds(fileName);
            logger.info("Uploaded file: " + fileName + " to S3 bucket: " + Configuration.BUCKET_NAME);
            return true;
        } catch (AmazonServiceException e) {
            logger.error("Amazon S3 failed to process the file: " + fileName, e);
        } catch (SdkClientException e) {
            logger.error("Failed to contact Amazon S3 or the client couldn't parse the response from Amazon S3", e);
        }

        return false;
    }

    public Set<String> getReports() {
        try {
            final Set<String> reports = new HashSet<>();
            ObjectListing objectListing = s3Client.listObjects(Configuration.BUCKET_NAME);
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                if (objectSummary.getKey().matches("reports/[A-Za-z0-9_-]+.txt")) {
                    reports.add(objectSummary.getKey().split("reports/")[1]);
                }
            }
            return reports;
        } catch (AmazonServiceException e) {
            logger.error("Amazon S3 failed to return list of objects for bucket: " + Configuration.BUCKET_NAME, e);
        } catch (SdkClientException e) {
            logger.error("Failed to contact Amazon S3 or the client couldn't parse the response from Amazon S3", e);
        }

        return new HashSet<>();
    }
}
