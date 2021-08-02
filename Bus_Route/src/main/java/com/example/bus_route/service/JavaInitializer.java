package com.example.bus_route.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;


public class JavaInitializer {

    FirebaseDatabase db;

    public void FireBaseService() throws IOException {
       // File file = new File(
        //        getClass().getClassLoader().getResource("key.json").getFile);

        FileInputStream fis = new FileInputStream("./firebase-adminsdk.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fis))
                .setDatabaseUrl("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);

     //   db = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase getDb() {
        //return db;
        return FirebaseDatabase.getInstance();
    }


}
