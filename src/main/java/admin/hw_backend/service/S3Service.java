package admin.hw_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    public String uploadFile(MultipartFile file) throws IOException {


        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .contentType(file.getContentType())
                        .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(file.getBytes())
        );

        return endpoint
                .replace("/s3", "")
                + "/object/public/"
                + bucketName
                + "/"
                + fileName;
    }

    public void deleteFile(String fileName) {
        if(fileName == null || fileName.isEmpty() || fileName.startsWith("blob:")) {
            return;
        }
        try{
            String file = fileName.substring(fileName.lastIndexOf("/") + 1);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        }
        catch(Exception e){
            System.err.println("Blad przy usuwaniu: " + e.getMessage());
        }
    }
}