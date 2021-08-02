package com.chalo.controller;

import com.chalo.exception.MyException;
import com.google.api.Http;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<HashMap<String,String>> handleMyException(MyException mex) {
        HashMap<String,String> res = new HashMap<>();
        res.put("type","/errors/custom-error");
        res.put("title",mex.getErrMsg());
        res.put("status",mex.getErrCode());
        res.put("detail",mex.getErrDetailMsg());

        return new ResponseEntity<HashMap<String,String>>(res, HttpStatus.valueOf(Integer.parseInt(mex.getErrCode())));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<HashMap<String,String>> handleMyException(Exception ex) {
//        HashMap<String,String> res = new HashMap<>();
//        res.put("type","/errors/generic-error");
//        res.put("detail",ex.getMessage());
//        res.put("status","500");
//        res.put("title","Error occurred during runtime");
//
//        return new ResponseEntity<HashMap<String,String>>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
