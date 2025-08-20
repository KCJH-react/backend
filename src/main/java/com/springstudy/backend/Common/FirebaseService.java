package com.springstudy.backend.Common;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
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
}
