package com.chalo.controller;

import com.chalo.service.BusService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BusController {

    @Autowired
    private BusService busService;

    private static final Logger logger = LogManager.getLogger(RouteController.class);

    @RequestMapping(value = "/{city}/routes/{transport}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Object>> getBusRoutes(@PathVariable("transport") String transport,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("limit") int limit,
                                                     @PathVariable("city") String city) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        List<Object> busRoutes = busService.getRoutes(city, transport, page, limit);

        if (busRoutes == null) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Object>>(busRoutes, headers, HttpStatus.OK);
    }

}
