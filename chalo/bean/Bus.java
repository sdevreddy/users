package com.chalo.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Bus {

    public static Object routes;
//    public static HashMap<Object, Object> METRO;

    @JsonCreator
    public Bus(@JsonProperty("routes") Object routes) {
        this.routes = routes;
    }

    public Bus() {

    }

    public void setRoutes(Object routes) {
        this.routes = routes;
    }

    public Object getRoutes() {
        return routes;
    }

}
