package demo;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import demo.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.remoteconfig.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
//import Test2.*;

public class AdminSDKDemo {


    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("getResponse");
        FileInputStream serviceAccount =
                new FileInputStream("./service-account-file.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://test-76a97-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);

        CountDownLatch sample1 = new CountDownLatch(1);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  ref = database.getReference();
        DatabaseReference usersRef = ref.child("users");
        ApiFuture<Void> sample = usersRef.child("asdf4").setValueAsync(new User("Nov 9, 1200", "dcsdf Hopper"));

        /*try {
            if (sample.get() != null) {
                sample1.countDown();
            }

            sample1.await(5000, TimeUnit.MILLISECONDS);
        }
        catch (Exception e){
            System.out.println("error");
        }*/
        sample1.countDown();
        sample1.await() ;

    }
}
