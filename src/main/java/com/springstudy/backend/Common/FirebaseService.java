package com.springstudy.backend.Common;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseService {

    @Value("${firebase.bucketName}")
    private String bucketName;

    public String getFileUrl(String fileName) {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(fileName);

        if (blob == null) return null;

        // 1시간 동안 유효한 URL 생성
        URL url = blob.signUrl(1, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());

        return url.toString();
    }

    public void uploadFile(String fileName, byte[] content, String contentType) {
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);
        bucket.create(fileName, content, contentType);
    }

    public boolean deleteFile(String fileUrl) {
        Bucket bucket = StorageClient.getInstance().bucket(bucketName);

        try {
            // URL에서 fileName 추출 (버킷 경로 이후 부분)
            String prefix = "https://storage.googleapis.com/" + bucketName + "/";
            String fileName;

            if (fileUrl.startsWith(prefix)) {
                fileName = fileUrl.substring(prefix.length());
            } else {
                // signedUrl일 경우 쿼리 파라미터 제거
                fileName = fileUrl.split("\\?")[0].replaceAll(".*/o/", "").replace("%2F", "/");
            }

            Blob blob = bucket.get(fileName);
            if (blob != null) {
                return blob.delete(); // 성공하면 true
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
