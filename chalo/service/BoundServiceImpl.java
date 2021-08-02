package com.chalo.service;

import com.chalo.utils.FirebaseDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("BoundService")
public class BoundServiceImpl implements BoundService {

    public static void main(String[] args) {

        try {
            System.out.println("Indore");
            System.out.println(getUpdatedBounds("indore","BUS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final ObjectMapper mapper = new ObjectMapper();
    FirebaseDB f = new FirebaseDB();

    public static HashMap<String,Object> getUpdatedBounds(String city, String transport) throws Exception {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://dazzling-fire-3689.firebaseio.com/development/cityData/" + city + "/Stops/" + transport.toUpperCase() + ".json")
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        Object res = objectMapper.readValue(response.body().string(), Object.class);

        HashMap<String,Object> stops = (HashMap<String,Object>)res;


        request = new Request.Builder()
                .url("https://dazzling-fire-3689.firebaseio.com/development/metaData/" + city + "/bound.json")
                .build();

        call = client.newCall(request);

        response = call.execute();
        res = objectMapper.readValue(response.body().string(), Object.class);

        HashMap<String,Double> bounds = (HashMap<String,Double>)res;


//        String url = "development/cityData/" + city + "/Stops/" + transport.toUpperCase() ;
//        Object readStops = f.read(url);
//
//        url = "development/metaData/" + city + "/bound";
//        HashMap<String,Double> bounds = (HashMap<String, Double>) f.read(url);

        double bottom = bounds.get("lat1");
        double top = bounds.get("lat2");
        double left = bounds.get("lon1");
        double right = bounds.get("lon2");

//        // For BHOPAL since meta data is not available
//        HashMap<String,Double> bounds = new HashMap<>();
//        double bottom = 23.1239;
//        double top = 23.4119;
//        double left = 77.2322;
//        double right = 77.7465;


        HashMap<String,Double> postBounds = new HashMap<>();
//        HashMap<String,Object> stops = (HashMap<String,Object>)readStops;

        HashMap<String,Object> boundsResponse = new HashMap<>();


        System.out.println("Total stops = " + stops.size());

        double preBottom = 22.5449000;
        double preTop = 22.8952000;
        double preLeft = 75.5736000;
        double preRight = 76.1627000;

        double meanLat = (left+right)/2;
        double meanLon = (bottom+top)/2;

        bounds.put("lat1",bottom);
        bounds.put("lat2",top);
        bounds.put("lon1",left);
        bounds.put("lon2",right);

//        boundsResponse.put("pre",bounds);
        System.out.println("Pre bounds " + bounds);

        int nStops = 0;

        for (Object value : stops.values()) {
            HashMap<String,Object> field = (HashMap<String, Object>) value;

            if(field.containsKey("o")){
                ArrayList<String> o = (ArrayList<String>) field.get("o");
                if(o.size()==1 && o.get(0).equals("etim"))
                    continue;
            }


            double pointLat = Double.parseDouble(field.get("lat").toString());
            double pointLon = Double.parseDouble(field.get("lon").toString());

            meanLat += pointLat;
            meanLon += pointLon;

            nStops++;
        }


        meanLat /= nStops;
        meanLon /= nStops;

        List<Object> outliers = new ArrayList<>();
        List<Object> outstops = new ArrayList<>();
        List<Object> onlyETIM = new ArrayList<>();

        for(String key: stops.keySet()){

            HashMap<String,Object> field = (HashMap<String, Object>) stops.get(key);

            if(field.containsKey("o")){
                ArrayList<String> o = (ArrayList<String>) field.get("o");
                if(o.size()==1 && o.get(0).equals("etim")) {
                    onlyETIM.add(field);
                    continue;
                }
            }

            double pointLat = Double.parseDouble(field.get("lat").toString());
            double pointLon = Double.parseDouble(field.get("lon").toString());

            String ok = "no";
            if(pointLat > preTop || pointLat < preBottom || pointLon > preRight || pointLon < preLeft) {
//                System.out.println(field);
                ok = "yes";
//                outstops.add(field);
            }
            System.out.println(field + "    " + ok);

//            if( Math.abs(pointLat - meanLat) > (preTop-preBottom) || Math.abs(pointLon - meanLon) > (preRight - preLeft)){
////                HashMap<String,Object> outlier  = new HashMap<>();
////                outlier.put(key,field);
//                outliers.add(key);
//                continue;
//            }

            bottom = Math.min(bottom,pointLat);
            top = Math.max(top,pointLat);
            left = Math.min(left,pointLon);
            right = Math.max(right,pointLon);

        }

        postBounds.put("lat1",bottom);
        postBounds.put("lat2",top);
        postBounds.put("lon1",left);
        postBounds.put("lon2",right);

//        boundsResponse.put("post",postBounds);
        System.out.println("Post bounds " + postBounds);

//        boundsResponse.put("FaultyStops",outstops);
//        System.out.println("Faulty Stops : " + outstops.size() + outstops);
//        boundsResponse.put("outliers",outliers);
//        boundsResponse.put("only ETIM",onlyETIM);

//        System.out.println(onlyETIM.size());
//        System.out.println(outliers.size());

        return boundsResponse;

    }

    public void analyseStops() throws Exception {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://dazzling-fire-3689.firebaseio.com/development/metaData/.json")
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();
        ObjectMapper objectMapper = new ObjectMapper();
        Object res = objectMapper.readValue(response.body().string(), Object.class);

        HashMap<String,Object> allCities = (HashMap<String,Object>)res;

        for(String key: allCities.keySet())
        {
            if(key.equalsIgnoreCase("uttarpradesh"))
                key = "up";

            System.out.println(key);
            Map<String,Object> value = (Map<String,Object>)allCities.get(key);
            if(value.containsKey("StationType"))
            {
                for(String mode: ((HashMap<String,Object>)value.get("StationType")).keySet())
                {

                    Set<String> stopSet = new HashSet<>();

                    request = new Request.Builder()
                            .url("https://dazzling-fire-3689.firebaseio.com/development/cityData/" + key.toLowerCase() +  "/Stops/" + mode.toUpperCase() +".json?shallow=true")
                            .build();

                    call = client.newCall(request);

                    response = call.execute();
                    res = objectMapper.readValue(response.body().string(), Object.class);

                    for( String stop: ((HashMap<String,Object>)res).keySet()){
                        stopSet.add(stop);
                    }



                    request = new Request.Builder()
                            .url("https://dazzling-fire-3689.firebaseio.com/development/cityData/" + key.toLowerCase() +  "/Routes/" + mode.toUpperCase() +".json")
                            .build();

                    call = client.newCall(request);

                    response = call.execute();
                    res = objectMapper.readValue(response.body().string(), Object.class);

                    HashMap<String,Object> routes = (HashMap<String, Object>) res;

                    for(Object routeObj: routes.values()){
                        HashMap<String,Object> route = (HashMap<String, Object>) routeObj;

                        List seq = (ArrayList) route.get("seq");
                        for (Object stopId : seq)
                        {
                            stopSet.remove(stopId.toString());
                        }
                    }

                    List<String> outliers = (ArrayList) ((HashMap<String, Object>) getUpdatedBounds(key,mode)).get("outliers");

                    int ct=0;
                    for(String outlier: outliers)
                    {
                        if(stopSet.contains(outlier))
                            ct++;
                    }

                    System.out.println("Number of outliers that are unused stops = " + ct);
                }
            }

        }
    }
}
