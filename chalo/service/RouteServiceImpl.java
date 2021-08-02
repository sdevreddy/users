package com.chalo.service;

import com.chalo.bean.Route;
import com.chalo.controller.RouteController;
import com.chalo.exception.MyException;
import com.chalo.utils.FirebaseDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service("RouteService")
public class RouteServiceImpl implements RouteService{

    final ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(RouteController.class);

    FirebaseDB f = new FirebaseDB();

    RouteServiceImpl() {
        FirebaseDB.FirebaseInit();
    }

    public Route getRoute(String city, String transport, String id) throws Exception{
        Route route = new Route();


        String url = "toolDatabase/cityData/" + city + "/Routes/" + transport.toUpperCase() + "/"+id;
        Object readResponse = f.read(url);

        try {
            route = mapper.convertValue(readResponse, Route.class);
        }
        catch(Exception e) {
            throw new Exception(e);
        }

        return route;
    }


    public void addRoute(String city, String id, String transport, Route route) throws Exception {
        f.write("toolDatabase/cityData/" + city + "/Routes/" + transport.toUpperCase() + "/"+id, route);
    }



    public void deleteRoute(String city, String transport, String id) throws Exception{
        f.delete("toolDatabase/cityData/" + city + "/Routes/" + transport.toUpperCase() + "/"+id);
    }


    public void updateRoute(String city, String id, String transport, Map<String,Object> reqParams) throws Exception{

        HashMap<String,Object> readResponse = (HashMap<String, Object>) f.read("toolDatabase/cityData/" + city + "/Routes/" + transport.toUpperCase() + "/"+id);

        DatabaseReference routeRef = FirebaseDB.ref.child("toolDatabase/cityData/" + city + "/Routes/" + transport.toUpperCase() + "/"+id);

        for(Map.Entry param : reqParams.entrySet()) {
            readResponse.put((String)param.getKey(),param.getValue());
        }

        CountDownLatch latch = new CountDownLatch(1);

        routeRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Map<String,String> val = (Map<String,String>) mutableData.getValue(Map.class);

                if(val.get("unActive")=="false"){
                    mutableData.setValue(readResponse);
                    return Transaction.success(mutableData);
                }
                return null;
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                latch.countDown();
            }
        });

        try {
            latch.await(5, TimeUnit.SECONDS);
        }
        catch(Exception e) {
            throw new MyException("500","Transaction failed","UnActive has already been set to true");
        }

        CountDownLatch latch2 = new CountDownLatch(1);

        ApiFuture<Void> future = routeRef.updateChildrenAsync(readResponse);
        if(future!=null)
            latch2.countDown();

        latch2.await();

    }
}
