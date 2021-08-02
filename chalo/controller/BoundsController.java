package com.chalo.controller;

import com.chalo.bean.Route;
import com.chalo.exception.MyException;
import com.chalo.service.BoundService;
import com.chalo.service.RouteService;
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

import java.util.*;

@RestController
public class BoundsController {
    @Autowired
    private BoundService boundService;

    private static final Logger logger = LogManager.getLogger(RouteController.class);

    @RequestMapping(value = "{city}/{transport}/bounds", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<HashMap<String,Object>> getBounds(@PathVariable("city") String city,
                                            @PathVariable("transport") String transport) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        HashMap<String,Object> stops = BoundService.getUpdatedBounds(city, transport);

        return new ResponseEntity<HashMap<String,Object>>(stops, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "analysestops", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<HashMap<String,Object>> analyse() throws Exception {
        HttpHeaders headers = new HttpHeaders();

        boundService.analyseStops();

        HashMap<String,Object> stops = new HashMap<>();
        return new ResponseEntity<HashMap<String,Object>>(stops, headers, HttpStatus.OK);
    }
}
