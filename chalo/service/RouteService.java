package service;

import java.util.List;
import java.util.Map;

import bean.Route;
import org.springframework.stereotype.Repository;

@Repository()
public interface RouteService {
    public Route getRoute(String city, String transport, String id) throws Exception;
    public void deleteRoute(String city, String transport, String id) throws Exception;
    public void updateRoute(String city, String id, String transport, Map<String,Object> reqParams) throws Exception;
    public void addRoute(String city, String id, String transport, Route route) throws Exception;
}