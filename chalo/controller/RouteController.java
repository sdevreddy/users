package com.chalo.controller;

import com.chalo.bean.Route;
import com.chalo.exception.MyException;
import com.chalo.service.RouteService;
import com.google.gson.Gson;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RouteController {

    @Autowired
    private RouteService routeService;

    private static final Logger logger = LogManager.getLogger(RouteController.class);

    @RequestMapping(value = "{city}/{transport}/route", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Route>> getRoute(@PathVariable("city") String city,
                                                @PathVariable("transport") String transport,
                                                @RequestParam("ids") String[] ids) throws Exception{
        HttpHeaders headers = new HttpHeaders();

        List<Route> routes = new ArrayList<>();

        for(String id: ids) {
            Route route = routeService.getRoute(city, transport, id);
            routes.add(route);
        }

        if (routes.get(0) == null) {
            String errorMessage = "No Routes Found: Invalid Database URL";
            logger.error(errorMessage);
            throw new MyException("404","Invalid Database URL", "Null value present at the specified url");
        }
        return new ResponseEntity<List<Route>>(routes, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "{city}/route/{transport}/{id}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Route> addRoute(@PathVariable("id") String id,
                                          @PathVariable("transport") String transport,
                                          @PathVariable("city") String city,
                                          @RequestBody String payload) throws Exception{
        HttpHeaders headers = new HttpHeaders();

        Gson gson = new Gson();

        Route route = gson.fromJson(payload, Route.class);

        routeService.addRoute(city,id,transport,route);

        return new ResponseEntity<Route>(route, headers, HttpStatus.CREATED);
    }


    @RequestMapping(value = "{city}/route/{transport}/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Void> deleteRoute(@PathVariable("id") String id,
                                            @PathVariable("city") String city,
                                            @PathVariable("transport") String transport) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        routeService.deleteRoute(city,transport,id);

        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }


    @RequestMapping(value = "{city}/route/{transport}/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<Void> updateRoute(@PathVariable("id") String id,
                                            @PathVariable("city") String city,
                                            @PathVariable("transport") String transport,
                                            @RequestBody String payload) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        Gson gson = new Gson();
        Map<String,Object> reqParams = gson.fromJson(payload, Map.class);

        routeService.updateRoute(city, id, transport, reqParams);
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }
}
