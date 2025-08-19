package com.springstudy.backend.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase_secret_key.json");
            FirebaseOptions.Builder optionBuilder = FirebaseOptions.builder();
            FirebaseOptions options = optionBuilder
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("imgstorage-6ba5c.firebasestorage.app")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}