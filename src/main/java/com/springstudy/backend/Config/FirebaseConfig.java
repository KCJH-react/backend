package com.springstudy.backend.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.bucketName}")
    private String bucketName;

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase_secret_key.json");
            FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
            FirebaseOptions options = optionBuilder
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(bucketName)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}