package com.example.bus_route.controller;

import com.example.bus_route.model.Route;
import com.example.bus_route.service.RouteService;
import com.google.gson.Gson;
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

    //private static final Logger logger = LogManager.getLogger(RouteController.class);

    @RequestMapping(value = "{city}/{transport}/route", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Route>> getRoute(@PathVariable("city") String city,
                                                @PathVariable("transport") String transport,
                                                @RequestParam("ids") String[] ids) throws Exception{
        HttpHeaders headers = new HttpHeaders();

        List<Route> routes = new ArrayList<>();

        for(String id: ids) {
           // System.out.println("ID is:"+ id);
            Route route = routeService.getRoute(city, transport, id);
            routes.add(route);
        }

        return new ResponseEntity<List<Route>>(routes, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "{city}/{transport}/routes", method = RequestMethod.GET, produces = "application/json")
    public String getRoutes(@PathVariable("city") String city,
                                                @PathVariable("transport") String transport) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        return routeService.getRoutes(city, transport);

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

       // routeService.updateRoute(city, id, transport, reqParams);
        routeService.updateRoute(city, id, transport, reqParams);
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }
}
