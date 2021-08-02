package com.chalo.service;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface BoundService {
    public static HashMap<String, Object> getUpdatedBounds(String city, String transport) throws Exception {
        return null;
    }

    public void analyseStops() throws Exception;
}
