package com.chalo.controller;

import com.chalo.bean.Route;
import com.chalo.exception.MyException;
import com.chalo.service.BoundService;
import com.chalo.service.RouteService;
import com.chalo.utils.CsvUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.security.spec.ECField;
import java.util.*;

@RestController
public class DupCitiesController {
    @Autowired
    private BoundService boundService;

    private static final Logger logger = LogManager.getLogger(RouteController.class);

    private ArrayList<String> appCities = new ArrayList<String>(Arrays.asList("agra","bhopal","bhubaneswar","chennai","guwahati","indore","jabalpur","kanpur","kochi","kolkata","lucknow","mangaluru","mathura","meerut","nagpur","patna","prayagraj","udupi","vijayawada","amritsar","davanagere","kottayam","thrissur"));;

    @RequestMapping(value = "getduplicateroutes", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<HashMap<String,Object>> getDuplicateRoutes() throws Exception{
        HttpHeaders headers = new HttpHeaders();

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
        HashMap<String,Object> validCities = new HashMap<>();

        HashMap<String,Integer> uniqRoutes = new HashMap<>();
        for(String key: allCities.keySet())
        {
            if(key.equalsIgnoreCase("uttarpradesh"))
                key = "up";
            Map<String,Object> value = (Map<String,Object>)allCities.get(key);
            if(value.containsKey("StationType"))
            {
//                System.out.println(key);
                for(String mode: ((HashMap<String,Object>)value.get("StationType")).keySet())
                {
                    request = new Request.Builder()
                            .url("https://dazzling-fire-3689.firebaseio.com/development/cityData/" + key.toLowerCase() +  "/Routes/" + mode.toUpperCase() +".json")
                            .build();

                    call = client.newCall(request);

                    response = call.execute();
                    res = objectMapper.readValue(response.body().string(), Object.class);

                    Map<String,Object> city = (HashMap<String,Object>)res;


                    for(String routeID : city.keySet())
                    {
                        Map<String,Object> route = (Map<String,Object>)city.get(routeID);

                        String spf = "null";
                        if(route.containsKey("sf"))
                            spf = route.get("sf").toString();
                        else if(route.containsKey("spf"))
                            spf = route.get("spf").toString();

                        String routeFeatures = key + "_" + route.get("ag") + "_" + route.get("name") + "_" + route.get("f")  + "_" + route.get("l") + "_" + spf;

                        if(uniqRoutes.containsKey(routeFeatures))
                            uniqRoutes.put(routeFeatures, uniqRoutes.get(routeFeatures) + 1);
                        else
                            uniqRoutes.put(routeFeatures, 1);
                    }

                }
                validCities.put(key,value);

            }

        }


        Set<String> dupRoutes = new HashSet<>();

        for(String routeFeatures: uniqRoutes.keySet())
        {
            if(uniqRoutes.get(routeFeatures)>1)
                dupRoutes.add(routeFeatures);
        }


        List<String[]> CsvList = new ArrayList<String[]>();

        String[] headings = new String[]{"City","Mode","Agency","RouteID","Name","First Stop","Last Stop","Special Flag"};
        CsvList.add(headings);

        for(String key: allCities.keySet())
        {
            if(key.equalsIgnoreCase("uttarpradesh"))
                key = "up";

            if(!(appCities.contains(key.toLowerCase()))){
                System.out.println(key);
                continue;
            }

            Map<String,Object> value = (Map<String,Object>)allCities.get(key);
            if(value.containsKey("StationType"))
            {
                for(String mode: ((HashMap<String,Object>)value.get("StationType")).keySet())
                {
                    request = new Request.Builder()
                            .url("https://dazzling-fire-3689.firebaseio.com/development/cityData/" + key.toLowerCase() +  "/Routes/" + mode.toUpperCase() +".json")
                            .build();

                    call = client.newCall(request);


                    response = call.execute();
                    res = objectMapper.readValue(response.body().string(), Object.class);

                    Map<String,Object> city = (HashMap<String,Object>)res;


                    for(String routeID : city.keySet())
                    {
                        Map<String,Object> route = (Map<String,Object>)city.get(routeID);
                        String spf = "null";
                        if(route.containsKey("sf"))
                            spf = route.get("sf").toString();
                        else if(route.containsKey("spf"))
                            spf = route.get("spf").toString();

                        String routeFeatures = key + "_" + route.get("ag") + "_" + route.get("name") + "_" + route.get("f")  + "_" + route.get("l") + "_" + spf;

                        if(dupRoutes.contains(routeFeatures)){

                            try{
                                String[] row = new String[8];
                                row[0] = key;
                                row[1] = mode;
                                row[2] = (String)route.get("ag");
                                row[3] = routeID;
                                row[4] = (String)route.get("name");
                                row[5] = (String)route.get("f");
                                row[6] = (String)route.get("l");
                                row[7] = spf;

                                System.out.println(key + "   " + route.get("ag") + "    " + route.get("name") + "   " + route.get("f") + "   " + route.get("l") + "   " + route.get("di") + "   " + route.get("sf"));

                                CsvList.add(row);
                            }
                            catch(Exception e) {
                                System.out.println(e);
                            }
                        }
                    }

                }

            }

        }

        CsvUtils.writeDataAtOnce("/home/abhishek/Chalo/Repos/FirebaseCRUD_Lucknow/src/main/resources/dupRoutes.csv", CsvList);
        System.out.println("Written to csv");

        return new ResponseEntity<HashMap<String,Object>>(validCities, headers, HttpStatus.OK);
    }
}
