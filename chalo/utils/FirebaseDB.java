package com.chalo.utils;

import com.chalo.bean.Route;
import com.chalo.controller.RouteController;
import com.chalo.exception.MyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.squareup.okhttp.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FirebaseDB {
    private static FileInputStream serviceAccount;
    private static FirebaseOptions options;
    public static DatabaseReference ref;
    private static FirebaseDatabase database;

    private static final Logger logger = LogManager.getLogger(RouteController.class);

    Object document;
    Boolean exists;

    // add different types of constructors
    public static void FirebaseInit() {
        try {
            serviceAccount = new FileInputStream("/home/abhishek/Chalo/Repos/FirebaseCRUD_Lucknow/src/main/resources/FirebaseAdminKey.json");

            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://java-dev-90f8f-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();

            FirebaseApp.initializeApp(options);

            database = FirebaseDatabase.getInstance();
            ref = database.getReference();
        }
        catch(Exception e){
            System.out.println("Can't find the secret key : " + e);
        }
    }

    public Object read(String url) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        DatabaseReference postRef = ref.child(url);

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                document = dataSnapshot.getValue();
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        try{
            countDownLatch.await();
        }
        catch (InterruptedException e){
            throw new Exception(e);
        }

        return document;
    }

    public Object readPaginated(String url, int page, int limit) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(1);
        DatabaseReference postRef = ref.child(url);


        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://java-dev-90f8f-default-rtdb.asia-southeast1.firebasedatabase.app/"+url+".json?shallow=true")
                .build();


        List<String> sortedRouteKeys = null;
        Call call = client.newCall(request);

        try {
            Response response = call.execute();

            ObjectMapper objectMapper = new ObjectMapper();
            Object res = objectMapper.readValue(response.body().string(), Object.class);

            Map<String,Object> resObj = (Map<String,Object>)res;


            Set<String> routeKeys = resObj.keySet();
            sortedRouteKeys = new ArrayList<>(routeKeys);
            Collections.sort(sortedRouteKeys);

        }
        catch(Exception e) {
            throw new Exception(e);
        }

        // if the
        if(sortedRouteKeys.size()<(page*limit)){
            throw new MyException("500","Page limit out of bounds","Parameter 'page' exceeding the number of pages");
        }

        postRef.orderByKey().limitToFirst(limit).startAt(sortedRouteKeys.get((page-1)*limit)+1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                document = dataSnapshot.getValue();
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        try{
            countDownLatch.await(5,TimeUnit.SECONDS);
        }
        catch (InterruptedException e){
            logger.error("Timeout, cannot retrieve page content");
            throw new MyException("500","Cannot retrieve page","Countdown latch timeout");
        }

        return document;
    }

    public void write(String url, Route writeRoute) throws Exception{
        DatabaseReference postRef = ref.child(url);
        CountDownLatch readLatch = new CountDownLatch(1);
        exists = false;

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    exists = true;
                }
                readLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        readLatch.await();
        if(exists)
            throw new MyException("500","Route already exists","Route with given id already exists");


        ApiFuture<Void> future = postRef.setValueAsync(writeRoute);
    }

    public void delete(String url) throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(2);
        DatabaseReference delRef = ref.child(url);

        Map<String,String> readRoute = (Map<String,String>)read(url);
        String revKey = readRoute.get("rr");

        logger.warn("You are going to delete the following routes \n" + delRef + "\n" + delRef.getParent().child(revKey));

        ApiFuture<Void> future1 = delRef.removeValueAsync();
        ApiFuture<Void> future2 = delRef.getParent().child(revKey).removeValueAsync();
        if(future1!=null)
            countDownLatch.countDown();

        if(future2!=null)
            countDownLatch.countDown();

        countDownLatch.countDown();
        countDownLatch.countDown();

        if(countDownLatch.getCount()>0){
            logger.error("Reverse route does not exist");
            throw new MyException("400","Reverse route not found","No reverse route exists for the given routeID");
        }

        try{
            countDownLatch.await(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e){
            System.out.println("Cannot decrement latch");
        }
    }

}
