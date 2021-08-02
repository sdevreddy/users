package com.chalo.service;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository()
public interface BusService {
    public List<Object> getRoutes(String city, String transport, int page, int limit) throws Exception;
}
