package com.ex.demo.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitializer {
    @PostConstruct
    private void initDB () throws IOException {
       // InputStream serviceAccount = this.getClass().getClassLoader()
         //       .getResourceAsStream("./test-76a97-firebase-adminsdk-f0nwh-7669860750.json");
        InputStream serviceAccount =this.getClass().getClassLoader()
                 .getResourceAsStream("./test-76a97-firebase-adminsdk-f0nwh-69a7cad73d.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
       // FirebaseApp.initializeApp(options);
    }
    public Firestore getFirebase() {
        return FirestoreClient.getFirestore();
    }
}
