package com.chalo.service;

import com.chalo.utils.FirebaseDB;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("BusService")
public class BusServiceImpl implements BusService {
    final ObjectMapper mapper = new ObjectMapper();
    FirebaseDB f = new FirebaseDB();

    public List<Object> getRoutes(String city, String transport, int page, int limit) throws Exception{
        HashMap<String, Object> readResponse = (HashMap<String, Object>) f.readPaginated("toolDatabase/cityData/" + city + "/Routes/" + transport.toUpperCase(), page, limit);
        List<Object> busRoutes = new ArrayList<Object>(readResponse.values());
        return busRoutes;

    }
}
